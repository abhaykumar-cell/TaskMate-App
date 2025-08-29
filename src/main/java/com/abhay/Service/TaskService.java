package com.abhay.Service;

import com.abhay.Dto.TaskDto;
import com.abhay.Entity.Task;
import com.abhay.Entity.MyUser;
import com.abhay.Exception.ResourceNotFoundException;
import com.abhay.Repository.TaskRepo;
import com.abhay.Repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private TaskRepo taskRepo;
    private ModelMapper modelMapper;
    private UserRepository userRepo;
    private EmailService emailService;

    public TaskService(TaskRepo taskRepo, ModelMapper modelMapper, UserRepository userRepo, EmailService emailService) {
        this.taskRepo = taskRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }
    @Operation(summary = "Add Your Task ")
    public TaskDto addTask(TaskDto taskDto){
        MyUser user = userRepo.findById(taskDto.getUserid()).orElseThrow(()->new ResourceNotFoundException("User Not Found with id : "+taskDto.getUserid()));

        Task task = modelMapper.map(taskDto, Task.class);
        task.setUser(user);

        TaskDto res = modelMapper.map(taskRepo.save(task),TaskDto.class);
        res.setUserid(taskDto.getUserid());

        return res;
    }
    @Operation(summary = "Get all Task by User id")
    public Page<TaskDto> showTask(Integer userid,int size, int page){

        List<Task> taskList = taskRepo.findAllTaskByUserId(userid);
        List<TaskDto> taskDtoList = taskList.stream().map(task->{
           TaskDto dto =  modelMapper.map(task, TaskDto.class);
           dto.setUserid(userid);
           return dto;
        }).toList();
        Pageable pageable = PageRequest.of(page,size);
        return new PageImpl<>(taskDtoList,pageable,size);


    }
    @Operation(summary = "Update Your Task")
    public TaskDto updateTask(TaskDto taskDto){
        Task task = taskRepo.findByUserId(taskDto.getUserid()).orElseThrow(()->new ResourceNotFoundException("Task Not Present by given id : "+taskDto.getUserid()));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        taskRepo.save(task);
        TaskDto res = modelMapper.map(task, TaskDto.class);
        res.setUserid(taskDto.getUserid());
        return res;
    }
    @Operation(summary = "Delete Your Task")
    public void deleteTask(Integer id){
        Task task = taskRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not present with id "+ id));
        taskRepo.delete(task);
    }

    @Operation(summary = "Get all Completed Task")
    public Page<TaskDto> getAllCompletedTask(Integer id,int size, int page){

        Pageable pageable = PageRequest.of(page,size);
        Page <Task> taskPage = taskRepo.findByUserIdAndCompletedTrue(id,pageable);
        Page<TaskDto> taskDtoPage = taskPage.map(task->{
            TaskDto dto =  modelMapper.map(task, TaskDto.class);
            dto.setUserid(id);
            return dto;
        });
        return taskDtoPage;

}
@Operation(summary = " Get All Pending Task ")
    public Page<TaskDto> getAllPendingTask(Integer id,int size, int page){

        Pageable pageable = PageRequest.of(page,size);
        Page <Task> taskPage = taskRepo.findByUserIdAndCompletedFalse(id,pageable);
        Page<TaskDto> taskDtoPage = taskPage.map(task->{
            TaskDto dto =  modelMapper.map(task, TaskDto.class);
            dto.setUserid(id);
            return dto;
        });
        return taskDtoPage;

    }
    @Scheduled(fixedRate = 3600000)
    public void doEmailforPendingTasks(){
        List<Task> pendingTask =taskRepo.findPendingTasksDueWithin(false, LocalDateTime.now().plusHours(2));
        for (Task i : pendingTask){
             String userEmail =i.getUser().getEmail();
             emailService.SendEmail(userEmail,"Upcoming Task Due Date – Action Required","Dear "+i.getUser().getName()+",\n" +
                     "\n" +
                     "This is a friendly reminder that the due date for your assigned task is approaching soon.\n" +
                     "Please ensure to complete the task at the earliest to avoid any delays.\n" +
                     "\n" +
                     "If you have any questions or need assistance, feel free to reach out.\n" +
                     "\n" +
                     "Best regards,\n" +
                     "Task Mate");


        }
    }
}
