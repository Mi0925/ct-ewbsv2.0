package cn.comtom.reso.main.ebr.service;

import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;

public interface IEbrChannelService extends BaseService<EbrChannel, String> {

	
	public String getChannelByEbrId(String ebrId);
}
