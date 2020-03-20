package cn.comtom.core.main.access.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.access.entity.dbo.AccessStrategy;
import cn.comtom.domain.core.access.info.AccessStrategyAll;
import cn.comtom.domain.core.access.info.AccessStrategyInfo;

import java.util.List;

public interface IAccessStrategyService extends BaseService<AccessStrategy,String> {
    List<AccessStrategyInfo> getByInfoId(String infoId);

    List<AccessStrategyAll> getInfoAllByInfoId(String infoId);
}
