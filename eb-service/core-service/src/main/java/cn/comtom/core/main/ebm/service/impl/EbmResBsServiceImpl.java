package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmResBsDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmResBs;
import cn.comtom.core.main.ebm.service.IEbmResBsService;
import cn.comtom.domain.core.ebm.info.EbmResBsInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EbmResBsServiceImpl extends BaseServiceImpl<EbmResBs,String> implements IEbmResBsService {

    @Autowired
    private EbmResBsDao dao;

    @Override
    public BaseDao<EbmResBs, String> getDao() {
        return dao;
    }

    @Override
    public List<EbmResBsInfo> getEbmResBsByResourceId(String brdItemId) {
        List<EbmResBs> list = dao.getEbmResBsByResourceId(brdItemId);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmResBs -> {
            EbmResBsInfo ebmResBsInfo = new EbmResBsInfo();
            BeanUtils.copyProperties(ebmResBs,ebmResBsInfo);
            return ebmResBsInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbmResBsInfo> getByBrdItemIdIdAndSysInfo(String brdItemId, String brdSysInfo) {
        List<EbmResBs> list = dao.getByBrdItemIdIdAndSysInfo(brdItemId,brdSysInfo);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmResBs -> {
            EbmResBsInfo ebmResBsInfo = new EbmResBsInfo();
            BeanUtils.copyProperties(ebmResBs,ebmResBsInfo);
            return ebmResBsInfo;
        }).collect(Collectors.toList());
    }
}
