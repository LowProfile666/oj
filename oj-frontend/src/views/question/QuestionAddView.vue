<template>
  <div id="questionAddView">
    <a-form
      :model="form"
      :style="{ width: '800px' }"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item field="title" label="题目标题">
        <a-input v-model="form.title" placeholder="请输入题目标题" />
      </a-form-item>
      <a-form-item field="content" label="题目内容">
        <MarkdownEditor
          :value="form.content"
          :handle-change="handleContentChange"
        />
      </a-form-item>
      <a-form-item field="tags" label="题目标签">
        <a-input-tag
          :default-value="form.tags"
          placeholder="输入标签"
          allow-clear
          v-model="form.tags"
        />
      </a-form-item>
      <a-form-item label="题目配置" :content-flex="false">
        <a-space direction="vertical" fill>
          <a-form-item field="judgeConfig.timeLimit" label="时间限制(ms)">
            <a-input-number
              min="0"
              v-model="form.judgeConfig.timeLimit"
              placeholder="请输入时间限制"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.memoryLimit" label="内存限制(kb)">
            <a-input-number
              min="0"
              v-model="form.judgeConfig.memoryLimit"
              placeholder="请输入内存限制"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.stackLimit" label="堆栈限制(kb)">
            <a-input-number
              min="0"
              v-model="form.judgeConfig.stackLimit"
              placeholder="请输入堆栈限制"
            />
          </a-form-item>
        </a-space>
      </a-form-item>
      <a-form-item field="judgeCase" label="题目用例" style="">
        <div>
          <a-button type="primary" @click="handleAdd">添加</a-button>
        </div>
        <div v-for="(item, index) in form.judgeCase" :key="index">
          <a-space size="mini">
            <a-input
              :style="{ width: '320px' }"
              placeholder="输入用例"
              v-model="item.input"
              allow-clear
            >
              <template #prefix> 输入</template>
            </a-input>
            <a-input
              :style="{ width: '320px' }"
              placeholder="输出用例"
              v-model="item.output"
              allow-clear
            >
              <template #prefix> 输出</template>
            </a-input>
            <a-button @click="handleDelete(index)">删除</a-button>
          </a-space>
        </div>
      </a-form-item>
      <a-form-item field="answer" label="题目答案">
        <MarkdownEditor
          :value="form.answer"
          :handle-change="handleAnswerChange"
        />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive } from "vue";
import MarkdownEditor from "@/components/MarkdownEditor.vue";
import { QuestionControllerService } from "../../../generated";
import { Message } from "@arco-design/web-vue";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();
const form = reactive({
  title: "",
  content: "",
  tags: [],
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  answer: "",
});

const handleSubmit = async () => {
  let res = null;
  if (route.query.id) {
    res = await QuestionControllerService.updateQuestionUsingPost(form);
  } else {
    res = await QuestionControllerService.addQuestionUsingPost(form);
  }
  if (res.code === 0) {
    Message.success(route.query.id ? "修改成功" : "创建成功");
    router.replace({
      name: "QuestionManage",
    });
  } else {
    Message.error((route.query.id ? "修改失败" : "创建失败") + res.message);
  }
};
const handleContentChange = (e) => {
  form.content = e;
};
const handleAnswerChange = (e) => {
  form.answer = e;
};
const handleAdd = () => {
  form.judgeCase.push({
    input: "",
    output: "",
  });
};
const handleDelete = (index) => {
  form.judgeCase.splice(index, 1);
};

const getData = async () => {
  if (!route.query.id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    route.query.id
  );
  if (res.code === 0) {
    res.data.tags = JSON.parse(res.data.tags);
    res.data.judgeCase = JSON.parse(res.data.judgeCase);
    res.data.judgeConfig = JSON.parse(res.data.judgeConfig);
    Object.assign(form, res.data);
  } else {
    Message.error("获取题目失败，" + res.message);
  }
};
onMounted(() => {
  getData();
});
</script>

<style scoped></style>
