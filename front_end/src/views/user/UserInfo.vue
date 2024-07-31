<script lang="ts" setup>
import { reactive, ref } from "vue";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";

const formSize = ref("large");

// # 用户信息
import { useUserInfoStore } from "@/stores/userinfo";
const userinfoStore = useUserInfoStore();

// # 修改用户信息表单数据和验证规则
interface UpdateUserInfoForm {
  id: number;
  username: string;
  nickname: string;
  email: string;
}

const updateUserInfoFormRef = ref<FormInstance>();
const updateUserInfoForm = reactive<UpdateUserInfoForm>({
  id: userinfoStore.info.id,
  username: userinfoStore.info.username,
  nickname: userinfoStore.info.nickname,
  email: userinfoStore.info.email,
});

const updateUserInfoRules = reactive<FormRules<UpdateUserInfoForm>>({
  nickname: [
    { required: true, message: "请输入昵称", trigger: "blur" },
    {
      min: 2,
      max: 16,
      message: "昵称长度不符合规则",
      trigger: "blur",
    },
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "邮箱格式不正确", trigger: "blur" },
  ],
});

import { userInfoUpdateService } from "@/api/user";
// 修改用户信息函数
const updateInfo = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      // 修改用户信息请求
      let result = userInfoUpdateService(updateUserInfoForm);
      ElMessage.success("修改成功");
      // 更新 pinia 中的用户信息
      userinfoStore.info.nickname = updateUserInfoForm.nickname;
      userinfoStore.info.email = updateUserInfoForm.email;
    }
  });
};
</script>

<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>基本资料</span>
      </div>
    </template>
    <el-row>
      <el-col :span="10">
        <!-- 修改用户信息表单 -->
        <el-form
          class="update-userinfo-form"
          ref="updateUserInfoFormRef"
          :model="updateUserInfoForm"
          :rules="updateUserInfoRules"
          hide-required-asterisk="false"
          :size="formSize"
        >
          <el-form-item label="登录名称">
            <el-input v-model="updateUserInfoForm.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="nickname">
            <el-input
              v-model="updateUserInfoForm.nickname"
              placeholder="请输入需要修改的昵称"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户邮箱" prop="email">
            <el-input
              v-model="updateUserInfoForm.email"
              placeholder="请输入需要修改的邮箱"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateInfo(updateUserInfoFormRef)"
              >提交修改</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped></style>
