package edu.aston.userservice.dto;

import edu.aston.userservice.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;

    public UserResponseDTO(final int id, final String name, final String email, final int age, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public UserResponseDTO(final User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.createdAt = user.getCreatedAt();
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getAge() {
        return this.age;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + this.id +
                ", name=" + this.name +
                ", email=" + this.email +
                ", age=" + this.age +
                ", createdAt=" + this.createdAt +
                "}";
    }
}
