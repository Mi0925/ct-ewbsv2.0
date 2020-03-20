package cn.comtom.ebs.front.main.file.service;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.domain.reso.file.request.FileUpdateRequest;
import cn.comtom.domain.reso.file.request.FileUploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface IFileService {
    FileInfo getFileInfoById(String fileId);

    Boolean updateFileInfo(FileUpdateRequest request);

    Boolean deleteFileInfo(String fileId);

    List<FileInfo> getList(FilePageRequest filePageRequest);

    Integer updateFileInfoBatch(List<FileUpdateRequest> fileRequestList);

    FileInfo saveUploadFile(FileUploadRequest req, String fileUrl, String filePath, MultipartFile multipartFile, String md5, String account);

    FileInfo saveUploadFile(FileUploadRequest req, String fileUrl, String filePath, File file, String md5, String account);
}
