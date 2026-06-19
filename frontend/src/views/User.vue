<template>
  <div class="user-page">
    <div class="header">
      <div class="top-bar">
        <van-icon name="setting-o" @click="router.push('/profile')" />
        <van-icon name="share-o" />
      </div>
      
      <div class="user-info">
        <van-image :src="userInfo.avatar" round width="80" height="80" />
        <div class="details">
          <div class="name">{{ userInfo.username }}</div>
          <div class="id">小红书号：{{ userInfo.id }}</div>
        </div>
      </div>
      
      <div class="bio">{{ userInfo.profile || '还没有简介哦~' }}</div>
      
      <div class="stats">
        <div class="item">
          <div class="num">120</div>
          <div class="label">关注</div>
        </div>
        <div class="item">
          <div class="num">350</div>
          <div class="label">粉丝</div>
        </div>
        <div class="item">
          <div class="num">1.2w</div>
          <div class="label">获赞与收藏</div>
        </div>
      </div>
    </div>

    <div class="content">
      <van-tabs v-model:active="activeTab" color="#ff2442" title-active-color="#333" @change="onTabChange">
        <van-tab title="笔记" name="notes">
          <div v-if="publishedNotes.length" class="note-list">
            <div v-for="note in publishedNotes" :key="note.id" class="note-list-item" @click="goDetail(note.id)">
              <div class="note-info">
                <div class="title">{{ note.title }}</div>
                <div class="meta">{{ note.createdAt }} · {{ note.likeCount }} 赞</div>
              </div>
              <van-icon name="arrow" color="#ccc" />
            </div>
          </div>
          <div v-else class="empty">暂无笔记</div>
        </van-tab>
        <van-tab title="收藏" name="collects">
          <div v-if="collectedNotes.length" class="note-list">
            <div v-for="note in collectedNotes" :key="note.id" class="note-list-item" @click="goDetail(note.id)">
              <div class="note-info">
                <div class="title">{{ note.title }}</div>
                <div class="meta">by {{ note.username }} · {{ note.likeCount }} 赞</div>
              </div>
              <van-icon name="arrow" color="#ccc" />
            </div>
          </div>
          <div v-else class="empty">暂无收藏</div>
        </van-tab>
        <van-tab title="赞过" name="likes">
          <div v-if="likedNotes.length" class="note-list">
            <div v-for="note in likedNotes" :key="note.id" class="note-list-item" @click="goDetail(note.id)">
              <div class="note-info">
                <div class="title">{{ note.title }}</div>
                <div class="meta">by {{ note.username }} · {{ note.likeCount }} 赞</div>
              </div>
              <van-icon name="arrow" color="#ccc" />
            </div>
          </div>
          <div v-else class="empty">暂无点赞</div>
        </van-tab>
      </van-tabs>
    </div>

    <div class="logout-btn">
      <van-button block round @click="handleLogout">退出登录</van-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { getUserInfo } from '../api/user'
import { getUserPublishedNotes, getUserLikedNotes, getUserCollectedNotes } from '../api/note'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('notes')
const userInfo = ref<any>(userStore.userInfo)

const publishedNotes = ref<any[]>([])
const likedNotes = ref<any[]>([])
const collectedNotes = ref<any[]>([])

onMounted(async () => {
  if (userStore.userId) {
    const res = await getUserInfo(Number(userStore.userId))
    userInfo.value = res
    userStore.setUserInfo(res)
    fetchData()
  }
})

const fetchData = async () => {
  if (!userStore.userId) return
  const id = Number(userStore.userId)
  if (activeTab.value === 'notes') {
    publishedNotes.value = await getUserPublishedNotes(id) as any
  } else if (activeTab.value === 'collects') {
    collectedNotes.value = await getUserCollectedNotes(id) as any
  } else if (activeTab.value === 'likes') {
    likedNotes.value = await getUserLikedNotes(id) as any
  }
}

const onTabChange = () => {
  fetchData()
}

const goDetail = (id: number) => {
  router.push(`/note/${id}`)
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.user-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  background-color: white;
  padding: 20px;
  background: linear-gradient(to bottom, #fff0f0, #ffffff);

  .top-bar {
    display: flex;
    justify-content: flex-end;
    gap: 20px;
    font-size: 24px;
    margin-bottom: 20px;
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 16px;

    .name {
      font-size: 20px;
      font-weight: bold;
      margin-bottom: 4px;
    }

    .id {
      font-size: 12px;
      color: #999;
    }
  }

  .bio {
    font-size: 14px;
    color: #333;
    margin-bottom: 20px;
  }

  .stats {
    display: flex;
    gap: 30px;

    .item {
      text-align: center;
      .num {
        font-weight: bold;
        font-size: 16px;
      }
      .label {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

.content {
  margin-top: 10px;
  background-color: white;
}

.empty {
  padding: 100px 0;
  text-align: center;
  color: #999;
  font-size: 14px;
}

.note-list {
  background-color: white;
  .note-list-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #f5f5f5;
    
    .note-info {
      flex: 1;
      .title {
        font-size: 15px;
        color: #333;
        margin-bottom: 6px;
        font-weight: 500;
      }
      .meta {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

.logout-btn {
  padding: 20px;
  margin-bottom: 20px;
}
</style>
