package cn.comtom.reso.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrGisInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrstatusCount;
import cn.comtom.domain.reso.ebr.request.EbrGisPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.VEbr;

import java.util.List;

public interface IVEbrGisService {

    List<EbrGisInfo> getEbrGisByPage(EbrGisPageRequest request);

    List<EbrstatusCount> getEbrGisStatusCount(EbrGisPageRequest request);
}
