package cn.comtom.reso.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.domain.reso.ebr.request.TerminalConditionRequest;
import cn.comtom.domain.reso.ebr.request.TerminalWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrChannelDao;
import cn.comtom.reso.main.ebr.dao.EbrTerminalDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.entity.dbo.EbrPlatform;
import cn.comtom.reso.main.ebr.entity.dbo.EbrTerminal;
import cn.comtom.reso.main.ebr.service.IEbrTerminalService;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.ResTypeDictEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EbrTerminalServiceImpl extends BaseServiceImpl<EbrTerminal,String> implements IEbrTerminalService {

    @Autowired
    private EbrTerminalDao dao;

    @Autowired
    private EbrChannelDao ebrChannelDao;

    @Override
    public BaseDao<EbrTerminal, String> getDao() {
        return dao;
    }

    @Override
    public List<EbrTerminalInfo> getList(EbrTerminalPageRequest request) {
        return dao.getList(request);
    }

    @Override
    public List<EbrTerminalInfo> findListByTremIds(List<String> devTrmIds) {
        return dao.findListByTremIds(devTrmIds);
    }

    @Override
    public List<EbrTerminalInfo> findTerminalListByWhere(TerminalWhereRequest request) {
        List<EbrTerminal> ebrTerminalList = dao.findTerminalListByWhere(request);
        return Optional.ofNullable(ebrTerminalList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebrTerminal -> {
                    EbrTerminalInfo ebrTerminalInfo = new EbrTerminalInfo();
                    BeanUtils.copyProperties(ebrTerminal,ebrTerminalInfo);
                    return ebrTerminalInfo;
                }).collect(Collectors.toList());
    }

    @Override
	public int save(EbrTerminal t) {
		ebrChannelDao.save(setAndGetEbrChannel(t));
		return dao.saveOneTerminal(t);
	}

	private EbrChannel setAndGetEbrChannel(EbrTerminal t) {
		EbrChannel ebrChannel = new EbrChannel();
		ebrChannel.setEbrId(t.getTerminalEbrId());
		ebrChannel.setSendChannel(CommonDictEnum.EBR_CHANNEL_1.getKey());
		return ebrChannel;
	}

	@Override
	public Integer countTerminalByCondition(TerminalConditionRequest terminalConditionRequest) {
		return dao.countTerminalByCondition(terminalConditionRequest);
	}
}
