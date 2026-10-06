import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import AnzeigeListeView from '@/views/AnzeigeListeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/anzeige' },
    { path: '/login', name: 'LoginView', component: LoginView },
    { path: '/anzeige', name: 'AnzeigeListeView', component: AnzeigeListeView },
  ],
})

export default router