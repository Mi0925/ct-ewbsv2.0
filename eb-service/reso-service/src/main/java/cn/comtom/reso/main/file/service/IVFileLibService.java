package cn.comtom.reso.main.file.service;

import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.VFileLibPageRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.file.entity.dbo.VFileLib;

import java.util.List;

public interface IVFileLibService extends BaseService<VFileLib,String> {

    List<VFileLibInfo> getList(VFileLibPageRequest request);
}
