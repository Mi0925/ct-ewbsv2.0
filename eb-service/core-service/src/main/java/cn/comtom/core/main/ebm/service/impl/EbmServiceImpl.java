package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmDao;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatch;
import cn.comtom.core.main.ebm.service.IEbmDispatchService;
import cn.comtom.core.main.ebm.service.IEbmService;
import cn.comtom.core.main.utils.SequenceGenerate;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.utils.DateUtil;
import cn.comtom.tools.utils.UUIDGenerator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EbmServiceImpl extends BaseServiceImpl<Ebm,String> implements IEbmService {

    @Autowired
    private EbmDao dao;

    @Autowired
    private IEbmDispatchService ebmDispatchService;
    
    @Autowired
    private SequenceGenerate sequenceGenerate;
    
    @Override
    public BaseDao<Ebm, String> getDao() {
        return dao;
    }

    @Override
    public List<EbmInfo> getDispatchEbm(String ebmState) {
        List<Ebm> ebmList = dao.getDispatchEbm(ebmState);
        return Optional.ofNullable(ebmList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebm -> {
            EbmInfo ebmInfo = new EbmInfo();
            BeanUtils.copyProperties(ebm,ebmInfo);
            return ebmInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbmInfo> list(EbmPageRequest request) {
        List<Ebm> ebmList = dao.list(request);
        return Optional.ofNullable(ebmList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebm -> {
            EbmInfo ebmInfo = new EbmInfo();
            BeanUtils.copyProperties(ebm,ebmInfo);
            return ebmInfo;
        }).collect(Collectors.toList());
    }

	@Override
	public Boolean cancelEbmPlay(String ebmid) {
		Ebm ebm = this.selectByRelatedEbmId(ebmid);
        //创建一条用于取消播发的EBM消息
        Ebm ebmAdd = new Ebm();
        BeanUtils.copyProperties(ebm,ebmAdd);
        String newebmId = ebm.getEbmId().substring(0,22)+ DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD)+sequenceGenerate.createId(Constants.EBM_SEQUENCE_ID);
        ebmAdd.setEbmId(newebmId);
        ebmAdd.setRelatedEbmId(ebm.getEbmId());                          //设置关联的EBM消息为需要取消的EBM消息
        ebmAdd.setMsgType(TypeDictEnum.EBM_MSG_TYPE_CANCEL.getKey());     //设置消息类型为取消播发
        ebmAdd.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());     //设置消息状态为创建
        this.save(ebmAdd);
        Ebm ebmUpdate = new Ebm();
        ebmUpdate.setEbmId(ebmid);
        ebmUpdate.setBroadcastState(5);
        this.update(ebmUpdate);
        //不经过生成方案，预案匹配之类的流程，直接生成待分发的消息（ebmDispatch）
        List<cn.comtom.domain.core.ebm.info.EbmDispatchInfo> ebmDispatchInfoList = ebmDispatchService.getEbmDispatchByEbmId(ebmid);
        List<EbmDispatch> ebmDispatchList = Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebmDispatchInfo -> {
                    EbmDispatch ebmDispatch = new EbmDispatch();
                    BeanUtils.copyProperties(ebmDispatchInfo,ebmDispatch);
                    ebmDispatch.setDispatchId(UUIDGenerator.getUUID());
                    ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());    //分发状态为取消
                    ebmDispatch.setEbmId(newebmId);             //关联取消播发的EBM消息
                    ebmDispatch.setFailCount(0);
                    ebmDispatch.setMatchEbdId(null);
                    return ebmDispatch;
                }).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(ebmDispatchInfoList)) {
        	ebmDispatchService.saveList(ebmDispatchList);
        }
		return true;
	}

	@Override
	public Ebm selectByRelatedEbmId(String ebmid) {
		Ebm condition = new Ebm();
		condition.setRelatedEbmId(ebmid);
		condition.setMsgType(TypeDictEnum.EBM_MSG_TYPE_PLAY.getKey());
		Ebm ebm = dao.getMapper().selectOne(condition);
		return ebm;
	}

}
