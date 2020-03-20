package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmAuxiliaryDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmAuxiliary;
import cn.comtom.core.main.ebm.service.IEbmAuxiliaryService;
import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EbmAuxiliaryServiceImpl extends BaseServiceImpl<EbmAuxiliary,String> implements IEbmAuxiliaryService {

    @Autowired
    private EbmAuxiliaryDao dao;

    @Override
    public BaseDao<EbmAuxiliary, String> getDao() {
        return dao;
    }

    @Override
    public List<EbmAuxiliaryInfo> getEbmAuxiliaryInfoByEbmId(String ebmId) {
        List<EbmAuxiliary> ebmAuxiliaryList = dao.getEbmAuxiliaryInfoByEbmId(ebmId);
        return Optional.ofNullable(ebmAuxiliaryList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmAuxiliary ->{
            EbmAuxiliaryInfo ebmAuxiliaryInfo = new EbmAuxiliaryInfo();
            BeanUtils.copyProperties(ebmAuxiliary,ebmAuxiliaryInfo);
            return  ebmAuxiliaryInfo;
        }).collect(Collectors.toList());
    }
}
