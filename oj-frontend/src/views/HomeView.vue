<template>
  <div class="home">
    <CodeEditor :handleChange="handleChange" :value="value" />
    <MarkdownEditor :value="value" :handleChange="handleChange" />
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" />
  </div>
</template>

<script setup lang="ts">
import HelloWorld from "@/components/HelloWorld.vue";
import MarkdownEditor from "@/components/MarkdownEditor.vue"; // @ is an alias to /src
import { ref } from "vue";
import CodeEditor from "@/components/CodeEditor.vue";

const value = ref("");
const handleChange = (e) => {
  console.log(e);
  value.value = e;
};
</script>
