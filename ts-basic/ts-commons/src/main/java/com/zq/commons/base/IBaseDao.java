package com.zq.commons.base;

/**
 * @author ZQ
 */
public interface IBaseDao<T> {
    /**
     * 根据主键删除数据
     * @param id
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入数据
     * @param record
     * @return 影响行数
     */
    int insert(T record);

    /**
     * 动态插入数据
     * @param record
     * @return 影响行数
     */
    int insertSelective(T record);

    /**
     * 根据主键查询
     * @param id
     * @return T
     */
    T selectByPrimaryKey(Long id);

    /**
     * 动态根据主键更新数据
     * @param record
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据主键更新数据
     * @param record
     * @return 影响行数
     */
    int updateByPrimaryKey(T record);
}
