package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmDispatchInfoDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatchInfo;
import cn.comtom.core.main.ebm.service.IEbmDispatchInfoService;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EbmDispatchInfoServiceImpl extends BaseServiceImpl<EbmDispatchInfo, String> implements IEbmDispatchInfoService {

    @Autowired
    private EbmDispatchInfoDao dao;

    @Override
    public BaseDao<EbmDispatchInfo, String> getDao() {
        return dao;
    }


    @Override
    public List<EbmDispatchInfoInfo> getByEbmId(String ebmId) {
        List<EbmDispatchInfo> ebmDispatchInfoList = dao.getByEbmId(ebmId);
        return Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebmDispatchInfo1 -> {
                    EbmDispatchInfoInfo info = new EbmDispatchInfoInfo();
                    BeanUtils.copyProperties(ebmDispatchInfo1, info);
                    return info;
                }).collect(Collectors.toList());
    }
}
