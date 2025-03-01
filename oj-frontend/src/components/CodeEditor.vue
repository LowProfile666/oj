<template>
  <div id="container" ref="editorContainerRef" style="height: 100%"></div>
</template>

<script lang="ts" setup>
import * as monaco from "monaco-editor";
import { onMounted, ref, defineProps, watch } from "vue";

const props = defineProps({
  value: String,
  language: {
    type: String,
    default: "java",
  },
  handleChange: {
    type: Function,
    default: () => {
      console.log();
    },
  },
});

const editorContainerRef = ref();

watch(
  () => props.language,
  () => {
    monaco.editor.setModelLanguage(editor.getModel(), props.language);
  }
);

let editor = {};
onMounted(() => {
  editor = monaco.editor.create(editorContainerRef.value, {
    value: props.value,
    language: props.language,
    automaticLayout: true,
    theme: "vs-dark",
    fontSize: 18,
    placeholder: "// 在这里输入代码",
  });
  editor.onDidChangeModelContent(() => {
    props.handleChange(editor.getValue());
  });
});
</script>
