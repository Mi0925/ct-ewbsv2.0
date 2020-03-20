package cn.comtom.reso.main.ebr.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrChannelDao;
import cn.comtom.reso.main.ebr.dao.EbrStationDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrBroadcast;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.entity.dbo.EbrPlatform;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;
import cn.comtom.reso.main.ebr.service.IEbrStationService;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.ResTypeDictEnum;

@Service
public class EbrStationServiceImpl extends BaseServiceImpl<EbrStation,String> implements IEbrStationService {

    @Autowired
    private EbrStationDao dao;

    @Autowired
    private EbrChannelDao ebrChannelDao;

    @Override
    public BaseDao<EbrStation, String> getDao() {
        return dao;
    }

    @Override
    public List<EbrStationInfo> findStationListByWhere(StationWhereRequest request) {
        List<EbrStation> ebrTerminalList = dao.findStationListByWhere(request);
        return Optional.ofNullable(ebrTerminalList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebrTerminal -> {
                	EbrStationInfo ebrTerminalInfo = new EbrStationInfo();
                    BeanUtils.copyProperties(ebrTerminal,ebrTerminalInfo);
                    return ebrTerminalInfo;
                }).collect(Collectors.toList());
    }

	@Override
	public List<EbrStationInfo> findListByStationIds(List<String> stationIds) {

		return dao.findListByStationIds(stationIds);
	}

	@Override
	public List<EbrStationInfo> getList(EbrStationPageRequest request) {
		return dao.getList(request);
	}

	@Override
	public int save(EbrStation t,String channel) {
		setEbrChannel(t,channel);
		return dao.saveStation(t);
	}

	@Override
	public int update(EbrStation t,String channel) {
		setEbrChannel(t,channel);
		return super.update(t);
	}

	private void setEbrChannel(EbrStation t,String channel) {
		if(StringUtils.isBlank(channel)) {
			return;
		}
		String channelByEbrId = ebrChannelDao.getChannelByEbrId(t.getEbrStId());
		EbrChannel ebrChannel = new EbrChannel();
		ebrChannel.setEbrId(t.getEbrStId());
		ebrChannel.setSendChannel(channel);
		if(StringUtils.isBlank(channelByEbrId)) {
			ebrChannelDao.save(ebrChannel);
		}else {
			ebrChannelDao.updateByEbrId(ebrChannel);
		}
	}
}
