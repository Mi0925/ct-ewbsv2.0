package cn.comtom.core.main.ebd.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebd.entity.dbo.Ebd;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.request.EbdPageRequest;

import java.util.List;

public interface IEbdService extends BaseService<Ebd,String> {

    List<EbdInfo> getByEbmId(String ebmId);

    List<EbdInfo> list(EbdPageRequest req);
}
