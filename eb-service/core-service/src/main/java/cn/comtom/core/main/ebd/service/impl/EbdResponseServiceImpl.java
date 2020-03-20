package cn.comtom.core.main.ebd.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebd.dao.EbdResponseDao;
import cn.comtom.core.main.ebd.entity.dbo.EbdResponse;
import cn.comtom.core.main.ebd.service.IEbdResponseService;
import cn.comtom.domain.core.ebd.info.EbdResponseInfo;
import cn.comtom.domain.core.ebd.request.EbdResponsePageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EbdResponseServiceImpl extends BaseServiceImpl<EbdResponse,String> implements IEbdResponseService {

    @Autowired
    private EbdResponseDao dao;

    @Override
    public BaseDao<EbdResponse, String> getDao() {
        return dao;
    }

    @Override
    public List<EbdResponseInfo> list(EbdResponsePageRequest request) {
        List<EbdResponse> list = dao.list(request);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebdResponse ->{
            EbdResponseInfo ebdResponseInfo = new EbdResponseInfo();
            BeanUtils.copyProperties(ebdResponse,ebdResponseInfo);
            return  ebdResponseInfo;
        }).collect(Collectors.toList());
    }
}
