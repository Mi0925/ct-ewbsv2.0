package cn.comtom.system.main.log.service.impl;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.log.dao.SysOperateLogDao;
import cn.comtom.system.main.log.entity.dbo.SysOperateLog;
import cn.comtom.system.main.log.service.ISysOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysOperateLogServiceImpl extends BaseServiceImpl<SysOperateLog,String> implements ISysOperateLogService {

    @Autowired
    private SysOperateLogDao accessNodeDao;

    @Override
    public BaseDao<SysOperateLog, String> getDao() {
        return accessNodeDao;
    }

}
