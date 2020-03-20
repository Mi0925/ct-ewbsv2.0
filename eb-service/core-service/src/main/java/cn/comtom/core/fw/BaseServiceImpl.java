package cn.comtom.core.fw;

import java.io.Serializable;
import java.util.List;

/**
 * 基础服务实现类
 * @author guomao
 * @param <T>
 * @param <PK>
 */
public abstract class BaseServiceImpl<T extends Serializable, PK> implements BaseService<T,PK>{

    public abstract BaseDao<T, PK> getDao();

    @Override
    public int save(T t){
        return getDao().save(t);
    }

    @Override
    public int saveList(List<T> list){
        return getDao().saveList(list);
    }

    @Override
    public int update(T t){
        return getDao().update(t);
    }

    @Override
    public T selectById(PK id){
        return getDao().selectById(id);
    }

    @Override
    public List<T> selectByIds(List<PK> ids){
        return getDao().selectByIds(ids);
    }

    @Override
    public List<T> selectAll(){
        return getDao().selectAll();
    }

    @Override
    public int delete(T t){
        return getDao().delete(t);
    }

    @Override
    public int deleteById(PK id){
        return getDao().deleteById(id);
    }

    @Override
    public int deleteByIds(List<PK> ids){
        return getDao().deleteByIds(ids);
    }

}
