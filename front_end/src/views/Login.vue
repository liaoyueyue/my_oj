<script lang="ts" setup>
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { User, Lock, Unlock, Right, Message } from "@element-plus/icons-vue";
import { userRegisterService, userLoginService } from "@/api/user.js";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { useTokenStore } from "@/stores/token";

// 路由
const router = useRouter();

// token存贮
const tokenStore = useTokenStore();

// 登录注册表单切换
const activeTab = ref("login");

// 表单大小
const formSize = ref("large");

// 登录表单数据和验证规则
interface LoginForm {
  account: string;
  password: string;
  captcha: string;
}

const loginFormRef = ref<FormInstance>();
const loginForm = reactive<LoginForm>({
  account: "",
  password: "",
  captcha: "",
});

const loginRules = reactive<FormRules<LoginForm>>({
  account: [
    { required: true, message: "请输入邮箱或者用户名", trigger: "blur" },
    {
      min: 2,
      max: 25,
      message: "邮箱或者用户名长度不符合规则",
      trigger: "blur",
    },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 16, message: "密码长度不符合规则", trigger: "blur" },
  ],
  captcha: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { min: 4, max: 6, message: "验证码长度不符合规则", trigger: "blur" },
  ],
});

// 登录函数
const login = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let result = await userLoginService(loginForm);
      ElMessage.success("登录成功");
      // 把得到的token存入pinia
      tokenStore.setToken(result.data);
      router.push("/");
    }
  });
};

// 注册表单数据和验证规则
interface RegisterForm {
  email: string;
  vercode: string;
  password: string;
  confirmPassword: string;
  nickname: string;
}
const registerFormRef = ref<FormInstance>();
const registerForm = reactive<RegisterForm>({
  email: "",
  vercode: "",
  password: "",
  confirmPassword: "",
  nickname: "",
});

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === "") {
    callback(new Error("请重新输入密码"));
  } else if (value !== registerForm.password) {
    callback(new Error("请保证两次输入的密码一致"));
  } else {
    callback();
  }
};

const registerRules = reactive<FormRules<RegisterForm>>({
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱", trigger: "blur" },
  ],
  vercode: [
    { required: true, message: "请输入邮箱验证码", trigger: "blur" },
    { min: 4, max: 6, message: "邮箱验证码长度不符合规则", trigger: "blur" },
  ],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  confirmPassword: [{ validator: validatePass, trigger: "blur" }],
  nickname: [{ required: true, message: "请输入昵称", trigger: "blur" }],
});

// 注册函数
const register = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let result = await userRegisterService(registerForm);
      ElMessage.success("注册成功 | " + result.data);
    }
  });
};

// 图片验证码相关数据
const captchaSrc = ref("");
const refreshCaptcha = () => {
  // 更新图片验证码，这里根据实际情况获取图片验证码的接口
  captchaSrc.value = ""; // 假设此处为更新验证码图片链接的逻辑
};

// 切换表单
const toggleForm = (formName) => {
  activeTab.value = formName;
  // 切换表单时重置表单数据
  if (formName === "login") {
    resetForm(loginFormRef.value);
  } else if (formName === "register") {
    resetForm(registerFormRef.value);
  }
};

// 清空表单
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};
</script>

<template>
  <div class="login-register-container">
    <div v-if="activeTab === 'login'">
      <el-card>
        <!-- 登录表单 -->
        <h2>登录</h2>
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          :size="formSize"
          hide-required-asterisk="false"
          style="width: 360px"
        >
          <el-form-item prop="account">
            <el-input
              v-model="loginForm.account"
              placeholder="请输入邮箱或者用户名"
              :prefix-icon="User"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              placeholder="请输入密码"
              show-password
              :prefix-icon="Lock"
            ></el-input>
          </el-form-item>
          <el-form-item prop="captcha">
            <el-input
              v-model="loginForm.captcha"
              placeholder="请输入验证码"
              :prefix-icon="Unlock"
            ></el-input>
            <img
              :src="captchaSrc"
              @click="refreshCaptcha"
              class="captcha-image"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="login(loginFormRef)"
              style="width: 100%"
              >登录</el-button
            >
          </el-form-item>
          <el-link :icon="Right" @click="toggleForm('register')">注册</el-link>
        </el-form>
      </el-card>
    </div>
    <div v-if="activeTab === 'register'">
      <el-card>
        <!-- 注册表单 -->
        <h2>注册</h2>
        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="register-form"
          :size="formSize"
          hide-required-asterisk="false"
          style="width: 360px"
        >
          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="请输入邮箱"
              :prefix-icon="Message"
            ></el-input>
          </el-form-item>
          <el-form-item prop="vercode">
            <el-input
              v-model="registerForm.vercode"
              placeholder="请输入邮箱验证码"
              :prefix-icon="Unlock"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              placeholder="请输入密码"
              show-password
              :prefix-icon="Lock"
            ></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              placeholder="请确认密码"
              show-password
              :prefix-icon="Lock"
            ></el-input>
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input
              v-model="registerForm.nickname"
              placeholder="请输入昵称"
              :prefix-icon="User"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="register(registerFormRef)"
              style="width: 100%"
              >注册</el-button
            >
          </el-form-item>
          <el-link :icon="Right" @click="toggleForm('login')">返回登录</el-link>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.login-register-container {
  height: 98vh;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  flex-direction: column; /* 垂直布局 */
  margin: 0 auto;
}

.captcha-image {
  cursor: pointer;
  margin-left: 10px;
  vertical-align: middle;
}
</style>
