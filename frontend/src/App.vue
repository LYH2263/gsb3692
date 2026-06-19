<template>
  <div class="app-container">
    <router-view v-slot="{ Component }">
      <keep-alive>
        <component :is="Component" v-if="$route.meta.keepAlive" />
      </keep-alive>
      <component :is="Component" v-if="!$route.meta.keepAlive" />
    </router-view>

    <van-tabbar v-if="showTabbar" v-model="active" active-color="#ff2442" inactive-color="#999" route>
      <van-tabbar-item to="/" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/search" icon="search">搜索</van-tabbar-item>
      <van-tabbar-item to="/post" icon="plus" class="post-btn"></van-tabbar-item>
      <van-tabbar-item to="/user" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const active = ref(0)

const showTabbar = computed(() => {
  const hiddenRoutes = ['/login', '/post', '/profile', '/note']
  return !hiddenRoutes.some(path => route.path.startsWith(path))
})
</script>

<style lang="scss">
body {
  margin: 0;
  padding: 0;
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

.app-container {
  padding-bottom: 50px;
}

.post-btn {
  .van-icon {
    background-color: #ff2442;
    color: white;
    padding: 4px;
    border-radius: 8px;
    font-size: 20px;
  }
}
</style>
