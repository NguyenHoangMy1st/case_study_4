package com.kin1st.coursemanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.validation.constraints .NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "course")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{course.title.notblank}")
    @Size(min = 5, max = 100, message = "{course.title.size}")
    private String title;

    @NotBlank(message = "{course.description.notblank}")
    @Size(min = 10, max = 10000, message = "{course.description.size}")
    private String description;
    @Column(length = 500)
    private String imageUrl;

    @ManyToOne
    private User teacher;
}