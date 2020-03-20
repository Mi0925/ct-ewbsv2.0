package cn.comtom.reso.main.file.service;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.file.entity.dbo.FileLibrary;

import java.util.List;

public interface IFileLibraryService extends BaseService<FileLibrary,String> {

    List<FileLibraryInfo> getList(FileLibraryPageRequest request);

    Integer deleteThisAndSubAll(String libId);
}
