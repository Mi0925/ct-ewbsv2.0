package cn.comtom.ebs.front.main.file.controller;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.domain.reso.file.request.FileUpdateRequest;
import cn.comtom.domain.reso.file.request.FileUploadRequest;
import cn.comtom.domain.reso.file.request.VFileLibPageRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.file.model.request.FileDownloadRequest;
import cn.comtom.ebs.front.main.file.service.IFileLibraryService;
import cn.comtom.ebs.front.main.file.service.IFileService;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.utils.FileUtil;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:WJ
 * @date: 2018/12/29 0029
 * @time: 下午 2:38
 * 文件管理接口
 */
@RequestMapping("/safeRest")
@RestController
@Api(tags = "文件管理", description = "文件管理")
@AuthRest
@Slf4j
public class FileController extends AuthController {

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private IFileLibraryService fileLibraryService;

    @Autowired
    private FastFileStorageClient storageClient;

    @GetMapping("/file/player")
    public void player() {
        System.out.println(1);
        System.out.println(2);
    }

    @PostMapping("/file/download")
    @ApiOperation(value = "文件下载", notes = "文件下载(通过文件ID)")
    @RequiresPermissions(Permissions.RES_FILE_DOWNLOAD)
    public void download(@Valid FileDownloadRequest req) throws IOException {
        //设置http请求返回类型为输出文件流
        HttpServletResponse response = this.getHttpResonse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data;charset=UTF-8");
        String fileUrl = "";
        String fileName = "";
        //文件类型为数据包文件
        if (req.getFileType().equals("1")) {
            //获取文件信息
            OriginFileInfo fileInfo = resoFeginService.getOriginFileById(req.getFileId());
            fileUrl = fileInfo.getUrl();
            //文件fastdfs路径不存在报错
            if (StringUtils.isBlank(fileUrl)) {
                return;
            }
            fileName = fileInfo.getOriginName();
        } else if (req.getFileType().equals("2")) {
            //文件类型为媒体文件，获取文件信息
            FileInfo fileInfo = resoFeginService.getFileInfoById(req.getFileId());
            fileUrl = fileInfo.getFileUrl();
            //文件fastdfs路径不存在报错
            if (StringUtils.isBlank(fileUrl)) {
                return;
            }
            fileName = fileInfo.getOriginName();
        }
        //输出文件流
        outputFileStream(response, fileName, fileUrl);
    }

    @PostMapping("/file/downloadByUrl")
    @ApiOperation(value = "文件下载", notes = "文件下载(通过url)")
    @RequiresPermissions(Permissions.RES_FILE_DOWNLOAD)
    public void downloadByUrl(@RequestParam(name = "filePath") String filePath, @RequestParam(name = "originName") String originName) throws IOException {
        HttpServletResponse response = this.getHttpResonse();
        outputFileStream(response, originName, filePath);
    }

    /**
     * 从fastdfs下载文件并通过httpresponse输出文件
     * @param response
     * @param fileName
     * @param fullPath
     * @throws IOException
     */
    private void outputFileStream(HttpServletResponse response, String fileName, String fullPath) throws IOException {
        //设置httpresponse输出类型等信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data;charset=UTF-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        OutputStream os = response.getOutputStream();
        //获取fastdfs的分组名
        String groupName = fullPath.substring(0, fullPath.indexOf("/"));
        //获取fastdfs的路径
        String filePath = fullPath.substring(fullPath.indexOf("/") + 1);
        //输出文件流
        storageClient.downloadFile(groupName, filePath, ins -> {
            try {
                IOUtils.copy(ins, os);
                os.flush();
            } finally {
                IOUtils.closeQuietly(ins);
                IOUtils.closeQuietly(os);
            }
            return filePath;
        });
    }

    @PostMapping("/file/upload")
    @ApiImplicitParams({@ApiImplicitParam(value = "文件", name = "multipartFile", required = true, allowMultiple = true, dataType = "MultipartFile")})
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @RequiresPermissions(Permissions.RES_FILE_UPLOAD)
    public ApiEntityResponse<FileInfo> uploadFiles(@Valid FileUploadRequest req, MultipartFile multipartFile, BindingResult result) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("[!~] upload files ");
        }
        if (StringUtils.isBlank(req.getLibId())) {
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        //获取文件夹的路径，如果路径为空则报错
        FileLibraryInfo fileLibraryInfo = fileLibraryService.getFileLibraryInfoById(req.getLibId());
        if (fileLibraryInfo == null || StringUtils.isBlank(fileLibraryInfo.getLibURI())) {
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.DIRECTORY_NOT_FOUND);
        }

        //判断文件类型是否符合要求
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!TypeDictEnum.FILE_TYPE_MP3.getDesc().equalsIgnoreCase(extension) && !TypeDictEnum.FILE_TYPE_AAC.getDesc().equalsIgnoreCase(extension) && !TypeDictEnum.FILE_TYPE_TEXT.getDesc().equalsIgnoreCase(extension)) {
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.UNSUPPORT_FILE_TYPE);
        }

        //生成文件的MD5判断是否上传过
        String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(multipartFile));
        FileInfo fileByMd5 = resoFeginService.getFileByMd5(md5);
        //通过md5查询不为空证明该文件上传过，不在上传直接存库文件信息
        FileInfo fileInfo;
        if (fileByMd5 != null) {
            fileInfo = fileService.saveUploadFile(req, fileByMd5.getFileUrl(), fileByMd5.getFilePath(), multipartFile, md5, getUser().getAccount());
        } else {
            //先上传文件，上传成功存库
            String originalFileName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            StorePath storePath = storageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), originalFileName, null);
            String fullPath = storePath.getFullPath();
            //如果上传文件失败，则报错
            if (StringUtils.isBlank(fullPath)) {
                return ApiResponseBuilder.buildEntityError(FrontErrorEnum.UPLOAD_FILE_ERROR);
            }
            //上传成功则保存文件信息
            fileInfo = fileService.saveUploadFile(req, fileLibraryInfo.getLibURI() + multipartFile.getOriginalFilename(), fullPath, multipartFile, md5, getUser().getAccount());
        }
        if (fileInfo == null) {
            log.error("保存文件信息失败：fileInfo为空");
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.UPLOAD_FILE_ERROR);
        }
        return ApiEntityResponse.ok(fileInfo);
    }

    @GetMapping("/fileLib/page")
    @ApiOperation(value = "文件夹和文件视图分页查询", notes = "文件夹和文件视图分页查询")
    //@RequiresPermissions(Permissions.RES_FILE_LIST)
    public ApiPageResponse<VFileLibInfo> getFileLibByPage(@Valid VFileLibPageRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~]query fileLib view by page.request=[{}]", request);
        }
        if (StringUtils.isBlank(request.getParentLibId())) {
            request.setParentLibId(Constants.ROOT_FILE_LIB_ID);
        }
        return resoFeginService.getFileLibViewByPage(request);
    }

    @GetMapping("/file/page")
    @ApiOperation(value = "分页查询文件列表", notes = "分页查询文件列表")
    //@RequiresPermissions(Permissions.RES_FILE_LIST)
    public ApiPageResponse<FileInfo> getFileListByPage(@Valid FilePageRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~]query fileInfo  by page.request=[{}]", request);
        }
        return resoFeginService.getFileInfoByPage(request);
    }

    @GetMapping("/file/{fileId}")
    @ApiOperation(value = "获取文件详情", notes = "获取文件详情")
    @RequiresPermissions(Permissions.RES_FILE_DETAIL)
    public ApiEntityResponse<FileInfo> getFileLibById(@PathVariable(name = "fileId") String fileId) {
        if (log.isDebugEnabled()) {
            log.debug("[!~]get fileInfo detail  by fileId.fileId=[{}]", fileId);
        }
        if (StringUtils.isBlank(fileId)) {
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        FileInfo fileInfo = fileService.getFileInfoById(fileId);
        if (StringUtils.isNotBlank(fileInfo.getLibId())) {
            FileLibraryInfo parentLibInfo = fileLibraryService.getFileLibraryInfoById(fileInfo.getLibId());
            fileInfo.setLibName(parentLibInfo.getLibName());
        }
        return ApiEntityResponse.ok(fileInfo);
    }

    @PutMapping("/file/update")
    @ApiOperation(value = "修改文件信息", notes = "修改文件信息")
    @RequiresPermissions(Permissions.RES_FILE_EIDT)
    public ApiResponse updateFileInfo(@RequestBody @Valid FileUpdateRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] update fileInfo  by fileId.request=[{}]", request);
        }
        //文件修改后重新审核
        request.setAuditState(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
        fileService.updateFileInfo(request);
        return ApiResponse.ok();
    }

    @PutMapping("/file/audit/batch")
    @ApiOperation(value = "批量修改文件信息", notes = "批量修改文件信息")
    @RequiresPermissions(Permissions.RES_FILE_EIDT)
    public ApiEntityResponse<Integer> auditFileInfoBatch(@RequestBody @Valid List<FileUpdateRequest> requests) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] update fileInfo  batch.requests=[{}]", requests);
        }
        requests = Optional.ofNullable(requests).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> StringUtils.isNotBlank(request.getId()) && StringUtils.isNotBlank(request.getAuditState()))
                .peek(request -> {
                    request.setAuditDate(new Date());
                    request.setAuditUser(getUser().getAccount());
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(fileService.updateFileInfoBatch(requests));
    }

    @DeleteMapping("/file/delete/{fileId}")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @RequiresPermissions(Permissions.RES_FILE_DELETE)
    public ApiResponse deleteFileInfo(@PathVariable(name = "fileId") String fileId) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] delete fileInfo  by fileId.fileId=[{}]", fileId);
        }
        fileService.deleteFileInfo(fileId);
        return ApiResponse.ok();
    }

}
