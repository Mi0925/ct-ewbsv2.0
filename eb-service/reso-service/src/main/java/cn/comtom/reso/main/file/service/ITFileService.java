package cn.comtom.reso.main.file.service;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.file.entity.dbo.TFile;

import java.util.List;

public interface ITFileService extends BaseService<TFile,String> {

    FileInfo getByMd5(String md5Code);

    List<FileInfo> getList(FilePageRequest request);
}
