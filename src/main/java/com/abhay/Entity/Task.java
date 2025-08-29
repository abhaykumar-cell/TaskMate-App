package com.abhay.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime due_date=LocalDateTime.now().plusHours(24);
    private Boolean completed = false;
    @Enumerated(EnumType.STRING)

    private prior priority;
    private Boolean soft_deleted=false;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user;
    private  LocalDateTime created_at= LocalDateTime.now();
    private LocalDateTime uploaded_at=LocalDateTime.now();

    public Task(String title, String description, prior priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
}
