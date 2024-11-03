package org.example.myojssm.service;

import org.example.myojssm.entity.Collection;
import org.example.myojssm.entity.PageBean;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-03-31
 * Time: 12:42
 */
public interface CollectionService {
    /**
     * 新增一个合集
     *
     * @param collection 合集实体
     * @return 是否成功添加
     */
    Boolean addCollection(Collection collection);

    /**
     * 获取合集列表
     *
     * @param pageNum        页码
     * @param pageSize       一页题目大小
     * @param collectionName 合集名称
     * @return 符合条件合集列表
     */
    PageBean<Collection> getCollectionList(Integer pageNum, Integer pageSize, String collectionName);

    /**
     * 查询合集-使用编号
     *
     * @param id 合集编号
     * @return 编号对应合集
     */
    Collection getCollectionById(int id);

    /**
     * 更新合集-使用编号
     *
     * @param collection 合集实体
     * @return 是否更新成功
     */
    Boolean updateCollection(Collection collection);


    /**
     * 通过 编号 删除合集
     *
     * @param id 合集编号
     * @return 是否删除成功
     */
    Boolean deleteCollection(int id);
}
