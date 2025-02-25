<template>
  <div id="navbarMenu">
    <a-row class="grid-demo" align="center" :wrap="false">
      <a-col flex="auto">
        <a-menu mode="horizontal" :selected-keys="selectedKeys">
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '30px', fontSize: '20px' }"
            disabled
          >
            <div class="logoBar">
              <div class="logoImage">
                <img src="../assets/logo.png" alt="logo" />
              </div>
              <div class="logoText">小丑OJ</div>
            </div>
          </a-menu-item>
          <a-menu-item
            v-for="item in menuItems"
            :key="item.path"
            @click="jump(item.name)"
          >
            {{ item.meta?.title }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div>{{ loginUser.userName ?? "未登录" }}</div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import routes from "@/router/routes";
import { computed, onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import checkUserAccess from "@/access/check";

const router = useRouter();
const route = useRoute();
const selectedKeys = ref(["/home"]);
const store = useStore();
const loginUser = ref(store.state.user.loginUser);

const baseRoutes = routes.filter((route) => route.name === "Base")[0];

const menuItems = computed(() => {
  return baseRoutes?.children?.filter((item) => {
    if (item.meta?.hide === "true") return false;
    if (!checkUserAccess(loginUser.value, item.meta?.access as string))
      return false;
    return true;
  });
});

// const menuItems = computed(() => {
//   return routes.filter((item) => {
//     if (item.meta?.hide === "true") return false;
//     if (!checkUserAccess(loginUser.value, item.meta?.access as string))
//       return false;
//     return true;
//   });
// });

router.afterEach((to, from) => {
  selectedKeys.value = [to.path];
});
const jump = (name) => {
  router.push({
    name,
  });
};

onMounted(() => {
  selectedKeys.value = [route.path];
});
</script>

<style scoped>
.logoBar {
  display: flex;
}

.logoImage img {
  height: 48px;
  border-radius: 50%;
}

.logoText {
  height: 48px;
  line-height: 48px;
  color: darkred;
  font-size: 20px;
  font-weight: bold;
  margin-left: 20px;
}
</style>
