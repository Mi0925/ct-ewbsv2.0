package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmBrdRecordDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdRecord;
import cn.comtom.core.main.ebm.service.IEbmBrdRecordService;
import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordPageRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordWhereRequest;
import cn.comtom.tools.utils.ReflectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EbmBrdRecordServiceImpl extends BaseServiceImpl<EbmBrdRecord,String> implements IEbmBrdRecordService {

    @Autowired
    private EbmBrdRecordDao dao;

    @Override
    public BaseDao<EbmBrdRecord, String> getDao() {
        return dao;
    }

    @Override
    public List<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(String ebmId) {
        List<EbmBrdRecord> list = dao.getEbmBrdRecordListByEbmId(ebmId);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmBrdRecord -> {
            EbmBrdRecordInfo ebmResInfo = new EbmBrdRecordInfo();
            BeanUtils.copyProperties(ebmBrdRecord,ebmResInfo);
            return ebmResInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbmBrdRecordInfo> findEbmBrdRecordListByWhere(EbmBrdRecordWhereRequest request) {
        List<EbmBrdRecord> ebmBrdRecordList = dao.findEbmBrdRecordListByWhere(request);
        return Optional.ofNullable(ebmBrdRecordList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmBrdRecord -> {
            EbmBrdRecordInfo ebmBrdRecordInfo = new EbmBrdRecordInfo();
            BeanUtils.copyProperties(ebmBrdRecord,ebmBrdRecordInfo);
            return ebmBrdRecordInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public EbmBrdRecordInfo getEbmBrdByEbmIdAndResourceId(String ebmId, String resourceId) {
        EbmBrdRecord ebmBrdRecord = dao.getEbmBrdByEbmIdAndResourceId(ebmId,resourceId);
        if(ebmBrdRecord != null){
            EbmBrdRecordInfo ebmBrdRecordInfo = new EbmBrdRecordInfo();
            BeanUtils.copyProperties(ebmBrdRecord,ebmBrdRecordInfo);
            return ebmBrdRecordInfo;
        }
        return null;
    }

    @Override
    public List<EbmBrdRecordInfo> page(EbmBrdRecordPageRequest req) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(req);
        List<EbmBrdRecordInfo> ebmBrdRecordList = dao.page(map);
        return ebmBrdRecordList;
    }
}
