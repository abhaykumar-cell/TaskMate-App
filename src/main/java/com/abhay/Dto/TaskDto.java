package com.abhay.Dto;

import com.abhay.Entity.prior;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto{
    private Integer id;
    @NotNull(message = "Title must import ")
    private String title;
    private String description;
    private Boolean completed;
    @Enumerated(EnumType.STRING)
    private prior priority;
    private Integer userid;

}
