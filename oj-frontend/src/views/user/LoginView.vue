<template>
  <div id="loginView">
    <div class="logoImage">
      <a-space
        ><img src="@/assets/logo.png" alt="logo" />
        <h2>欢迎登录</h2></a-space
      >
    </div>
    <div class="loginDiv">
      <a-form
        :model="form"
        :style="{ textAlign: 'center', width: '400px', margin: '0 auto' }"
        @submit="handleSubmit"
      >
        <a-form-item
          field="userAccount"
          tooltip="电话、邮箱、用户名"
          label="账号"
        >
          <a-input v-model="form.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item field="userPassword" label="密码" tooltip="密码不少于8位">
          <a-input-password
            v-model="form.userPassword"
            placeholder="请输入密码"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit">登录</a-button>
          <div style="text-align: right; width: 100%">
            <router-link :to="{ name: 'Register' }"
              >没有账号？立即注册
            </router-link>
          </div>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { useStore } from "vuex";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();
const store = useStore();
const form = reactive({
  userAccount: "",
  userPassword: "",
});
const handleSubmit = async () => {
  const res = await store.dispatch("user/login", form);
  if (res) {
    const redirect = route.query.redirectName ?? "Home";
    await router.replace({
      name: redirect as string,
    });
  }
};
</script>

<style scoped>
.logoImage {
  text-align: center;
}

.logoImage img {
  height: 88px;
  border-radius: 50%;
}
</style>
