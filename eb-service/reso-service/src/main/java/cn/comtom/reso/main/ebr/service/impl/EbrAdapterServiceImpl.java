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

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterWhereRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrAdapterDao;
import cn.comtom.reso.main.ebr.dao.EbrChannelDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;
import cn.comtom.reso.main.ebr.mapper.EbrChannelMapper;
import cn.comtom.reso.main.ebr.service.IEbrAdapterService;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.ResTypeDictEnum;

@Service
public class EbrAdapterServiceImpl extends BaseServiceImpl<EbrAdapter,String> implements IEbrAdapterService {

    @Autowired
    private EbrAdapterDao dao;

    @Autowired
    private EbrChannelDao ebrChannelDao;

    @Override
    public BaseDao<EbrAdapter, String> getDao() {
        return dao;
    }

    @Override
    public List<EbrAdapterInfo> findAdapterListByWhere(AdapterWhereRequest request) {
        List<EbrAdapter> ebrTerminalList = dao.findAdapterListByWhere(request);
        return Optional.ofNullable(ebrTerminalList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebrTerminal -> {
                	EbrAdapterInfo ebrTerminalInfo = new EbrAdapterInfo();
                    BeanUtils.copyProperties(ebrTerminal,ebrTerminalInfo);
                    return ebrTerminalInfo;
                }).collect(Collectors.toList());
    }

	@Override
	public List<EbrAdapterInfo> findListByAdapterIds(List<String> adapterIds) {

		return dao.findListByAdapterIds(adapterIds);
	}

	@Override
	public List<EbrAdapterInfo> getList(EbrAdapterPageRequest request) {

		return dao.getList(request);
	}

	@Override
	public int save(EbrAdapter t, String channel) {
		setEbrChannel(t,channel);
		return dao.saveAdapter(t);
	}

	@Override
	public int update(EbrAdapter t, String channel) {
		setEbrChannel(t,channel);
		return super.update(t);
	}

	private void setEbrChannel(EbrAdapter t,String channel) {
		if(StringUtils.isBlank(channel)) {
			return;
		}
		String channelByEbrId = ebrChannelDao.getChannelByEbrId(t.getEbrAsId());
		EbrChannel ebrChannel = new EbrChannel();
		ebrChannel.setEbrId(t.getEbrAsId());
		ebrChannel.setSendChannel(channel);
		if(StringUtils.isBlank(channelByEbrId)) {
			ebrChannelDao.save(ebrChannel);
		}else {
			ebrChannelDao.updateByEbrId(ebrChannel);
		}
	}

}
