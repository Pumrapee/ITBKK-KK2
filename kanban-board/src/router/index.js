import { createRouter, createWebHistory } from "vue-router"
import HomeView from "@/views/HomeView.vue"

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/task",
      name: "task",
      component: HomeView,
    },
    {
      path: "/",
      redirect: "/task",
    },
  ],
})

export default router
