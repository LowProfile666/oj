<template>
  <div id="questionListView">
    <a-form :model="pagination" layout="inline">
      <a-form-item field="title" label="标题" style="min-width: 280px">
        <a-input v-model="pagination.title" placeholder="请输入标题" />
      </a-form-item>
      <a-form-item field="tags" label="标签" style="min-width: 280px">
        <a-input-tag v-model="pagination.tags" placeholder="请输入标签" />
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
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="tag in record.tags" :key="tag" color="green"
            >{{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #acRatio="{ record }">
        {{
          `${record.submitNum ? record.acceptedNum / record.submitNum : 0} % (${
            record.acceptedNum
          }/${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-button type="primary" @click="toDoQuestion(record.id)"
          >去干他
        </a-button>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref, watchEffect } from "vue";
import { QuestionControllerService } from "../../../generated";
import { Message } from "@arco-design/web-vue";
import router from "@/router";
import moment from "moment";

const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率（AC/SUBMIT）",
    slotName: "acRatio",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    title: "",
    slotName: "optional",
  },
];
const data = ref([]);
const pagination = ref({
  title: "",
  tags: [],
  total: 0,
  pageSize: 5,
  current: 1,
});

const toDoQuestion = (id) => {
  router.push({
    name: "QuestionDo",
    params: {
      id: id,
    },
  });
};
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
  pagination.value.tags = [];
  pagination.value.title = "";
  getData();
};

const getData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    pagination.value
  );
  if (res.code === 0) {
    data.value = res.data.records;
    pagination.value.total = Number(res.data.total);
    pagination.value.current = Number(res.data.current);
  } else {
    Message.error("获取题目失败" + res.message);
  }
};

onMounted(() => {
  getData();
});
</script>

<style scoped>
#questionListView {
  width: 90%;
  margin: 0 auto;
}
</style>
