<template>
  <div class="note-detail" v-if="note">
    <van-nav-bar left-arrow @click-left="router.back()">
      <template #title>
        <div class="author-header">
          <van-image :src="note.avatar" round width="30" height="30" />
          <span>{{ note.username }}</span>
        </div>
      </template>
      <template #right>
        <van-button size="mini" round plain type="danger">关注</van-button>
        <van-icon name="share-o" size="20" style="margin-left: 10px" />
      </template>
    </van-nav-bar>

    <div class="content-scroll">
      <van-swipe :autoplay="3000" indicator-color="white" class="banner">
        <van-swipe-item v-for="(img, index) in note.images.split(',')" :key="index" class="swipe-item">
          <van-image :src="img" fit="cover" class="swipe-img" />
        </van-swipe-item>
      </van-swipe>

      <div class="info-section">
        <div class="title">{{ note.title }}</div>
        <div class="content">{{ note.content }}</div>
        <div class="time">{{ note.createdAt }}</div>
      </div>

      <div class="comment-section">
        <div class="count">共 {{ note.commentCount }} 条评论</div>
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <van-image :src="comment.avatar" round width="30" height="30" />
          <div class="details">
            <div class="user">{{ comment.username }}</div>
            <div class="text">{{ comment.content }}</div>
            <div class="meta">{{ comment.createdAt }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="bottom-bar">
      <div class="comment-input" @click="showCommentPopup = true">
        <van-icon name="edit" />
        <span>说点什么...</span>
      </div>
      <div class="actions">
        <div class="action-item" @click="handleLike">
          <van-icon :name="note.isLiked ? 'like' : 'like-o'" :color="note.isLiked ? '#ff2442' : '#333'" />
          <span>{{ note.likeCount }}</span>
        </div>
        <div class="action-item" @click="handleCollect">
          <van-icon :name="note.isCollected ? 'star' : 'star-o'" :color="note.isCollected ? '#ff2442' : '#333'" />
          <span>{{ note.collectCount }}</span>
        </div>
        <div class="action-item">
          <van-icon name="chat-o" />
          <span>{{ note.commentCount }}</span>
        </div>
      </div>
    </div>

    <van-action-sheet v-model:show="showCommentPopup" title="发表评论">
      <div class="comment-popup">
        <van-field
          v-model="newComment"
          rows="3"
          autosize
          type="textarea"
          placeholder="优质评论优先展示"
          class="comment-field"
        />
        <div class="submit-btn">
          <van-button type="primary" size="small" round color="#ff2442" @click="submitComment">发送</van-button>
        </div>
      </div>
    </van-action-sheet>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getNoteDetail, getComments, addComment, likeNote, collectNote } from '../api/note'
import { useUserStore } from '../store/user'
import { showSuccessToast, showFailToast } from 'vant'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const noteId = Number(route.params.id)
const note = ref<any>(null)
const comments = ref<any[]>([])
const showCommentPopup = ref(false)
const newComment = ref('')

onMounted(async () => {
  try {
    note.value = await getNoteDetail(noteId, { userId: userStore.userId })
    comments.value = await getComments(noteId) as any
  } catch (err) {
    showFailToast('获取详情失败')
  }
})

const handleLike = async () => {
  if (!userStore.userId) return router.push('/login')
  try {
    await likeNote({ userId: userStore.userId, noteId })
    note.value.isLiked = !note.value.isLiked
    note.value.likeCount += note.value.isLiked ? 1 : -1
  } catch (err) {}
}

const handleCollect = async () => {
  if (!userStore.userId) return router.push('/login')
  try {
    await collectNote({ userId: userStore.userId, noteId })
    note.value.isCollected = !note.value.isCollected
    note.value.collectCount += note.value.isCollected ? 1 : -1
  } catch (err) {}
}

const submitComment = async () => {
  if (!userStore.userId) return router.push('/login')
  if (!newComment.value) return
  try {
    await addComment({
      userId: userStore.userId,
      noteId,
      content: newComment.value
    })
    showSuccessToast('评论成功')
    newComment.value = ''
    showCommentPopup.value = false
    comments.value = await getComments(noteId) as any
    note.value.commentCount++
  } catch (err) {}
}
</script>

<style lang="scss" scoped>
.note-detail {
  background-color: white;
  min-height: 100vh;
  padding-bottom: 60px;
}

.author-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.banner {
  height: 400px;
  
  .swipe-item {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
  }

  .swipe-img {
    width: 100%;
    height: 100%;
  }
}

.info-section {
  padding: 16px;
  .title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 12px;
  }
  .content {
    font-size: 15px;
    line-height: 1.6;
    color: #333;
    white-space: pre-wrap;
  }
  .time {
    font-size: 12px;
    color: #999;
    margin-top: 16px;
  }
}

.comment-section {
  padding: 16px;
  border-top: 1px solid #f5f5f5;
  .count {
    font-size: 14px;
    font-weight: bold;
    margin-bottom: 16px;
  }
  .comment-item {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;
    .details {
      flex: 1;
      .user {
        font-size: 13px;
        color: #666;
        margin-bottom: 4px;
      }
      .text {
        font-size: 14px;
        color: #333;
        margin-bottom: 4px;
      }
      .meta {
        font-size: 11px;
        color: #999;
      }
    }
  }
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background-color: white;
  display: flex;
  align-items: center;
  padding: 0 16px;
  gap: 16px;
  border-top: 1px solid #f5f5f5;

  .comment-input {
    flex: 1;
    height: 36px;
    background-color: #f5f5f5;
    border-radius: 18px;
    display: flex;
    align-items: center;
    padding: 0 12px;
    gap: 8px;
    color: #999;
    font-size: 14px;
  }

  .actions {
    display: flex;
    gap: 16px;
    .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 2px;
      font-size: 11px;
      color: #333;
      .van-icon {
        font-size: 20px;
      }
    }
  }
}

.comment-popup {
  padding: 16px;
  .comment-field {
    background-color: #f5f5f5;
    border-radius: 8px;
  }
  .submit-btn {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
  }
}
</style>
