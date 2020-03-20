package cn.comtom.reso.fw;

import java.util.List;

public interface BaseService<T, PK> {

    int save(T t);

    int saveList(List<T> ts);

    int update(T t);

    T selectById(PK id);

    List<T> selectByIds(List<PK> ids);

    List<T> selectAll();

    int deleteById(PK id);

    int delete(T t);

    int deleteByIds(List<PK> id);

}
