<script setup>
import { useRoute } from "vue-router";
import { reactive, ref } from "vue";
import { Edit } from "@element-plus/icons-vue";

const route = useRoute();
const id = route.params.id;

// 导入题目数据
const title = ref();
const description = ref();
import { queryProblemService } from "@/api/problem";
const getProblemInfo = async () => {
  let result = await queryProblemService(id);
  title.value = result.data.title;
  description.value = result.data.description;
  code.value = result.data.templateCode;
};
getProblemInfo();

// 代码编辑器
import MonacoEditor from "@/components/MonacoEditor.vue";
import prettier from "prettier/standalone";
import java from "prettier-plugin-java";
const code = ref("// 在这里编写代码");
const language = ref("java");
const theme = ref("vs-dark");

const output = ref();

const languageOptions = ref([
  {
    key: "Java",
    label: "Java",
    value: "java",
  },
  {
    key: "C++",
    label: "C++",
    value: "c++",
  },
  {
    key: "Javascript",
    label: "Javascript",
    value: "javascript",
  },
]);

const formatCode = async () => {
  const formattedCode = await prettier.format(code.value, {
    parser: "java",
    plugins: [java],
  });
  code.value = formattedCode;
};

const submitCode = () => {
  // 这里添加提交代码的逻辑
  alert("代码已提交");
};

import { compileProblemService } from "@/api/compile";
import { ElMessage } from "element-plus";
const runCode = async () => {
  console.log(id)
  console.log(code.value)
  if (code.value == " " || code.value == null || code.value == "// 请在此处编写你的 Java 代码") {
      ElMessage.error("提交代码为空！");
      return;
  }
  let compileData = reactive({
    id: id,
    code: code.value,
  });
  let result = await compileProblemService(compileData);
  if (result.data.error == 0) {
    // 编译运行没有问题
    output.value = result.data.stdout;
  } else {
    output.value = result.data.reason;
  } 
};
</script>

<template>
  <div class="common-layout">
    <el-container style="height: 100vh">
      <el-header>
        <el-menu
          :default-active="$route.path"
          router
          class="el-menu-demo"
          mode="horizontal"
          :ellipsis="false"
        >
          <el-menu-item index="/index"> 题目列表 </el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <el-row :gutter="10" style="height: 100%">
          <el-col :span="11" style="height: 100%">
            <el-card shadow="hover" style="height: 100%; overflow: auto">
              <h2>{{ title }}</h2>
              <p>{{ description }}</p>
            </el-card>
          </el-col>
          <el-col :span="13" style="height: 100%">
            <el-card shadow="hover" style="height: 100%; overflow: auto">
              <div class="editor-controls">
                <el-select
                  v-model="language"
                  placeholder="Select"
                  size="large"
                  style="width: 180px"
                >
                  <el-option
                    v-for="item in languageOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
                <el-button
                  type="warning"
                  size="large"
                  plain
                  :icon="Edit"
                  @click="formatCode"
                  >格式化代码</el-button
                >
                <el-text class="mx-1" type="danger">目前仅支持Java</el-text>
              </div>
              <div id="monaco-editor" style="height: 400px; margin: 2px 0px">
                <MonacoEditor
                  v-model:value="code"
                  :language="language"
                  :theme="theme"
                />
              </div>
              <div class="submit-controls">
                <el-button type="primary" @click="runCode">运行代码</el-button>
                <el-button type="success" @click="submitCode"
                  >提交代码</el-button
                >
              </div>
              <h3>输出结果</h3>
              <el-card style="height: 100%; overflow: auto">
                <pre>{{ output }}</pre>
              </el-card>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
.el-col {
  border-radius: 4px;
}
h2 {
  margin-top: 0;
}
.editor-controls .el-select,
.el-button {
  margin-right: 10px;
}
</style>
