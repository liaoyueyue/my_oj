import request from "@/utils/request.js";

// 题目列表查询
export const problemListService = (currentPage, pageSize, searchForm) => {
  let conditionData = {
    pageNum: currentPage,
    pageSize: pageSize,
    collectionName: searchForm.collectionName,
    level: searchForm.level,
  };
  return request.get("/problem/list", { params: conditionData });
};

// 添加题目
export const addProblemService = (problemData) => {
  return request.post("/problem/add", problemData); 
}

// 编辑题目
export const updateProblemService = (problemData) => {
  return request.put("/problem/update", problemData);
}

// 删除题目
export const deleteProblemService = (id) => {
  return request.delete("/problem/delete?id=" + id);
}

// 查询题目
export const queryProblemService = (id) => {
  return request.get("/problem/detail?id=" + id);
}