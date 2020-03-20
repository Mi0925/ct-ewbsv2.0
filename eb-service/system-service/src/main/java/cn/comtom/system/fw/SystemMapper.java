package cn.comtom.system.fw;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

/**
 * 持久化接口基类
 * @param <T>
 * @param <PK>
 */
public interface SystemMapper<T,PK> extends Mapper<T>, InsertUseGeneratedKeysMapper<T>, InsertListMapper<T>, IdListMapper<T,PK> {
}