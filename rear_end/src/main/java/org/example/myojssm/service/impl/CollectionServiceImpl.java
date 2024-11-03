package org.example.myojssm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.myojssm.common.Result;
import org.example.myojssm.entity.Collection;
import org.example.myojssm.entity.PageBean;
import org.example.myojssm.mapper.CollectionMapper;
import org.example.myojssm.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-03-31
 * Time: 12:42
 */
@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public Boolean addCollection(Collection collection) {
        return collectionMapper.insertCollection(collection) > 0;
    }

    @Override
    public PageBean<Collection> getCollectionList(Integer pageNum, Integer pageSize, String collectionName) {
        PageHelper.startPage(pageNum, pageSize);
        List<Collection> collections = collectionMapper.queryCollectionListByName(collectionName);
        Page<Collection> page = (Page<Collection>) collections;
        return new PageBean<>(page.getTotal(), page.getResult());
    }

    @Override
    public Collection getCollectionById(int id) {
        return collectionMapper.queryCollectionById(id);
    }

    @Override
    public Boolean updateCollection(Collection collection) {
        return collectionMapper.updateCollectionById(collection) > 0;
    }

    @Override
    public Boolean deleteCollection(int id) {
        return collectionMapper.deleteCollectionById(id) > 0;
    }
}
