package cn.comtom.core.main.ebm.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmRes;
import cn.comtom.domain.core.ebm.info.EbmResInfo;

import java.util.List;

public interface IEbmResService extends BaseService<EbmRes,String> {
    List<EbmResInfo> getEbmResListByBrdItemId(String brdItemId);
}
