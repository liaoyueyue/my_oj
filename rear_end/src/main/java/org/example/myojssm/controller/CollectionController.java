package org.example.myojssm.controller;

import jakarta.validation.constraints.NotNull;
import org.example.myojssm.common.Result;
import org.example.myojssm.entity.Collection;
import org.example.myojssm.entity.PageBean;
import org.example.myojssm.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-03-31
 * Time: 12:39
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @PostMapping("/add")
    public Result<Void> addCollection(@RequestBody @Validated(Collection.Add.class) Collection collection) {
        Boolean isAdd = collectionService.addCollection(collection);
        if (isAdd) {
            return Result.success();
        } else {
            return Result.error("合集添加错误");
        }
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteCollection(@RequestParam @NotNull int id) {
        Boolean isDel = collectionService.deleteCollection(id);
        if (isDel) {
            return Result.success();
        } else {
            return Result.error("删除失败，请检查合集编号");
        }
    }

    @PutMapping("/update")
    public Result<Void> updateCollection(@RequestBody @Validated(Collection.Update.class) Collection collection) {
        Boolean isUpdate = collectionService.updateCollection(collection);
        if (isUpdate) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @GetMapping("/list")
    public Result<PageBean<Collection>> getCollectionList(@NotNull Integer pageNum, @NotNull Integer pageSize, @RequestParam(required = false) String collectionName) {
        if (pageNum < 1 || pageSize > 20) {
            return Result.error();
        }
        PageBean<Collection> collectionList = collectionService.getCollectionList(pageNum, pageSize, collectionName);
        if (collectionList != null) {
            return Result.success(collectionList);
        } else {
            return Result.error("合集列表获取错误");
        }
    }
}
