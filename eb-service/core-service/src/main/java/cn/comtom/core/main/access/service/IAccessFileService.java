package cn.comtom.core.main.access.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.access.entity.dbo.AccessFile;
import cn.comtom.domain.core.access.info.AccessFileInfo;

import java.util.List;

public interface IAccessFileService extends BaseService<AccessFile,String> {
    List<AccessFileInfo> getByInfoId(String infoId);
}
