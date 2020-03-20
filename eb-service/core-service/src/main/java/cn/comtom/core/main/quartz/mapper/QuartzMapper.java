package cn.comtom.core.main.quartz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.core.quartz.QuartzInfo;
import cn.comtom.domain.core.quartz.QuartzPageRequest;

@Mapper
@Repository
public interface QuartzMapper {

    List<QuartzInfo> getQuartzInfoList(QuartzPageRequest request);

}
