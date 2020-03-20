package cn.comtom.core.main.flow.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.flow.entity.dbo.DispatchFlow;
import cn.comtom.core.main.flow.mapper.FlowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DispatchFlowDao extends BaseDao<DispatchFlow,String> {

    @Autowired
    private FlowMapper mapper;


    @Override
    public CoreMapper<DispatchFlow, String> getMapper() {
        return mapper;
    }



}
