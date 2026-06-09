import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getUserInfo, getUserMenus } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))
  const menus = ref(JSON.parse(localStorage.getItem('menus') || '[]'))

  async function loginAction(loginForm) {
    const res = await login(loginForm)
    const data = res.data
    token.value = data.token
    userInfo.value = {
      userId: data.userId,
      username: data.username,
      nickname: data.nickname,
      avatar: data.avatar,
      roles: data.roles
    }
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    return data
  }

  async function fetchUserInfo() {
    const res = await getUserInfo()
    userInfo.value = {
      userId: res.data.userId,
      username: res.data.username,
      nickname: res.data.nickname,
      avatar: res.data.avatar,
      roles: res.data.roles
    }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  async function fetchMenus() {
    const res = await getUserMenus()
    menus.value = res.data
    localStorage.setItem('menus', JSON.stringify(menus.value))
  }

  function logoutAction() {
    token.value = ''
    userInfo.value = null
    menus.value = []
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('menus')
  }

  return {
    token,
    userInfo,
    menus,
    loginAction,
    fetchUserInfo,
    fetchMenus,
    logoutAction
  }
})