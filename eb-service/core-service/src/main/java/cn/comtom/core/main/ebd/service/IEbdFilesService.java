package cn.comtom.core.main.ebd.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebd.entity.dbo.EbdFiles;
import cn.comtom.domain.core.ebd.info.EbdFilesInfo;

import java.util.List;

public interface IEbdFilesService extends BaseService<EbdFiles,String> {

    List<EbdFilesInfo> getByEbdId(String ebdId);
}
