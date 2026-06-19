package com.xhs.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "笔记ID不能为空")
    private Long noteId;

    @NotBlank(message = "评论内容不能为空")
    private String content;
    private LocalDateTime createdAt;
    
    // 扩展字段
    private String username;
    private String avatar;
}
