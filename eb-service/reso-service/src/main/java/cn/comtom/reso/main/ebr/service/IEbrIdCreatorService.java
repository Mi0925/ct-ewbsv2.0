package cn.comtom.reso.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrIdCreator;

public interface IEbrIdCreatorService extends BaseService<EbrIdCreator,String> {

    String getSourceTypeCodeSN(EbrIdCreatorInfo creatorInfo);


    EbrIdCreator getByCondition(EbrIdCreatorInfo creatorInfo);
}
