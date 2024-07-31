<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useTokenStore } from "@/stores/token";
import { userInfoService } from "@/api/user";
import { useUserInfoStore } from "@/stores/userinfo";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  User,
  Crop,
  ArrowDown,
  SwitchButton,
  Edit,
} from "@element-plus/icons-vue";
import defaultAvatar from "@/assets/images/default_avatar.jpg";

const router = useRouter();
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();

// 导航栏
const navList = ref([
  { route: "/index", navItem: "首页" },
  { route: "/manage", navItem: "管理面板" },
]);

const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
};

// 获取用户详细信息
const getUserInfo = async () => {
  let result = await userInfoService();
  userInfoStore.setInfo(result.data);
};
getUserInfo();

// 下拉菜单事件
const dropdownHandleCommand = (command) => {
  if (command === "logout") {
    ElMessageBox.confirm("是否退出登录？需要重新登录！", "警告", {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(async () => {
        // 1. 清空 pinia 中的 token 和 userinfo
        tokenStore.removeToken();
        userInfoStore.removeInfo();
        // 2. 跳转到登录页面
        router.push("/login");
        ElMessage({
          type: "success",
          message: "退出登录成功",
        });
      })
      .catch(() => {
        ElMessage({
          type: "info",
          message: "取消退出登录",
        });
      });
  } else {
    router.push("/manage/user/" + command);
  }
};
</script>

<template>
  <el-menu
    :default-active="$route.path"
    router
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
  >
    <el-menu-item index="/">
      <img style="width: 100px" src="@/assets/images/logo.svg" alt="logo" />
    </el-menu-item>
    <el-menu-item
      v-for="(item, i) in navList"
      :key="i"
      :index="item.route"
    >
      {{ item.navItem }}
    </el-menu-item>
    <div class="flex-grow" />
    <el-menu-item>
      <el-dropdown @command="dropdownHandleCommand">
        <span class="el-dropdown-link">
          <el-avatar
            :src="
              userInfoStore.info.userPic
                ? userInfoStore.info.userPic
                : defaultAvatar
            "
          />
          <el-icon class="el-icon--right">
            <ArrowDown />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="info" :icon="User"
              >基本资料</el-dropdown-item
            >
            <el-dropdown-item command="updatePwd" :icon="Edit"
              >更改密码</el-dropdown-item
            >
            <el-dropdown-item command="avater" :icon="Crop"
              >更换头像</el-dropdown-item
            >
            <el-dropdown-item command="logout" :icon="SwitchButton"
              >退出登录</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-menu-item>
  </el-menu>
</template>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
.el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
:deep(.el-tooltip__trigger:focus-visible) {
  outline: unset;
}
</style>
