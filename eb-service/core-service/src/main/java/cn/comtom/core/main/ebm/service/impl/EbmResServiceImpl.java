package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmResDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmRes;
import cn.comtom.core.main.ebm.service.IEbmResService;
import cn.comtom.domain.core.ebm.info.EbmResInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EbmResServiceImpl extends BaseServiceImpl<EbmRes,String> implements IEbmResService {

    @Autowired
    private EbmResDao dao;

    @Override
    public BaseDao<EbmRes, String> getDao() {
        return dao;
    }

    @Override
    public List<EbmResInfo> getEbmResListByBrdItemId(String brdItemId) {
        List<EbmRes> list = dao.getEbmResListByBrdItemId(brdItemId);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmRes -> {
            EbmResInfo ebmResInfo = new EbmResInfo();
            BeanUtils.copyProperties(ebmRes,ebmResInfo);
            return ebmResInfo;
        }).collect(Collectors.toList());
    }
}
