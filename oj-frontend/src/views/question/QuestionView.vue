<template>
  <div id="questionView">
    <a-split
      :style="{
        height: '100vh',
        width: '100%',
        minWidth: '500px',
        border: '1px solid var(--color-border)',
        borderRadius: '5px',
      }"
      min="0.3"
      max="0.8"
      default-size="0.5"
    >
      <template #first>
        <a-card :bordered="false" header-style="height: 48px">
          <template #extra>
            <!--            todo 显示点赞收藏数等信息-->
          </template>
          <template #title>
            <h2>{{ question.title }}</h2>
          </template>
          <a-tabs type="line" size="small">
            <a-tab-pane key="1" title="题目描述">
              <a-row>
                <a-col :span="14">
                  <a-space wrap>
                    <a-tag v-for="tag in question.tags" :key="tag" color="green"
                      >{{ tag }}
                    </a-tag>
                  </a-space>
                </a-col>
                <a-col :span="10">
                  <a-space size="medium">
                    <span>
                      时间限制：{{ question.judgeConfig?.timeLimit }} ms
                    </span>
                    <span>
                      空间限制：{{ question.judgeConfig?.memoryLimit }} kb
                    </span>
                  </a-space>
                </a-col>
              </a-row>
              <a-row style="margin-top: 20px">
                <a-col
                  ><span style="font-style: italic; font-weight: bold"
                    >题目作者：{{ question.userVO?.userName }}</span
                  ></a-col
                >
              </a-row>
              <MarkdownViewer :value="question.content" />
            </a-tab-pane>
            <a-tab-pane key="2" title="题解">
              <!--              todo 题解-->
            </a-tab-pane>
            <a-tab-pane key="3" title="评论">
              <!--              todo 评论-->
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </template>
      <template #second>
        <div>
          <a-split
            direction="vertical"
            :style="{ height: '100vh' }"
            min="0.6"
            max="0.8"
            default-size="0.6"
          >
            <template #first>
              <a-card
                :bordered="false"
                style="height: 80%"
                body-style="height: 100%"
                header-style="height: 48px"
              >
                <template #title>
                  <div style="display: flex">
                    <div>
                      <a-form :model="form" layout="inline">
                        <a-form-item
                          field="language"
                          label="语言"
                          style="min-width: 280px; margin-bottom: 0"
                        >
                          <a-select
                            v-model="form.language"
                            :style="{ width: '200px' }"
                            placeholder="请选择编程语言"
                          >
                            <a-option
                              v-for="option in languageOptions"
                              :key="option"
                              >{{ option }}
                            </a-option>
                          </a-select>
                        </a-form-item>
                      </a-form>
                    </div>
                    <div
                      style="display: flex; justify-content: right; width: 100%"
                    >
                      <a-space>
                        <a-button type="outline">运行</a-button>
                        <a-button
                          type="primary"
                          status="success"
                          @click="submitQuestion"
                          >提交
                        </a-button>
                      </a-space>
                    </div>
                  </div>
                </template>
                <CodeEditor
                  :language="form.language"
                  :value="form.code"
                  :handle-change="handleChange"
                />
              </a-card>
            </template>
            <template #second>
              <a-typography-paragraph>运行结果等信息</a-typography-paragraph>
            </template>
          </a-split>
        </div>
      </template>
    </a-split>
  </div>
</template>

<script lang="ts" setup>
import CodeEditor from "@/components/CodeEditor.vue";
import MarkdownViewer from "@/components/MarkdownViewer.vue";
import { defineProps, onMounted, ref } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitControllerService,
} from "../../../generated";
import { Message } from "@arco-design/web-vue";

const props = defineProps({
  id: String,
});
const question = ref({});
const form = ref({
  language: "java",
  questionId: props.id,
  code: "",
});
const languageOptions = ref([]);

const submitQuestion = async () => {
  const res = await QuestionSubmitControllerService.doQuestionSubmitUsingPost(
    form.value
  );
  if (res.code === 0) {
    Message.success("提交成功");
  } else {
    Message.error("提交失败，" + res.message);
  }
};
const handleChange = (value) => {
  form.value.code = value;
};
const getData = async () => {
  let res = await QuestionControllerService.getQuestionVoByIdUsingGet(props.id);
  if (res.code === 0) {
    question.value = res.data;
  } else {
    Message.error("获取题目失败，" + res.message);
  }
  res = await QuestionControllerService.getLanguages();
  if (res.code === 0) {
    languageOptions.value = res.data;
  } else {
    Message.error("获取编程语言失败，" + res.message);
  }
};
onMounted(() => {
  getData();
});
</script>

<style scoped></style>
