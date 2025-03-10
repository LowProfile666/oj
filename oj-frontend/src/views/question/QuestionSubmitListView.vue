<template>
  <div id="questionListView">
    <a-form :model="pagination" layout="inline">
      <a-form-item field="questionId" label="题号" style="min-width: 280px">
        <a-input v-model="pagination.questionId" placeholder="请输入题号" />
      </a-form-item>
      <a-form-item field="language" label="语言" style="min-width: 280px">
        <a-select
          v-model="pagination.language"
          :style="{ width: '200px' }"
          placeholder="请选择编程语言"
          :allow-clear="true"
        >
          <a-option v-for="option in languageOptions" :key="option"
            >{{ option }}
          </a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-space wrap>
          <a-button type="primary" @click="handleSearch">搜索</a-button>
          <a-button type="primary" status="warning" @click="handleReset"
            >重置
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
    <a-table
      :data="data"
      :pagination="{
        total: pagination.total,
        current: pagination.current,
        pageSize: pagination.pageSize,
      }"
      @page-change="changePage"
      column-resizable
      :columns="columns"
      style="margin-bottom: 20px"
    >
      <template #status="{ record }">
        <a-space wrap>
          <a-tag v-if="record.status === 2" color="green">Accept</a-tag>
          <a-tag v-else-if="record.status === 1" color="lightblue"
            >Judging
          </a-tag>
          <a-tag v-else-if="record.status === 0" color="lightgrey"
            >Pending
          </a-tag>
          <a-tag v-else-if="record.status === 3" color="red">Wrong</a-tag>
        </a-space>
      </template>
      <template #time="{ record }">
        {{ record.judgeInfo.time ?? "N" }}
      </template>
      <template #memory="{ record }">
        {{ record.judgeInfo.memory ?? "N" }}
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { QuestionControllerService } from "../../../generated";
import { Message } from "@arco-design/web-vue";
import router from "@/router";

const columns = [
  {
    title: "ID",
    dataIndex: "id",
  },
  {
    title: "语言",
    dataIndex: "language",
  },
  {
    title: "状态",
    slotName: "status",
  },
  {
    title: "时间",
    slotName: "time",
  },
  {
    title: "空间",
    slotName: "memory",
  },
  {
    title: "题号",
    dataIndex: "questionId",
  },
  {
    title: "用户",
    dataIndex: "userId",
  },
  {
    title: "提交时间",
    dataIndex: "createTime",
  },
];
const data = ref([]);
const pagination = ref({
  questionId: "",
  language: "",
  total: 0,
  pageSize: 5,
  current: 1,
});
const languageOptions = ref([]);

const changePage = (pageNum) => {
  pagination.value.current = pageNum;
  getData();
};
const handleSearch = () => {
  pagination.value.current = 1;
  getData();
};
const handleReset = () => {
  pagination.value.current = 1;
  pagination.value.language = "";
  pagination.value.questionId = "";
  getData();
};

const getData = async () => {
  let res = await QuestionControllerService.listQuestionSubmitByPageUsingPost({
    ...pagination.value,
    sortField: "createTime",
    sortOrder: "desc",
  });
  if (res.code === 0) {
    data.value = res.data.records;
    pagination.value.total = Number(res.data.total);
    pagination.value.current = Number(res.data.current);
  } else {
    Message.error("获取题目失败" + res.message);
  }
  res = await QuestionControllerService.getLanguagesUsingGet();
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

<style scoped>
#questionListView {
  width: 1280px;
  margin: 0 auto;
}
</style>
