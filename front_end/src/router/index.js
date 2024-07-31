import { createRouter, createWebHistory } from "vue-router";
// 1. 定义路由组件.
import LoginView from "@/views/Login.vue";
import ManageView from "@/views/Manage.vue";
import IndexView from "@/views/Index.vue";
import PracticeView from "@/views/Practice.vue";

import ProblemCollection from "@/views/problem/ProblemCollection.vue";
import ProblemManage from "@/views/problem/ProblemManage.vue";
import UserAvater from "@/views/user/UserAvater.vue";
import UserInfo from "@/views/user/UserInfo.vue";
import UserUpdatePwd from "@/views/user/UserUpdatePwd.vue";
import Developing from "@/views/template/developing.vue";

// 2. 定义路由
const routes = [
  { path: "/", redirect: "/index" },
  { path: "/login", component: LoginView },
  { path: "/index", component: IndexView },
  { path: "/practice/:id", component: PracticeView },
  {
    path: "/manage",
    component: ManageView,
    redirect: "/manage/problem/manage",
    children: [
      {
        path: "problem",
        children: [
          { path: "collection", component: ProblemCollection },
          { path: "manage", component: ProblemManage },
        ],
      },
      {
        path: "user",
        children: [
          { path: "avater", component: UserAvater },
          { path: "info", component: UserInfo },
          { path: "updatePwd", component: UserUpdatePwd },
        ],
      },
      { path: "developing", component: Developing },
    ],
  },
];

// 3. 创建路由实例并传递 `routes` 配置
const router = createRouter({
  history: createWebHistory(),
  routes, // `routes: routes` 的缩写
});

// 4. 导出路由
export default router;
