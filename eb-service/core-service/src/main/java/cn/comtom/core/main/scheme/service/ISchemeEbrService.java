package cn.comtom.core.main.scheme.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.scheme.entity.dbo.SchemeEbr;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;

import java.util.List;

public interface ISchemeEbrService extends BaseService<SchemeEbr,String> {

    List<SchemeEbrInfo> getSchemeEbrListBySchemeId(String schemeId);

	Boolean deleteBySchemeId(String schemeId);
}
