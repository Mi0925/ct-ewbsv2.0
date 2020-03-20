package cn.comtom.ebs.front.main.file.service;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileLibraryAddRequest;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.domain.reso.file.request.FileLibraryUpdateRequest;

import java.util.List;

public interface IFileLibraryService {
    FileLibraryInfo getFileLibraryInfoById(String libId);

    Boolean updateFileLibraryInfo(FileLibraryUpdateRequest request);

    Boolean deleteFileLibrary(String libId);

    List<FileLibraryInfo> getList(FileLibraryPageRequest request);

    Integer updateFileLibraryBatch(List<FileLibraryUpdateRequest> requestList);

    Integer deleteFileLibraryAll(String libId);

    FileLibraryInfo saveFileLibraryInfo(FileLibraryAddRequest request);
}
