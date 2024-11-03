package org.example.myojssm.controller;

import jakarta.validation.constraints.NotNull;
import org.example.myojssm.common.Result;
import org.example.myojssm.entity.PageBean;
import org.example.myojssm.entity.Problem;
import org.example.myojssm.entity.vo.ProblemVo;
import org.example.myojssm.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Created with IntelliJ IDEA.
 * Description: 题目服务层
 * User: liaoyueyue
 * Date: 2024-02-27
 * Time: 23:44
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private ProblemService problemservice;

    @PostMapping("/add")
    public Result<Void> addProblem(@RequestBody @Validated(Problem.Add.class) Problem problem) {
        Boolean isAdd = problemservice.addProblem(problem);
        if (isAdd) {
            return Result.success();
        } else {
            return Result.error("题目添加错误");
        }
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteProblem(@RequestParam @NotNull int id) {
        Boolean isDel = problemservice.deleteProblem(id);
        if (isDel) {
            return Result.success();
        } else {
            return Result.error("题目删除错误");
        }
    }

    @PutMapping("/update")
    public Result<Void> updateProblem(@RequestBody @Validated(Problem.Update.class) Problem problem) {
        Boolean isUpdate = problemservice.updateProblem(problem);
        if (isUpdate) {
            return Result.success();
        } else {
            return Result.error("题目更新错误");
        }
    }

    @GetMapping("/list")
    public Result<PageBean<ProblemVo>> showProblemListByCollectionOrLevel(@NotNull Integer pageNum, @NotNull Integer pageSize, @RequestParam(required = false) String collectionName, @RequestParam(required = false) String level) {
        if (pageNum < 1 || pageSize > 20) {
            return Result.error();
        }
        PageBean<ProblemVo> problemList = problemservice.getProblemList(pageNum, pageSize, collectionName, level);
        if (problemList != null) {
            return Result.success(problemList);
        } else {
            return Result.error("题目列表获取错误");
        }
    }

    @GetMapping("/detail")
    public Result<Problem> showProblemById(@RequestParam @NotNull int id) {
        Problem problem = problemservice.queryProblemById(id);
        if (problem != null) {
            return Result.success(problem);
        } else {
            return Result.error("题目详情获取错误");
        }
    }
}
