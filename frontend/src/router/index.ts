import { createRouter, createWebHistory } from 'vue-router'
import Startseite from '@/components/Startseite.vue'
import Login from '@/components/Login.vue'

const routes = [
  { path: '/', name: 'Startseite', component: Startseite },
  { path: '/login', name: 'Login', component: Login },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
