import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import ACCESSENUM from "@/access/AccessEnum";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Base",
    component: () => import("../layouts/BaseLayout.vue"),
    redirect: "/home",
    children: [
      {
        path: "/home",
        name: "Home",
        component: () => import("../views/HomeView.vue"),
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
          access: ACCESSENUM.ADMIN,
        },
      },
      {
        path: "/403",
        name: "403",
        component: () => import("../views/exception/403View.vue"),
        meta: {
          title: "403",
          hide: "true",
        },
      },
    ],
  },
  {
    path: "/user",
    name: "User",
    component: () => import("../layouts/UserLayout.vue"),
    redirect: "/user/login",
    children: [
      {
        path: "login",
        name: "Login",
        component: () => import("../views/user/LoginView.vue"),
        meta: {
          title: "用户登录",
        },
      },
      {
        path: "register",
        name: "Register",
        component: () => import("../views/user/RegisterView.vue"),
        meta: {
          title: "用户注册",
        },
      },
    ],
  },
];

export default routes;
