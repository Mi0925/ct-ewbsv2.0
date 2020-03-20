package cn.comtom.reso.main.file.service;

import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.domain.reso.file.request.OriginFilePageRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.file.entity.dbo.OriginFile;

import java.util.List;

public interface IOriginFileService extends BaseService<OriginFile,String> {

    OriginFileInfo save(OriginFileAddRequest request);

    List<OriginFileInfo> list(OriginFilePageRequest request);

    OriginFileInfo getByMd5(String md5);
}
