<template>
  <div class="post-page">
    <van-nav-bar title="发布笔记" left-text="取消" @click-left="router.back()">
      <template #right>
        <van-button type="primary" size="small" round color="#ff2442" @click="handlePost" :loading="posting">发布</van-button>
      </template>
    </van-nav-bar>

    <div class="content">
      <div class="upload-section">
        <van-uploader v-model="fileList" multiple :max-count="9" preview-size="100px" />
      </div>
      
      <van-field
        v-model="title"
        placeholder="填写标题会有更多赞哦~"
        class="title-field"
        maxlength="20"
      />
      
      <van-field
        v-model="content"
        type="textarea"
        placeholder="添加正文"
        rows="4"
        autosize
        class="content-field"
      />
      
      <van-cell-group inset>
        <van-cell title="分类" is-link :value="category" @click="showCategory = true" />
      </van-cell-group>
    </div>

    <van-action-sheet v-model:show="showCategory" :actions="categories" @select="onSelectCategory" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { uploadFile } from '../api/user'
import { saveNote } from '../api/note'
import { showSuccessToast, showFailToast } from 'vant'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const title = ref('')
const content = ref('')
const category = ref('生活')
const showCategory = ref(false)
const posting = ref(false)
const fileList = ref<any[]>([])

const categories = [
  { name: '时尚' },
  { name: '美食' },
  { name: '旅行' },
  { name: '生活' },
  { name: '科技' },
  { name: '运动' }
]

const onSelectCategory = (item: any) => {
  category.value = item.name
  showCategory.value = false
}

const handlePost = async () => {
  if (!title.value) return showFailToast('请填写标题')
  if (fileList.value.length === 0) return showFailToast('请上传图片')
  
  posting.value = true
  try {
    const imageUrls: string[] = []
    for (const file of fileList.value) {
      const formData = new FormData()
      formData.append('file', file.file)
      const url = await uploadFile(formData) as any
      imageUrls.push(url)
    }

    await saveNote({
      userId: userStore.userId,
      title: title.value,
      content: content.value,
      images: imageUrls.join(','),
      category: category.value
    })

    showSuccessToast('发布成功')
    router.push('/')
  } catch (err: any) {
    showFailToast(err.message || '发布失败')
  } finally {
    posting.value = false
  }
}
</script>

<style lang="scss" scoped>
.post-page {
  background-color: white;
  min-height: 100vh;
}

.upload-section {
  padding: 16px;
  border-bottom: 1px solid #f5f5f5;
}

.title-field {
  font-size: 16px;
  font-weight: bold;
}

.content-field {
  font-size: 14px;
}
</style>
