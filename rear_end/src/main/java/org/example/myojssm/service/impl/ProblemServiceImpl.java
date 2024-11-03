package org.example.myojssm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.myojssm.common.Result;
import org.example.myojssm.entity.PageBean;
import org.example.myojssm.entity.Problem;
import org.example.myojssm.entity.vo.ProblemVo;
import org.example.myojssm.mapper.ProblemMapper;
import org.example.myojssm.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-02-27
 * Time: 23:39
 */

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public List<Problem> queryAllProblem() {
        return problemMapper.queryAllProblem();
    }

    @Override
    public Problem queryProblemById(Integer id) {
        return problemMapper.queryProblemById(id);
    }

    @Override
    public Boolean addProblem(Problem problem) {
        return problemMapper.insertProblem(problem) > 0;
    }

    @Override
    public PageBean<ProblemVo> getProblemList(Integer pageNum, Integer pageSize, String collectionName, String level) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProblemVo> problems = problemMapper.queryProblemListByColAndLevel(collectionName, level);
        Page<ProblemVo> page = (Page<ProblemVo>) problems;
        return new PageBean<>(page.getTotal(), page.getResult());
    }

    @Override
    public Boolean deleteProblem(int id) {
        if (id <= 0) {
            return false;
        }
        return problemMapper.deleteProblemById(id) > 0;
    }

    @Override
    public Boolean updateProblem(Problem problem) {
        return problemMapper.updateProblemById(problem) > 0;
    }
}
