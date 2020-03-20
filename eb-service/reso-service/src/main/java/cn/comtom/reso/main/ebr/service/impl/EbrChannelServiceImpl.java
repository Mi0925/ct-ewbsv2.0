package cn.comtom.reso.main.ebr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrChannelDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.service.IEbrChannelService;

@Service
public class EbrChannelServiceImpl extends BaseServiceImpl<EbrChannel,String> implements IEbrChannelService {
	
	@Autowired
    private EbrChannelDao ebrChannelDao;
	
	@Override
	public BaseDao<EbrChannel, String> getDao() {
		
		return ebrChannelDao;
	}

	@Override
	public String getChannelByEbrId(String ebrId) {
		
		return ebrChannelDao.getChannelByEbrId(ebrId);
	}

}
