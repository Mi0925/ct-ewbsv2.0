package cn.comtom.ebs.front.main.file.service.impl;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.domain.reso.file.request.FileUpdateRequest;
import cn.comtom.domain.reso.file.request.FileUploadRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.file.service.IFileService;
import cn.comtom.ebs.front.utils.FileUtil;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public FileInfo getFileInfoById(String fileId) {
        return resoFeginService.getFileInfoById(fileId);
    }

    @Override
    public Boolean updateFileInfo(FileUpdateRequest request) {
        return resoFeginService.updateFileInfo(request);
    }

    @Override
    public Boolean deleteFileInfo(String fileId) {
        return resoFeginService.deleteFileInfoByFileId(fileId);
    }

    @Override
    public List<FileInfo> getList(FilePageRequest filePageRequest) {
        filePageRequest.setLimit(Constants.MAX_SQL_ROWS);
        ApiPageResponse<FileInfo> response = resoFeginService.getFileInfoByPage(filePageRequest);
        if(response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    public Integer updateFileInfoBatch(List<FileUpdateRequest> fileRequestList) {
        return resoFeginService.updateFileInfoBatch(fileRequestList);
    }

    /**
     * 保存上传文件的基本信息
     * @param req
     * @param fileUrl
     * @param filePath
     * @param multipartFile
     * @param md5
     * @param account
     * @return
     */
    @Override
    public FileInfo saveUploadFile(FileUploadRequest req, String fileUrl, String filePath, MultipartFile multipartFile, String md5, String account) {
        FileAddRequest request = new FileAddRequest();
        Long duration = getFileDuration(multipartFile);
        request.setSecondLength(duration.intValue());
        request.setFileType(TypeDictEnum.FILE_TYPE_MEDIA.getKey());
        //设置文件虚拟文件夹路径
        request.setFileUrl(fileUrl);
        //设置文件fastdfs路径
        request.setFilePath(filePath);
        request.setByteSize(multipartFile.getSize());
        request.setCreateUser(account);
        request.setFileExt(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        request.setLibId(req.getLibId());
        request.setMd5Code(md5);
        request.setOriginName(multipartFile.getOriginalFilename());
        request.setUploadedName(multipartFile.getOriginalFilename());
        request.setAuditState(req.getAuditState());
        return  resoFeginService.saveFile(request);
    }

    /**
     * 保存生成文件上传的基本信息
     * @param req
     * @param fileUrl
     * @param filePath
     * @param file
     * @param md5
     * @param account
     * @return
     */
    @Override
    public FileInfo saveUploadFile(FileUploadRequest req, String fileUrl, String filePath, File file, String md5, String account) {
        FileAddRequest request = new FileAddRequest();
        Long duration = FileUtils.getDuration(file);
        request.setSecondLength(duration.intValue());
        request.setFileType(TypeDictEnum.FILE_TYPE_MEDIA.getKey());
        //设置文件虚拟文件夹路径
        request.setFileUrl(fileUrl);
        //设置fastdfs路径
        request.setFilePath(filePath);
        request.setByteSize(file.length());
        request.setCreateUser(account);
        request.setFileExt(FilenameUtils.getExtension(file.getName()));
        request.setLibId(req.getLibId());
        request.setMd5Code(md5);
        request.setOriginName(file.getName());
        request.setUploadedName(file.getName());
        request.setAuditState(req.getAuditState());
        return  resoFeginService.saveFile(request);
    }

    private Long getFileDuration(MultipartFile multipartFile) {
        SysParamsInfo sysParamsInfo = systemFeginService.getSysParamsInfoByKey(Constants.TEMP_FILE_PATH);
        File transferedFile = new File(sysParamsInfo.getParamValue() + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(transferedFile);
        } catch (IOException e) {
            log.error("文件转换异常！",e);
        }
        return FileUtils.getDuration(transferedFile);
    }
}
