<template>
  <Editor
    :value="props.value"
    :plugins="plugins"
    :mode="props.mode"
    @change="props.handleChange"
    placeholder="请输入内容"
  />
</template>

<script lang="ts" setup>
import { defineProps } from "vue";
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import "highlight.js/styles/github.css";
import "juejin-markdown-themes/dist/juejin.min.css";

const props = defineProps({
  value: String,
  handleChange: Function,
  mode: {
    type: String,
    default: "split",
  },
});

const plugins = [gfm(), highlight()];
</script>
