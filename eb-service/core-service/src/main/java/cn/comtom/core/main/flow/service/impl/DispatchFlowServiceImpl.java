package cn.comtom.core.main.flow.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.flow.dao.DispatchFlowDao;
import cn.comtom.core.main.flow.entity.dbo.DispatchFlow;
import cn.comtom.core.main.flow.service.IDispatchFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispatchFlowServiceImpl extends BaseServiceImpl<DispatchFlow,String> implements IDispatchFlowService {

    @Autowired
    private DispatchFlowDao dao;

    @Override
    public BaseDao<DispatchFlow, String> getDao() {
        return dao;
    }

}
