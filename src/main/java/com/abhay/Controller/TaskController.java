package com.abhay.Controller;

import com.abhay.Dto.TaskDto;
import com.abhay.Entity.Task;
import com.abhay.Service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@Tag(name="Task APIs")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody TaskDto taskDto){
        TaskDto res = taskService.addTask(taskDto);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> showTask(@PathVariable Integer id ,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size){

        Page<TaskDto> taskDtoPage = taskService.showTask(id,size,page);
        return new ResponseEntity<>(taskDtoPage,HttpStatus.OK);

    }
    @PutMapping
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto){
        TaskDto res = taskService.updateTask(taskDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id ){
       taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted Successfully"+id,HttpStatus.OK);
    }

    @GetMapping("/completed/{id}")
    public ResponseEntity<?> getCompletedTask(@PathVariable Integer id,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size){
        Page <TaskDto> taskPage = taskService.getAllCompletedTask(id,size,page);
        if (taskPage.isEmpty()){
            return new ResponseEntity<>("No Record Found !",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(taskPage,HttpStatus.OK);

    }

    @GetMapping("/pending/{id}")
    public ResponseEntity<?> getPendingTask(@PathVariable Integer id,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size){
        Page <TaskDto> taskPage = taskService.getAllPendingTask(id, size,page);
        if (taskPage.isEmpty()){
            return new ResponseEntity<>("No Record Found !",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(taskPage,HttpStatus.OK);

    }

}
