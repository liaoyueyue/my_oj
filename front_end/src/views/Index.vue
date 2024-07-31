<script lang="ts" setup>
import { ref, reactive } from "vue";

import HeaderMenu from "@/components/HeaderMenu.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

const router = useRouter();

// # 题目表格
const currentPage = ref(1);
const pageSize = ref(10);
const small = ref(false);
const background = ref(false);
const disabled = ref(false);
const total = ref();

const searchForm = reactive({
  collectionName: "",
  level: "",
});

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`);
  getProblemList();
};
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`);
  getProblemList();
};

const onSubmit = () => {
  getProblemList();
};

const onResetting = () => {
  searchForm.collectionName = "";
  searchForm.level = "";
  getProblemList();
  ElMessage.success("重置成功");
};

const tableData = ref();

// 导入题目表格数据
import { problemListService } from "@/api/problem";
const getProblemList = async () => {
  let result = await problemListService(
    currentPage.value,
    pageSize.value,
    searchForm
  );
  tableData.value = result.data.items;
  total.value = result.data.total;
};
getProblemList();

const handleCellClick = (row, column, cell, event) => {
  router.push(`/practice/${row.id}`);
};
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <HeaderMenu/>
      </el-header>
      <el-main>
        <el-card>
          <div class="card-header">
            <span style="font-size: larger; font-weight: bold"
              >选择需要做的题</span
            >
          </div>
          <hr style="margin-top: 20px" />
          <el-form :inline="true" :model="searchForm" class="demo-form-inline">
            <el-form-item label="合集名称">
              <el-input
                v-model="searchForm.collectionName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
            <el-form-item label="等级">
              <el-select
                v-model="searchForm.level"
                placeholder="请选择"
                clearable
                @change="getProblemList()"
              >
                <el-option label="简单" value="简单" />
                <el-option label="中等" value="中等" />
                <el-option label="困难" value="困难" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">搜索</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="default" @click="onResetting">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table
            :data="tableData"
            @cell-click="handleCellClick"
            style="width: 100%"
          >
            <el-table-column prop="id" label="编号" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="collectionName" label="合集名称" />
            <el-table-column prop="updateTime" label="更新时间" />
            <el-table-column prop="level" label="等级" />
          </el-table>
          <el-pagination
            class="el-p"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 15, 20]"
            :small="small"
            :disabled="disabled"
            :background="background"
            layout="jumper, total, sizes, prev, pager, next"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-main>
      <el-footer>myoj @2024 Created by liaoyueyue</el-footer>
    </el-container>
  </div>
</template>

<style scoped>
.el-p {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}

.card-header {
  display: flex;
  justify-content: space-between;
}
</style>
