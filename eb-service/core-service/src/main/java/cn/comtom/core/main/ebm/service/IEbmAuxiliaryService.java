package cn.comtom.core.main.ebm.service;


import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmAuxiliary;
import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;

import java.util.List;

public interface IEbmAuxiliaryService extends BaseService<EbmAuxiliary,String> {
    List<EbmAuxiliaryInfo> getEbmAuxiliaryInfoByEbmId(String ebmId);
}
