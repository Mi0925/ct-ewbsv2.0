package cn.comtom.reso.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrGisInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrstatusCount;
import cn.comtom.domain.reso.ebr.request.EbrGisPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.VEbrDao;
import cn.comtom.reso.main.ebr.dao.VEbrGisDao;
import cn.comtom.reso.main.ebr.entity.dbo.VEbr;
import cn.comtom.reso.main.ebr.service.IVEbrGisService;
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
public class VEbrGisServiceImpl  implements IVEbrGisService{

    @Autowired
    private VEbrGisDao dao;

    public List<EbrGisInfo> getEbrGisByPage(EbrGisPageRequest request) {
        return dao.getEbrGisByPage(request);
    }

    @Override
    public List<EbrstatusCount> getEbrGisStatusCount(EbrGisPageRequest request) {
        return dao.getEbrGisStatusCount(request);
    }
}
