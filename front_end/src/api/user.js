import request from "@/utils/request.js";

// 用户注册
export const userRegisterService = (registerData) => {
  const params = new URLSearchParams();
  for (let key in registerData) {
    params.append(key, registerData[key]);
  }
  return request.post("/user/register", params);
};

// 用户登录
export const userLoginService = (loginData) => {
  const params = new URLSearchParams();
  for (let key in loginData) {
    params.append(key, loginData[key]);
  }
  return request.post("/user/login", params);
};

// 获取用户详细信息
export const userInfoService = () => {
  return request.get("/user/userinfo");
};

// 用户信息修改
export const userInfoUpdateService = (userInfoData) => {
  return request.put("/user/update-info", userInfoData);
};

// 用户修改密码
export const userUpdatePwd = (updatePwdData) => {
  const params = new URLSearchParams();
  for (let key in updatePwdData) {
    params.append(key, updatePwdData[key]);
  }
  return request.patch("/user/update-pwd", params);
};
