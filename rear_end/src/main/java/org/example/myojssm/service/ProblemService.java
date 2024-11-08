package org.example.myojssm.service;

import org.example.myojssm.entity.PageBean;
import org.example.myojssm.entity.Problem;
import org.example.myojssm.entity.vo.ProblemVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-03-23
 * Time: 13:29
 */
public interface ProblemService {
    /**
     * 查询所有题目列表
     * @return 题目列表
     */
    List<Problem> queryAllProblem();

    /**
     * 查询题目使用编号
     * @param id 编号
     * @return 对应编号的题目
     */
    Problem queryProblemById(Integer id);

    /**
     * 添加题目
     * @param problem 题目实体
     * @return 是否添加成功
     */
    Boolean addProblem(Problem problem);

    /**
     * 查询不同类别的题目库
     *
     * @param pageNum    页码
     * @param pageSize   一页题目大小
     * @param collectionName     题目合集名称
     * @param level      题目等级
     * @return 题目分页实体
     */
    PageBean<ProblemVo> getProblemList(Integer pageNum, Integer pageSize, String collectionName, String level);

    /**
     * 删除题目 通过编号
     * @param id 编号
     * @return 是否删除成功
     */
    Boolean deleteProblem(int id);

    /**
     * 更新题目
     * @param problem 题目实体类
     * @return 是否更新成功
     */
    Boolean updateProblem(Problem problem);
}
