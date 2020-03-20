package cn.comtom.core.main.quartz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.core.main.quartz.mapper.QuartzMapper;
import cn.comtom.core.main.quartz.service.IQuartzService;
import cn.comtom.domain.core.quartz.QuartzInfo;
import cn.comtom.domain.core.quartz.QuartzPageRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuartzServiceImpl  implements IQuartzService {

    @Autowired
    private QuartzMapper quartzMapper;

	
	@Override 
	public List<QuartzInfo> findQuartzInfoList(QuartzPageRequest request) { 
		
		return quartzMapper.getQuartzInfoList(request); 
	}
	 
}
