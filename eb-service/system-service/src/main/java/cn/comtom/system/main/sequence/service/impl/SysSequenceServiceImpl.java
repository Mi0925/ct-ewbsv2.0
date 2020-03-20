package cn.comtom.system.main.sequence.service.impl;

import cn.comtom.domain.system.sequence.info.SysSequenceInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.sequence.dao.SysSequenceDao;
import cn.comtom.system.main.sequence.entity.dbo.SysSequence;
import cn.comtom.system.main.sequence.service.ISysSequenceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysSequenceServiceImpl extends BaseServiceImpl<SysSequence,String> implements ISysSequenceService {

    @Autowired
    private SysSequenceDao sequenceDao;

    @Override
    public BaseDao<SysSequence, String> getDao() {
        return sequenceDao;
    }

    @Override
    public SysSequenceInfo getValue(String name) {
        SysSequence sequence = sequenceDao.getValue(name);

        SysSequenceInfo info = null;
        if (sequence != null) {
            info = new SysSequenceInfo();
            BeanUtils.copyProperties(sequence,info);
        }
        return info;
    }


}
