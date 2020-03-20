package cn.comtom.system.main.sequence.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.sequence.entity.dbo.SysSequence;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysSequenceMapper extends SystemMapper<SysSequence,String> {
}
