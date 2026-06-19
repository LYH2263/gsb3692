<template>
  <div class="profile-page">
    <van-nav-bar title="编辑资料" left-arrow @click-left="router.back()" />
    
    <div class="avatar-section">
      <van-uploader :after-read="afterRead">
        <van-image :src="userInfo.avatar" round width="80" height="80" />
        <div class="tip">点击修改头像</div>
      </van-uploader>
    </div>

    <van-cell-group inset>
      <van-field v-model="userInfo.username" label="名字" placeholder="填写名字" input-align="right" />
      <van-field v-model="userInfo.profile" label="简介" type="textarea" placeholder="填写简介" rows="2" autosize input-align="right" />
    </van-cell-group>

    <div style="margin: 32px 16px;">
      <van-button round block type="primary" color="#ff2442" @click="handleSave" :loading="saving">保存</van-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { uploadFile, updateUser } from '../api/user'
import { showSuccessToast, showFailToast } from 'vant'

const router = useRouter()
const userStore = useUserStore()
const userInfo = ref<any>({ ...userStore.userInfo })
const saving = ref(false)

const afterRead = async (file: any) => {
  const formData = new FormData()
  formData.append('file', file.file)
  try {
    const url = await uploadFile(formData)
    userInfo.value.avatar = url
    showSuccessToast('头像上传成功')
  } catch (err) {
    showFailToast('头像上传失败')
  }
}

const handleSave = async () => {
  saving.value = true
  try {
    const res = await updateUser(userInfo.value)
    userStore.setUserInfo(res)
    showSuccessToast('保存成功')
    router.back()
  } catch (err) {
    showFailToast('保存失败')
  } finally {
    saving.value = false
  }
}
</script>

<style lang="scss" scoped>
.profile-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.avatar-section {
  padding: 40px 0;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  background-color: white;
  margin-bottom: 12px;

  .tip {
    font-size: 12px;
    color: #999;
    margin-top: 8px;
  }
}
</style>
