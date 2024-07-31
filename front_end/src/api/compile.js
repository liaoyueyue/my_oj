import request from "@/utils/request.js";

// 题目编译
export const compileProblemService = (compileData) => {
  const params = new URLSearchParams();
  for (let key in compileData) {
    params.append(key, compileData[key]);
  }
  return request.post("/compile", params);
};
