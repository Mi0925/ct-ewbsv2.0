package cn.comtom.core.fw;

import java.util.List;
import java.util.Map;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 2:06
 * .sql.xml需要有对应的sql方法
 */
public interface BaseMapper<T,PK> {

    List<T> queryList(Map<String,Object> map);
}
