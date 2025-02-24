<template>
  <div id="root">
    <BaseLayout />
  </div>
</template>

<script setup lang="ts">
import BaseLayout from "@/layouts/BaseLayout.vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();
store.dispatch("user/login", {
  id: 1,
  username: "zsm",
  role: "admin",
});
const loginUser = store.state.user.loginUser;

router.beforeEach((to, from, next) => {
  if (to.meta.auth === "admin" && loginUser.role !== "admin") {
    next("/403");
    return;
  }
  next();
});
</script>
