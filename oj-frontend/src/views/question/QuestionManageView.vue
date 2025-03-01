<template>
  <div id="questionManageView">
    <a-button type="primary" @click="toAdd">
      <template #icon>
        <icon-plus />
      </template>
      添加
    </a-button>
    <a-table
      :data="data"
      :pagination="false"
      column-resizable
      style="margin-bottom: 20px"
    >
      <template #columns>
        <a-table-column title="ID" dataIndex="id" />
        <a-table-column title="标题" data-index="title"></a-table-column>
        <a-table-column title="内容" data-index="content"></a-table-column>
        <a-table-column title="提交数" data-index="submitNum"></a-table-column>
        <a-table-column
          title="通过数"
          data-index="acceptedNum"
        ></a-table-column>
        <a-table-column title="收藏数" data-index="favourNum"></a-table-column>
        <a-table-column title="点赞数" data-index="thumbNum"></a-table-column>
        <a-table-column title="标签" data-index="tags">
          <template #cell="{ record }">
            <a-space wrap>
              <a-tag
                v-for="tag in JSON.parse(record.tags)"
                :key="tag"
                color="green"
                >{{ tag }}
              </a-tag>
            </a-space>
          </template>
        </a-table-column>
        <a-table-column title="配置" data-index="judgeConfig">
          <template #cell="{ record }">
            <a-space wrap>
              <a-tag color="blue"
                >时间：{{ JSON.parse(record.judgeConfig).timeLimit }}ms
              </a-tag>
              <a-tag color="blue"
                >内存：{{ JSON.parse(record.judgeConfig).memoryLimit }}kb
              </a-tag>
              <a-tag color="blue"
                >堆栈：{{ JSON.parse(record.judgeConfig).stackLimit }}kb
              </a-tag>
            </a-space>
          </template>
        </a-table-column>
        <a-table-column title="创建用户" data-index="userId"></a-table-column>
        <a-table-column
          title="创建时间"
          data-index="createTime"
        ></a-table-column>
        <a-table-column title="操作">
          <template #cell="{ record }">
            <a-space>
              <a-button
                type="primary"
                status="normal"
                @click="updateQuestion(record)"
                >编辑
              </a-button>
              <a-button
                type="primary"
                status="danger"
                @click="deleteQuestion(record)"
                >删除
              </a-button>
            </a-space>
          </template>
        </a-table-column>
      </template>
    </a-table>

    <div style="margin-bottom: 20px; display: flex; justify-content: right">
      <a-pagination
        :total="pagination.total"
        :current="pagination.current"
        :page-size="pagination.pageSize"
        @change="changePage"
        show-total
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { QuestionControllerService } from "../../../generated";
import { Message } from "@arco-design/web-vue";
import router from "@/router";
import { IconPlus } from "@arco-design/web-vue/es/icon";

const data = ref([]);
const pagination = ref({
  total: 0,
  pageSize: 5,
  current: 1,
});
const changePage = (pageNum) => {
  pagination.value.current = pageNum;
  getData();
};
const toAdd = () => {
  router.push({
    name: "QuestionAdd",
  });
};
const getData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
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
const updateQuestion = (record) => {
  router.push({
    name: "QuestionUpdate",
    query: {
      id: record.id,
    },
  });
};
const deleteQuestion = async (record) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: record.id,
  });
  if (res.code === 0) {
    Message.success("删除成功");
    getData();
  } else {
    Message.error("删除失败，" + res.message);
  }
};

onMounted(() => {
  getData();
});
</script>

<style scoped>
#questionManageView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
