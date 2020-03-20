package cn.comtom.core.main.ebm.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmResBs;
import cn.comtom.domain.core.ebm.info.EbmResBsInfo;

import java.util.List;

public interface IEbmResBsService extends BaseService<EbmResBs,String> {
    List<EbmResBsInfo> getEbmResBsByResourceId(String ebmResourceId);

    List<EbmResBsInfo> getByBrdItemIdIdAndSysInfo(String brdItemId, String brdSysInfo);
}
