package cn.comtom.system.main.sequence.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.sequence.entity.dbo.SysSequence;
import cn.comtom.system.main.sequence.mapper.SysSequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SysSequenceDao extends BaseDao<SysSequence,String> {

    @Autowired
    private SysSequenceMapper mapper;

    @Override
    public SystemMapper<SysSequence, String> getMapper() {
        return mapper;
    }

    public SysSequence getValue(String name) {
        SysSequence sequence = new SysSequence();
        sequence.setName(name);
        return mapper.selectOne(sequence);
    }

    public void updateById(SysSequence sequence) {

    }
}
