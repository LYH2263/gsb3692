<template>
  <div class="home">
    <van-sticky>
      <div class="header">
        <van-tabs v-model:active="activeCategory" @change="onCategoryChange" color="#ff2442" title-active-color="#333">
          <van-tab v-for="item in categories" :key="item" :title="item" :name="item" />
        </van-tabs>
      </div>
    </van-sticky>

    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list
        v-model:loading="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <div class="waterfall">
          <div class="column">
            <div v-for="note in leftNotes" :key="note.id" class="note-card" @click="goDetail(note.id)">
              <van-image :src="getFirstImage(note.images)" class="cover-img" fit="cover" />
              <div class="info">
                <div class="title">{{ note.title }}</div>
                <div class="footer">
                  <div class="author">
                    <van-image :src="note.avatar" round width="18" height="18" />
                    <span class="username">{{ note.username }}</span>
                  </div>
                  <div class="likes">
                    <van-icon name="like-o" />
                    <span class="count">{{ note.likeCount }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="column">
            <div v-for="note in rightNotes" :key="note.id" class="note-card" @click="goDetail(note.id)">
              <van-image :src="getFirstImage(note.images)" class="cover-img" fit="cover" />
              <div class="info">
                <div class="title">{{ note.title }}</div>
                <div class="footer">
                  <div class="author">
                    <van-image :src="note.avatar" round width="18" height="18" />
                    <span class="username">{{ note.username }}</span>
                  </div>
                  <div class="likes">
                    <van-icon name="like-o" />
                    <span class="count">{{ note.likeCount }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getNoteList } from '../api/note'

const router = useRouter()
const activeCategory = ref('全部')
const categories = ['全部', '时尚', '美食', '旅行', '生活', '科技', '运动']

const notes = ref<any[]>([])
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)
const page = ref(1)
const size = ref(6)

const leftNotes = computed(() => notes.value.filter((_, i) => i % 2 === 0))
const rightNotes = computed(() => notes.value.filter((_, i) => i % 2 !== 0))

const onLoad = async () => {
  try {
    const res = await getNoteList({ 
      category: activeCategory.value,
      page: page.value,
      size: size.value
    })
    
    const data = res as any
    
    if (refreshing.value) {
      notes.value = []
      refreshing.value = false
    }
    
    notes.value = [...notes.value, ...data]
    loading.value = false
    
    if (data.length < size.value) {
      finished.value = true
    } else {
      page.value++
    }
  } catch (err) {
    loading.value = false
    finished.value = true
  }
}

const onRefresh = () => {
  finished.value = false
  loading.value = true
  page.value = 1
  onLoad()
}

const onCategoryChange = () => {
  notes.value = []
  onRefresh()
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
.home {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  background-color: white;
  padding: 0 10px;
}

.waterfall {
  display: flex;
  padding: 8px;
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
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;

  .cover-img {
    width: 100%;
    aspect-ratio: 3 / 4;
    background-color: #f0f0f0;
  }

  .info {
    padding: 10px;

    .title {
      font-size: 13px;
      font-weight: 500;
      color: #333;
      margin-bottom: 8px;
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .author {
        display: flex;
        align-items: center;
        gap: 4px;
        flex: 1;
        overflow: hidden;

        .username {
          font-size: 11px;
          color: #666;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      .likes {
        display: flex;
        align-items: center;
        gap: 2px;
        color: #999;
        font-size: 11px;

        .van-icon {
          font-size: 13px;
        }
      }
    }
  }
}
</style>
