<script lang="ts" setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";

import defaultAvatar from "@/assets/images/default_avatar.jpg";

// # 个人信息 token
import { useUserInfoStore } from "@/stores/userinfo";
import { useTokenStore } from "@/stores/token";

const userInfoStore = useUserInfoStore();
const tokenStore = useTokenStore();

const imageUrl = ref(userInfoStore.info.userPic);

// 头像上传成功后
import type { UploadProps } from "element-plus";

const handleAvatarSuccess: UploadProps["onSuccess"] = (
  response,
  uploadFile
) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw!);
  // 更新 userInfoStore 里的 头像地址
  userInfoStore.info.userPic = response.data;
};

// 头像上传之前
const beforeAvatarUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.type !== "image/jpeg") {
    ElMessage.error("Avatar picture must be JPG format!");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("Avatar picture size can not exceed 2MB!");
    return false;
  }
  return true;
};
</script>

<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>更换头像</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-upload
          ref="uploadRef"
          class="avatar-uploader"
          :show-file-list="false"
          :auto-upload="true"
          action="/api/user/update-avatar"
          name="avatar"
          :headers="{ Authorization: tokenStore.token }"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar" />
          <img v-else :src="defaultAvatar" class="avatar" />
        </el-upload>
        <br />
        <p style="font-size: large; font-weight: 100;">请点击头像更新</p>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped>
.avatar-uploader .avatar {
  width: 250px;
  height: 250px;
  display: block;
  border-radius: 50%;
}
</style>
