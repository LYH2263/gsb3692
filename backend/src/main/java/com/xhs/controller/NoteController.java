package com.xhs.controller;

import com.xhs.common.Result;
import com.xhs.entity.Comment;
import com.xhs.entity.Note;
import com.xhs.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/list")
    public Result<List<Note>> list(@RequestParam(required = false) String category,
                                 @RequestParam(required = false) @Size(max = 20, message = "关键词长度不能超过20") String keyword,
                                 @RequestParam(required = false) Long userId,
                                 @RequestParam(defaultValue = "1") @Min(value = 1, message = "页码最小为1") Integer page,
                                 @RequestParam(defaultValue = "10") @Min(value = 1, message = "每页数量最小为1") @Max(value = 100, message = "每页数量最大为100") Integer size) {
        log.info("获取笔记列表, category: {}, keyword: {}, userId: {}, page: {}, size: {}", category, keyword, userId, page, size);
        return Result.success(noteService.list(category, keyword, userId, page, size));
    }

    @GetMapping("/{id}")
    public Result<Note> getById(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        log.info("获取笔记详情, id: {}, userId: {}", id, userId);
        return Result.success(noteService.getById(id, userId));
    }

    @PostMapping("/save")
    public Result<Void> save(@Valid @RequestBody Note note) {
        log.info("保存笔记, title: {}, userId: {}", note.getTitle(), note.getUserId());
        noteService.save(note);
        return Result.success();
    }

    @PostMapping("/like")
    public Result<Void> like(@RequestParam Long userId, @RequestParam Long noteId) {
        log.info("点赞笔记, userId: {}, noteId: {}", userId, noteId);
        noteService.like(userId, noteId);
        return Result.success();
    }

    @PostMapping("/collect")
    public Result<Void> collect(@RequestParam Long userId, @RequestParam Long noteId) {
        log.info("收藏笔记, userId: {}, noteId: {}", userId, noteId);
        noteService.collect(userId, noteId);
        return Result.success();
    }

    @GetMapping("/comments/{noteId}")
    public Result<List<Comment>> getComments(@PathVariable Long noteId) {
        log.info("获取笔记评论, noteId: {}", noteId);
        return Result.success(noteService.getComments(noteId));
    }

    @PostMapping("/comment")
    public Result<Void> comment(@Valid @RequestBody Comment comment) {
        log.info("发表评论, noteId: {}, userId: {}", comment.getNoteId(), comment.getUserId());
        noteService.addComment(comment);
        return Result.success();
    }

    @GetMapping("/user/published")
    public Result<List<Note>> getPublishedByUser(@RequestParam Long userId) {
        log.info("获取用户发布的笔记, userId: {}", userId);
        return Result.success(noteService.getPublishedByUser(userId));
    }

    @GetMapping("/user/liked")
    public Result<List<Note>> getLikedByUser(@RequestParam Long userId) {
        log.info("获取用户点赞的笔记, userId: {}", userId);
        return Result.success(noteService.getLikedByUser(userId));
    }

    @GetMapping("/user/collected")
    public Result<List<Note>> getCollectedByUser(@RequestParam Long userId) {
        log.info("获取用户收藏的笔记, userId: {}", userId);
        return Result.success(noteService.getCollectedByUser(userId));
    }
}
