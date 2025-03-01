<template>
  <Viewer :value="props.value" :plugins="plugins" />
</template>

<script lang="ts" setup>
import { defineProps } from "vue";
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Viewer } from "@bytemd/vue-next";
import "highlight.js/styles/github.css";
import "juejin-markdown-themes/dist/juejin.min.css";

const props = defineProps({
  value: String,
});

const plugins = [gfm(), highlight()];
</script>
