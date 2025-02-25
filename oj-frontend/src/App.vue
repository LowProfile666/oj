<template>
  <div id="root">
    <router-view />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useStore } from "vuex";

const store = useStore();
const init = () => {
  console.log("欢迎使用我的OJ");
  store.dispatch("user/getLoginUser");
};
onMounted(() => {
  init();
});
</script>
