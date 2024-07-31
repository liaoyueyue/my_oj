<script lang="ts" setup>
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";

const formSize = ref("large");
// 修改密码表单数据和验证规则
interface UpdatePwdForm {
  oldPwd: string;
  newPwd: string;
  confimPwd: string;
}

const updatePwdFormRef = ref<FormInstance>();
const updatePwdForm = reactive<UpdatePwdForm>({
  oldPwd: "",
  newPwd: "",
  confimPwd: "",
});

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === "") {
    callback(new Error("请重新输入密码"));
  } else if (value === updatePwdForm.oldPwd) {
    callback(new Error("不能和原密码相同"));
  } else if (value !== updatePwdForm.newPwd) {
    callback(new Error("两次密码不匹配"));
  } else {
    callback();
  }
};

const updatePwdRules = reactive<FormRules<UpdatePwdForm>>({
  oldPwd: [
    { required: true, message: "请输入旧密码", trigger: "blur" },
    { min: 6, max: 16, message: "密码长度不符合规则", trigger: "blur" },
  ],
  newPwd: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 16, message: "密码长度不符合规则", trigger: "blur" },
  ],
  confimPwd: [{ validator: validatePass, trigger: "blur" }],
});

import router from "@/router";
import { userUpdatePwd } from "@/api/user";
// 修改密码函数
const updatePwd = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let result = userUpdatePwd(updatePwdForm);
      ElMessage.success("修改成功");
      router.push("/login");
    }
  });
};
</script>
<template>
  
  <el-card>
    <template #header>
      <div class="header">
        <span>更改密码</span>
      </div>
    </template>
    <el-form
      ref="updatePwdFormRef"
      :model="updatePwdForm"
      :rules="updatePwdRules"
      class="updatePwd-form"
      :size="formSize"
      hide-required-asterisk="false"
      style="width: 360px"
    >
      <el-form-item prop="oldPwd">
        <el-input
          v-model="updatePwdForm.oldPwd"
          placeholder="请输入旧密码"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item prop="newPwd">
        <el-input
          v-model="updatePwdForm.newPwd"
          placeholder="请输入密码"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item prop="confimPwd">
        <el-input
          v-model="updatePwdForm.confimPwd"
          placeholder="请再次输入密码"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="updatePwd(updatePwdFormRef)"
          >修改密码
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<style scoped></style>
