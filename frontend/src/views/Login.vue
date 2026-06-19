<template>
  <div class="login-page">
    <van-nav-bar :title="isLogin ? '登录' : '注册'" left-arrow @click-left="router.back()" />
    
    <div class="logo">
      <div class="red-box">小红书</div>
    </div>

    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="username"
          name="username"
          label="用户名"
          placeholder="请输入用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
          v-model="password"
          type="password"
          name="password"
          label="密码"
          placeholder="请输入密码"
          :rules="[{ required: true, message: '请填写密码' }]"
        />
        <van-field
          v-if="!isLogin"
          v-model="confirmPassword"
          type="password"
          name="confirmPassword"
          label="确认密码"
          placeholder="请再次输入密码"
          :rules="[
            { required: true, message: '请确认密码' },
            { validator: validateConfirmPassword, message: '两次输入的密码不一致' }
          ]"
        />
      </van-cell-group>
      
      <div style="margin: 32px 16px;">
        <van-button round block type="primary" native-type="submit" color="#ff2442">
          {{ isLogin ? '登录' : '注册' }}
        </van-button>
        
        <div class="toggle-text" @click="isLogin = !isLogin">
          {{ isLogin ? '还没有账号？去注册' : '已有账号？去登录' }}
        </div>
      </div>
    </van-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login, register } from '../api/user'
import { useUserStore } from '../store/user'
import { showSuccessToast, showFailToast } from 'vant'

const router = useRouter()
const userStore = useUserStore()
const isLogin = ref(true)
const username = ref('')
const password = ref('')
const confirmPassword = ref('')

const validateConfirmPassword = (val: string) => {
  return val === password.value
}

const onSubmit = async () => {
  try {
    if (isLogin.value) {
      const res = await login({ username: username.value, password: password.value })
      userStore.setUserInfo(res)
      showSuccessToast('登录成功')
      router.push('/')
    } else {
      await register({ username: username.value, password: password.value })
      showSuccessToast('注册成功，请登录')
      isLogin.value = true
    }
  } catch (err: any) {
    showFailToast(err.message || '操作失败')
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  background-color: white;
  min-height: 100vh;
}

.logo {
  display: flex;
  justify-content: center;
  margin: 60px 0;

  .red-box {
    background-color: #ff2442;
    color: white;
    font-size: 24px;
    font-weight: bold;
    padding: 10px 20px;
    border-radius: 12px;
  }
}

.toggle-text {
  text-align: center;
  margin-top: 16px;
  color: #999;
  font-size: 14px;
}
</style>
