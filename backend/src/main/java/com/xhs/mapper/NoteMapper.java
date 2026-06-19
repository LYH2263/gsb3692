package com.xhs.mapper;

import com.xhs.entity.Comment;
import com.xhs.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoteMapper {
    List<Note> findAll(@Param("category") String category, @Param("keyword") String keyword, @Param("offset") Integer offset, @Param("limit") Integer limit);
    Note findById(@Param("id") Long id);
    int insert(Note note);
    int update(Note note);
    
    // 互动功能
    int like(@Param("userId") Long userId, @Param("note_id") Long noteId);
    int unlike(@Param("userId") Long userId, @Param("note_id") Long noteId);
    int isLiked(@Param("userId") Long userId, @Param("note_id") Long noteId);
    
    int collect(@Param("userId") Long userId, @Param("note_id") Long noteId);
    int uncollect(@Param("userId") Long userId, @Param("note_id") Long noteId);
    int isCollected(@Param("userId") Long userId, @Param("note_id") Long noteId);
    
    // 评论功能
    List<Comment> findCommentsByNoteId(@Param("noteId") Long noteId);
    int insertComment(Comment comment);
    
    // 用户相关查询
    List<Note> findPublishedByUser(@Param("userId") Long userId);
    List<Note> findLikedByUser(@Param("userId") Long userId);
    List<Note> findCollectedByUser(@Param("userId") Long userId);
}
