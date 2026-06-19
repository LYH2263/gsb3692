import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    userId: localStorage.getItem('userId') || null,
    token: localStorage.getItem('token') || null,
  }),
  actions: {
    setUserInfo(user: any) {
      this.userInfo = user;
      this.userId = user.id;
      this.token = user.token;
      localStorage.setItem('userInfo', JSON.stringify(user));
      localStorage.setItem('userId', user.id);
      if (user.token) {
        localStorage.setItem('token', user.token);
      }
    },
    logout() {
      this.userInfo = {};
      this.userId = null;
      this.token = null;
      localStorage.removeItem('userInfo');
      localStorage.removeItem('userId');
      localStorage.removeItem('token');
    },
  },
});
