import { createRouter, createWebHistory } from 'vue-router'
import Startseite from '@/components/Startseite.vue'
import Login from '@/components/Login.vue'

export const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/start', name: 'Startseite', component: Startseite, meta: { requiresAuth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

function isLoggedIn() {
  return !!sessionStorage.getItem('token')
}

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !isLoggedIn()) return '/login'
  if (to.path === '/login' && isLoggedIn()) return '/start'
})

export default router
