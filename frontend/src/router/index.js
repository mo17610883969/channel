import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/system/user',
    children: [
      {
        path: 'system/user',
        name: 'UserManage',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'system/menu',
        name: 'MenuManage',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理' }
      },
      {
        path: 'channel/list',
        name: 'ChannelList',
        component: () => import('@/views/channel/channelList.vue'),
        meta: { title: '渠道信息列表' }
      },
      {
        path: 'margin/list',
        name: 'MarginAccountList',
        component: () => import('@/views/marginAccount/index.vue'),
        meta: { title: '保证金管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const whiteList = ['/login']

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 渠道保证金管理系统` : '渠道保证金管理系统'
  const token = localStorage.getItem('token')
  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router