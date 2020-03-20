package cn.comtom.system.main.sequence.service;

import cn.comtom.domain.system.sequence.info.SysSequenceInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.sequence.entity.dbo.SysSequence;

public interface ISysSequenceService extends BaseService<SysSequence,String> {
    SysSequenceInfo getValue(String name);

}
