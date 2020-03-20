package cn.comtom.system.main.plan.dao;

import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.plan.entity.dbo.SysPlanMatch;
import cn.comtom.system.main.plan.mapper.SysPlanMatchMapper;
import cn.comtom.tools.enums.SysDictCodeEnum;
import cn.comtom.tools.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class SysPlanMatchDao extends BaseDao<SysPlanMatch,String> {

    @Autowired
    private SysPlanMatchMapper mapper;


    @Override
    public SystemMapper<SysPlanMatch, String> getMapper() {
        return mapper;
    }



    public List<SysPlanMatchInfo> page(SysPlanPageRequest req) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(req);
        map.put("flowDictGroupCode",SysDictCodeEnum.PLAN_MATH_FLOW.getCode()); //关联数据字典查询
        return mapper.queryList(map);
    }

    public List<SysPlanMatchInfo> getMatchedPlan(String[] severityArr, String eventType, String srcEbrIds,  String[] areaCodes) {
        Map<String , Object> map = new HashMap<>();
        if(severityArr != null && severityArr.length>0) {
        	map.put("severities",severityArr);
        }
        if(areaCodes != null && areaCodes.length>0) {
        	map.put("areaCodes",areaCodes);
        }
        map.put("srcEbrIds", srcEbrIds);
        map.put("eventType",eventType);
        return mapper.getMatchedPlan(map);
    }
}
