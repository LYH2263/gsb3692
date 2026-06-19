package com.xhs.controller;

import com.xhs.common.JwtUtils;
import com.xhs.common.Result;
import com.xhs.dto.UserLoginDTO;
import com.xhs.entity.User;
import com.xhs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/search")
    public Result<List<User>> search(@RequestParam @Size(max = 20, message = "关键词长度不能超过20") String keyword) {
        log.info("用户搜索，关键词: {}", keyword);
        return Result.success(userService.search(keyword));
    }

    @Value("${file.upload-path}")
    private String uploadPath;

    @PostMapping("/login")
    public Result<User> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if (user != null) {
            String token = jwtUtils.createToken(user.getId());
            user.setToken(token);
            return Result.success(user);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody UserLoginDTO registerDTO) {
        log.info("用户注册: {}", registerDTO.getUsername());
        User user = userService.register(registerDTO.getUsername(), registerDTO.getPassword());
        if (user != null) {
            String token = jwtUtils.createToken(user.getId());
            user.setToken(token);
            return Result.success(user);
        }
        return Result.error("用户名已存在");
    }

    @GetMapping("/{id}")
    public Result<User> getUserInfo(@PathVariable Long id) {
        log.info("获取用户信息，ID: {}", id);
        User user = userService.findById(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    @PostMapping("/update")
    public Result<User> update(@RequestBody User user) {
        log.info("更新用户信息，ID: {}", user.getId());
        userService.update(user);
        return Result.success(userService.findById(user.getId()));
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }
        
        // 文件安全校验
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/octet-stream"))) {
            return Result.error("只允许上传图片文件");
        }
        
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            return Result.error("文件大小超过限制 (5MB)");
        }

        // 安全文件名生成
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 只允许常见图片后缀
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".webp", ".gif");
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            return Result.error("不支持的文件格式: " + extension);
        }

        String fileName = UUID.randomUUID().toString() + extension;
        File dest = new File(uploadPath + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            log.info("文件上传成功: {}", fileName);
            return Result.success("/uploads/" + fileName);
        } catch (IOException e) {
            log.error("上传失败: ", e);
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
