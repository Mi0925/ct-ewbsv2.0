package cn.comtom.reso.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.VEbr;

import java.util.List;

public interface IVEbrService extends BaseService<VEbr,String> {

    List<EbrInfo> list(EbrQueryRequest request);

    List<EbrInfo> listForInfoSrc(EbrQueryRequest queryRequest);
}
