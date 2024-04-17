import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import results from '../views/results.vue'
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/results',
    name: 'results',
    component: results
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
