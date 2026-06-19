package com.xhs.entity;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
public class User {
    private Long id;
    private String username;
    
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;
    
    private String avatar;
    private String profile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
}
