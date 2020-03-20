package cn.comtom.reso.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.VEbrDao;
import cn.comtom.reso.main.ebr.entity.dbo.VEbr;
import cn.comtom.reso.main.ebr.service.IVEbrService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VEbrServiceImpl extends BaseServiceImpl<VEbr,String> implements IVEbrService {

    @Autowired
    private VEbrDao dao;

    @Override
    public BaseDao<VEbr, String> getDao() {
        return dao;
    }


    @Override
    public List<EbrInfo> list(EbrQueryRequest request) {
        List<VEbr> vEbrList = dao.list(request);
        return Optional.ofNullable(vEbrList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebr -> {
                    EbrInfo ebrInfo = new EbrInfo();
                    BeanUtils.copyProperties(ebr,ebrInfo);
                    return ebrInfo;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<EbrInfo> listForInfoSrc(EbrQueryRequest queryRequest) {
        return dao.listForInfoSrc(queryRequest);
    }
}
