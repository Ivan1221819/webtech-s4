import { createRouter, createWebHistory } from 'vue-router'
import Startseite from '../components/Startseite.vue'

const routes = [
  {
    path: '/',
    name: 'Startseite',
    component: Startseite
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
