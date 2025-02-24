<template>
  <div id="navbarMenu">
    <a-row class="grid-demo" style="margin-bottom: 16px" align="center">
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
            v-for="route in routes"
            :key="route.path"
            @click="jump(route.name)"
          >
            {{ route.meta.title }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div>{{ store.state.user.loginUser.username ?? "未登录" }}</div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import routes from "@/router/routes";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const selectedKeys = ref(["/"]);
const store = useStore();

// store.dispatch("user/login", {
//   id: 1,
//   username: "zsm",
//   role: "admin",
// });

router.afterEach((to, from) => {
  selectedKeys.value = [to.path];
});
const jump = (name) => {
  router.push({
    name,
  });
};
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
