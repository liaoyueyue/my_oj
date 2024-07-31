import "./assets/main.css";
import "element-plus/dist/index.css";

import ElementPlus from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";

import { createPinia } from "pinia";
import { createPersistedState } from "pinia-persistedstate-plugin";

import Router from "@/router";
import { createApp } from "vue";
import App from "./App.vue";

const app = createApp(App);
const Pinia = createPinia();
const Persist = createPersistedState();

Pinia.use(Persist);

app.use(ElementPlus, {
  locale: zhCn,
});
app.use(Pinia);
app.use(Router);

app.mount("#app");

