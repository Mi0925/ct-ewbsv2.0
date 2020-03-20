package cn.comtom.core.main.ebm.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmPlanRef;
import cn.comtom.domain.core.ebm.info.EbmPlanRefInfo;

public interface IEbmPlanRefService extends BaseService<EbmPlanRef,String> {
    EbmPlanRefInfo getOneByEbmId(String ebmId);
}
