import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: HomeView,
    meta: {
      title: "主页",
    },
  },
  {
    path: "/about",
    name: "About",
    component: () => import("../views/AboutView.vue"),
    meta: {
      title: "关于",
    },
  },
  {
    path: "/admin",
    name: "Admin",
    component: () => import("../views/AdminView.vue"),
    meta: {
      title: "管理",
      auth: "admin",
    },
  },
  {
    path: "/403",
    name: "403",
    component: () => import("../views/exception/403View.vue"),
    meta: {
      title: "403",
    },
  },
];

export default routes;
