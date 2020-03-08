package com.zq.commons.base;

/**
 * @author ZQ
 * @Date 2020/2/10
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T> {

    /**
     * 获取Dao对象
     * @return Dao对象
     */
    public abstract IBaseDao<T> getBaseDao();

    public int deleteByPrimaryKey(Long id) {
        return getBaseDao().deleteByPrimaryKey(id);
    }

    public int insert(T record) {
        return getBaseDao().insert(record);
    }

    public int insertSelective(T record) {
        return getBaseDao().insertSelective(record);
    }

    public T selectByPrimaryKey(Long id) {
        return getBaseDao().selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(T record) {
        return getBaseDao().updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        return getBaseDao().updateByPrimaryKey(record);
    }
}
