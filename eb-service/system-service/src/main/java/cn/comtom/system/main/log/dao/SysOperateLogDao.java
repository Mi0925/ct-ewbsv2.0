package cn.comtom.system.main.log.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.log.entity.dbo.SysOperateLog;
import cn.comtom.system.main.log.mapper.SysOperateLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SysOperateLogDao extends BaseDao<SysOperateLog,String> {

    @Autowired
    private SysOperateLogMapper mapper;

    @Override
    public SystemMapper<SysOperateLog, String> getMapper() {
        return mapper;
    }



}
