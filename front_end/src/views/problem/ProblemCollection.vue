<script lang="ts" setup>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";

// # 题目合集表格
const currentPage = ref(1);
const pageSize = ref(10);
const small = ref(false);
const background = ref(false);
const disabled = ref(false);
const total = ref();

const searchForm = reactive({
  collectionName: "",
});

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`);
  getcollectionList();
};
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`);
  getcollectionList();
};

const onSubmit = () => {
  getcollectionList();
};

const onResetting = () => {
  searchForm.collectionName = "";
  getcollectionList();
  ElMessage.success("重置成功");
};

const tableData = ref();

// 导入题目合集表格数据
import { collectionListService } from "@/api/collection";
const getcollectionList = async () => {
  let result = await collectionListService(
    currentPage.value,
    pageSize.value,
    searchForm
  );
  tableData.value = result.data.items;
  total.value = result.data.total;
};
getcollectionList();

// # 弹窗 题目合集
const dialogVisible = ref(false);
const dialogTitle = ref("");

// 显示添加弹窗
const showAddDialog = () => {
  dialogTitle.value = "添加合集";
  clearCollectionForm();
  dialogVisible.value = true;
};

// 显示编辑弹窗
const showEditDialog = (row) => {
  dialogTitle.value = "编辑合集";
  collectionForm.name = row.name;
  collectionForm.description = row.description;
  collectionForm.id = row.id;
  dialogVisible.value = true;
};

// # 添加合集表单数据和验证规则
interface CollectionForm {
  id?: number; // 添加一个可选的 id 属性
  name: string;
  description: string;
}

const collectionFormRef = ref<FormInstance>();
const collectionForm = reactive<CollectionForm>({
  name: "",
  description: "",
});

const collectionRules = reactive<FormRules<CollectionForm>>({
  name: [
    { required: true, message: "请输入需要添加合集的名称", trigger: "blur" },
  ],
  description: [
    { required: true, message: "请输入需要添加合集的描述", trigger: "blur" },
  ],
});

// 清空合集表单数据函数
const clearCollectionForm = () => {
  collectionForm.name = "";
  collectionForm.description = "";
};

import {
  addCollectionService,
  updateCollectionService,
  deleteCollectionService,
} from "@/api/collection";
// 添加合集函数
const addCollection = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let result = await addCollectionService(collectionForm);
      ElMessage.success("添加成功");
      getcollectionList(); // 刷新表格
      dialogVisible.value = false;
    }
  });
};
// 编辑合集函数
const editCollection = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let result = await updateCollectionService(collectionForm);
      ElMessage.success("修改成功");
      getcollectionList(); // 刷新表格
      dialogVisible.value = false;
    }
  });
};
// 删除合集函数
const deleteCollection = (row) => {
  ElMessageBox.confirm("此操作将永久删除该合集。确认继续？", "警告", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let result = await deleteCollectionService(row.id);
      ElMessage({
        type: "success",
        message: "删除成功",
      });
      getcollectionList();
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "取消删除",
      });
    });
};
</script>

<template>
  <el-card>
    <div class="card-header">
      <span style="font-size: larger; font-weight: bold">题目合集管理</span>
      <el-button type="primary" @click="showAddDialog">添加题目合集</el-button>
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
      <el-form-item>
        <el-button type="primary" @click="onSubmit">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="default" @click="onResetting">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="id" label="编号" />
      <el-table-column prop="name" label="合集名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column prop="updateTime" label="更新时间" />
      <el-table-column prop="operation" label="操作" width="180">
        <template #default="{ row }">
          <el-row>
            <el-button
              type="primary"
              :icon="Edit"
              circle
              @click="showEditDialog(row)"
            />
            <el-button
              type="danger"
              :icon="Delete"
              circle
              @click="deleteCollection(row)"
            />
          </el-row>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
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
  <!-- 添加弹窗 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500">
    <!-- 添加编辑合集表单 -->
    <el-form
      ref="collectionFormRef"
      :model="collectionForm"
      :rules="collectionRules"
      class="addCollection-form"
      label-width="auto"
      style="max-width: 600px"
    >
      <el-form-item label="合集名称" prop="name">
        <el-input v-model="collectionForm.name" placeholder="请输入合集名称" />
      </el-form-item>
      <el-form-item label="合集描述" prop="description">
        <el-input
          v-model="collectionForm.description"
          placeholder="请输入合集描述"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          v-show="dialogTitle == '添加合集'"
          @click="addCollection(collectionFormRef)"
          >确认</el-button
        >
        <el-button
          type="primary"
          v-show="dialogTitle == '编辑合集'"
          @click="editCollection(collectionFormRef)"
          >确认</el-button
        >
      </div>
    </template>
  </el-dialog>
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
