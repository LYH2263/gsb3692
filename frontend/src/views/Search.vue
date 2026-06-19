<template>
  <div class="search-page">
    <div class="search-header">
      <van-search
        v-model="keyword"
        placeholder="大家都在搜“春季穿搭”"
        show-action
        shape="round"
        @search="onSearch"
      >
        <template #action>
          <div @click="onSearch">搜索</div>
        </template>
      </van-search>
    </div>

    <div v-if="!searched" class="hot-search">
      <div class="title">热门搜索</div>
      <div class="tags">
        <van-button v-for="tag in hotTags" :key="tag" size="small" round @click="onTagClick(tag)">
          {{ tag }}
        </van-button>
      </div>
    </div>

    <div v-else class="search-results-container">
      <van-tabs v-model:active="activeTab" sticky color="#ff2442" title-active-color="#333">
        <van-tab title="笔记" name="notes">
          <div class="search-results">
            <van-list
              v-model:loading="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="onLoad"
            >
              <div v-if="notes.length" class="waterfall">
                <div class="column">
                  <div v-for="note in leftNotes" :key="note.id" class="note-card" @click="goDetail(note.id)">
                    <van-image :src="getFirstImage(note.images)" radius="8" fit="cover" class="cover-img" />
                    <div class="info">
                      <div class="title">{{ note.title }}</div>
                      <div class="footer">
                        <div class="author">
                          <van-image :src="note.avatar" round width="18" height="18" />
                          <span class="username">{{ note.username }}</span>
                        </div>
                        <div class="likes">
                          <van-icon name="like-o" />
                          <span>{{ note.likeCount }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="column">
                  <div v-for="note in rightNotes" :key="note.id" class="note-card" @click="goDetail(note.id)">
                    <van-image :src="getFirstImage(note.images)" radius="8" fit="cover" class="cover-img" />
                    <div class="info">
                      <div class="title">{{ note.title }}</div>
                      <div class="footer">
                        <div class="author">
                          <van-image :src="note.avatar" round width="18" height="18" />
                          <span class="username">{{ note.username }}</span>
                        </div>
                        <div class="likes">
                          <van-icon name="like-o" />
                          <span>{{ note.likeCount }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <van-empty v-else description="暂无相关笔记" />
            </van-list>
          </div>
        </van-tab>
        <van-tab title="用户" name="users">
          <div class="user-results">
            <div v-if="users.length" class="user-list">
              <div v-for="user in users" :key="user.id" class="user-item">
                <van-image :src="user.avatar" round width="50" height="50" />
                <div class="user-info">
                  <div class="username">{{ user.username }}</div>
                  <div class="profile">{{ user.profile || '暂无简介' }}</div>
                </div>
              </div>
            </div>
            <van-empty v-else description="暂无相关用户" />
          </div>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getNoteList } from '../api/note'
import { searchUsers } from '../api/user'

const router = useRouter()
const keyword = ref('')
const hotTags = ['穿搭', '美食', '上海探店', '周末去哪儿', '好物分享', '健身', '装修']
const notes = ref<any[]>([])
const users = ref<any[]>([])
const loading = ref(false)
const finished = ref(false)
const searched = ref(false)
const activeTab = ref('notes')

const leftNotes = computed(() => notes.value.filter((_, i) => i % 2 === 0))
const rightNotes = computed(() => notes.value.filter((_, i) => i % 2 !== 0))

const onSearch = async () => {
  if (!keyword.value) return
  searched.value = true
  notes.value = []
  users.value = []
  loading.value = true
  finished.value = false
  onLoad()
  fetchUsers()
}

const onLoad = async () => {
  try {
    const res = await getNoteList({ keyword: keyword.value })
    notes.value = res as any
    loading.value = false
    finished.value = true
  } catch (err) {
    loading.value = false
    finished.value = true
  }
}

const fetchUsers = async () => {
  try {
    const res = await searchUsers({ keyword: keyword.value })
    users.value = res as any
  } catch (err) {}
}

const onTagClick = (tag: string) => {
  keyword.value = tag
  onSearch()
}

const getFirstImage = (images: string) => {
  if (!images) return ''
  // 选取附件列表中的第一张图作为封面
  const imgList = images.split(',').map(img => img.trim()).filter(img => img !== '')
  return imgList.length > 0 ? imgList[0] : ''
}

const goDetail = (id: number) => {
  router.push(`/note/${id}`)
}
</script>

<style lang="scss" scoped>
.search-page {
  background-color: white;
  min-height: 100vh;
}

.search-header {
  padding-bottom: 10px;
}

.hot-search {
  padding: 20px;
  .title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 16px;
  }
  .tags {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
}

.search-results-container {
  flex: 1;
}

.user-results {
  background-color: white;
  padding: 12px;
}

.user-list {
  display: flex;
  flex-direction: column;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;

  &:last-child {
    border-bottom: none;
  }

  .user-info {
    flex: 1;
    overflow: hidden;

    .username {
      font-size: 15px;
      font-weight: 500;
      color: #333;
      margin-bottom: 4px;
    }

    .profile {
      font-size: 12px;
      color: #999;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
}

.search-results {
  background-color: #f5f5f5;
  padding: 8px;
}

.waterfall {
  display: flex;
  gap: 8px;

  .column {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
}

.note-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 8px;

  .cover-img {
    width: 100%;
    aspect-ratio: 3 / 4;
    background-color: #f0f0f0;
  }

  .info {
    padding: 8px;

    .title {
      font-size: 14px;
      font-weight: 500;
      color: #333;
      margin-bottom: 8px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 11px;
      color: #999;

      .author {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }
}
</style>
