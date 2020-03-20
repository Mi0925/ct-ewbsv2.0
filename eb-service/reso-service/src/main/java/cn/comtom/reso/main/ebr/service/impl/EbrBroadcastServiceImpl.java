package cn.comtom.reso.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.domain.reso.ebr.request.BroadcastWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrBroadcastDao;
import cn.comtom.reso.main.ebr.dao.EbrChannelDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;
import cn.comtom.reso.main.ebr.entity.dbo.EbrBroadcast;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;
import cn.comtom.reso.main.ebr.service.IEbrBroadcastService;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.ResTypeDictEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EbrBroadcastServiceImpl extends BaseServiceImpl<EbrBroadcast,String> implements IEbrBroadcastService {

    @Autowired
    private EbrBroadcastDao dao;

    @Autowired
    private EbrChannelDao ebrChannelDao;
    
    @Override
    public BaseDao<EbrBroadcast, String> getDao() {
        return dao;
    }

    @Override
    public List<EbrBroadcastInfo> getMatchBroadcast(String areaCode, String relatedPsEbrId) {
        List<EbrBroadcast> ebrBroadcastList = dao.getMatchBroadcast(areaCode,relatedPsEbrId);
        return Optional.ofNullable(ebrBroadcastList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebrBroadcast -> {
            EbrBroadcastInfo ebrBroadcastInfo = new EbrBroadcastInfo();
            BeanUtils.copyProperties(ebrBroadcast,ebrBroadcastInfo);
            return ebrBroadcastInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbrBroadcastInfo> getList(BroadcastPageRequest request) {
        return dao.getList(request);
    }

    @Override
    public List<EbrBroadcastInfo> findBroadcastListByWhere(BroadcastWhereRequest request) {
        List<EbrBroadcast> ebrBroadcastList = dao.findBroadcastListByWhere(request);
        return Optional.ofNullable(ebrBroadcastList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebrBroadcast -> {
                    EbrBroadcastInfo ebrBroadcastInfo = new EbrBroadcastInfo();
                    BeanUtils.copyProperties(ebrBroadcast,ebrBroadcastInfo);
                    return ebrBroadcastInfo;
                }).collect(Collectors.toList());
    }

	@Override
	public int save(EbrBroadcast t,String channel) {
		setEbrChannel(t,channel);
		return super.save(t);
	}
	
	@Override
	public int update(EbrBroadcast t,String channel) {
		setEbrChannel(t,channel);
		return super.update(t);
	}
    
	private void setEbrChannel(EbrBroadcast t,String channel) {
		if(StringUtils.isBlank(channel)) {
			return;
		}
		String channelByEbrId = ebrChannelDao.getChannelByEbrId(t.getBsEbrId());
		EbrChannel ebrChannel = new EbrChannel();
		ebrChannel.setEbrId(t.getBsEbrId());
		ebrChannel.setSendChannel(channel);
		if(StringUtils.isBlank(channelByEbrId)) {
			ebrChannelDao.save(ebrChannel);
		}else {
			ebrChannelDao.updateByEbrId(ebrChannel);
		}
	}
}
