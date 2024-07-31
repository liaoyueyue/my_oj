import request from "@/utils/request.js";

// 题目合集列表查询
export const collectionListService = (currentPage, pageSize, searchForm) => {
  let conditionData = {
    pageNum: currentPage,
    pageSize: pageSize,
    collectionName: searchForm.collectionName,
  };;
  return request.get("/collection/list", { params: conditionData });
};

// 题目合集添加
export const addCollectionService = (collectionData) => {
  return request.post("/collection/add", collectionData);
};

// 题目合集编辑
export const updateCollectionService = (collectionData) => {
  return request.put("/collection/update", collectionData);
};

// 题目合集删除
export const deleteCollectionService = (id) => {
  return request.delete("/collection/delete?id=" + id);
};
