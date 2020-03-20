package cn.comtom.core.main.quartz.service;

import java.util.List;

import cn.comtom.domain.core.quartz.QuartzInfo;
import cn.comtom.domain.core.quartz.QuartzPageRequest;

public interface IQuartzService {

	public List<QuartzInfo> findQuartzInfoList(QuartzPageRequest request);
	
}
