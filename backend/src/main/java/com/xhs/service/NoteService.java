package com.xhs.service;

import com.xhs.entity.Comment;
import com.xhs.entity.Note;
import com.xhs.mapper.NoteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public List<Note> list(String category, String keyword, Long userId, Integer page, Integer size) {
        Integer offset = (page != null && size != null) ? (page - 1) * size : null;
        log.debug("查询笔记列表: category={}, keyword={}, offset={}, size={}", category, keyword, offset, size);
        List<Note> notes = noteMapper.findAll(category, keyword, offset, size);
        fillInteractions(notes, userId);
        return notes;
    }

    public Note getById(Long id, Long userId) {
        log.debug("查询笔记详情: id={}, userId={}", id, userId);
        Note note = noteMapper.findById(id);
        if (note != null && userId != null) {
            note.setIsLiked(noteMapper.isLiked(userId, note.getId()) > 0);
            note.setIsCollected(noteMapper.isCollected(userId, note.getId()) > 0);
        }
        return note;
    }

    public void save(Note note) {
        log.info("保存新笔记: title={}, userId={}", note.getTitle(), note.getUserId());
        noteMapper.insert(note);
    }

    @Transactional
    public void like(Long userId, Long noteId) {
        if (noteMapper.isLiked(userId, noteId) == 0) {
            log.info("用户 {} 点赞笔记 {}", userId, noteId);
            noteMapper.like(userId, noteId);
        } else {
            log.info("用户 {} 取消点赞笔记 {}", userId, noteId);
            noteMapper.unlike(userId, noteId);
        }
    }

    @Transactional
    public void collect(Long userId, Long noteId) {
        if (noteMapper.isCollected(userId, noteId) == 0) {
            log.info("用户 {} 收藏笔记 {}", userId, noteId);
            noteMapper.collect(userId, noteId);
        } else {
            log.info("用户 {} 取消收藏笔记 {}", userId, noteId);
            noteMapper.uncollect(userId, noteId);
        }
    }

    public List<Comment> getComments(Long noteId) {
        return noteMapper.findCommentsByNoteId(noteId);
    }

    @Transactional
    public void addComment(Comment comment) {
        log.info("用户 {} 在笔记 {} 下发表评论", comment.getUserId(), comment.getNoteId());
        noteMapper.insertComment(comment);
    }

    public List<Note> getPublishedByUser(Long userId) {
        List<Note> notes = noteMapper.findPublishedByUser(userId);
        fillInteractions(notes, userId);
        return notes;
    }

    public List<Note> getLikedByUser(Long userId) {
        List<Note> notes = noteMapper.findLikedByUser(userId);
        fillInteractions(notes, userId);
        return notes;
    }

    public List<Note> getCollectedByUser(Long userId) {
        List<Note> notes = noteMapper.findCollectedByUser(userId);
        fillInteractions(notes, userId);
        return notes;
    }

    private void fillInteractions(List<Note> notes, Long userId) {
        if (userId != null) {
            for (Note note : notes) {
                note.setIsLiked(noteMapper.isLiked(userId, note.getId()) > 0);
                note.setIsCollected(noteMapper.isCollected(userId, note.getId()) > 0);
            }
        }
    }
}
