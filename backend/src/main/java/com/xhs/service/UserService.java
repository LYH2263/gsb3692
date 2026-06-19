package com.xhs.service;

import com.xhs.entity.User;
import com.xhs.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> search(String keyword) {
        log.debug("搜索用户, keyword: {}", keyword);
        return userMapper.search(keyword);
    }

    public User login(String username, String password) {
        log.debug("用户尝试登录: {}", username);
        User user = userMapper.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            log.info("用户登录成功: {}", username);
            return user;
        }
        log.warn("用户登录失败 (用户名或密码错误): {}", username);
        return null;
    }

    public User register(String username, String password) {
        if (userMapper.findByUsername(username) != null) {
            log.warn("用户注册失败 (用户名已存在): {}", username);
            return null;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAvatar("https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg");
        userMapper.insert(user);
        log.info("新用户注册成功: {}", username);
        return user;
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public int update(User user) {
        return userMapper.update(user);
    }
}
