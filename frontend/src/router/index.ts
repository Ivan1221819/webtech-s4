import { createRouter, createWebHistory } from 'vue-router'
import Startseite from '../components/Startseite.vue'

const routes = [
  {
    path: '/',          
    name: 'Startseite',
    component: Startseite
  },
  {
    path: '/merkliste',
    name: 'Merkliste',
    component: () => import('../components/Merkliste.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
