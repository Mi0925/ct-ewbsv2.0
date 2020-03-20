package cn.comtom.ebs.front.main.file.controller;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.*;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.file.service.IFileLibraryService;
import cn.comtom.ebs.front.main.file.service.IFileService;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/safeRest/fileLibrary")
@Api(tags = "文件夹管理", description = "文件夹管理")
@AuthRest
public class FileLibraryController extends AuthController {

    @Autowired
    private IFileLibraryService fileLibraryService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private ISystemFeginService systemFeginService;


    @GetMapping("/{libId}")
    @ApiOperation(value = "获取文件夹详情", notes = "获取文件夹详情")
    @RequiresPermissions(Permissions.RES_FILE_DIR_DETAIL)
    public ApiEntityResponse<FileLibraryInfo> getFileLibraryById(@PathVariable(name = "libId") String libId){
        if(log.isDebugEnabled()){
            log.debug("[!~]get FileLibraryInfo detail  by libId.libId=[{}]",libId);
        }
        if(StringUtils.isBlank(libId)){
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        FileLibraryInfo fileLibraryInfo = fileLibraryService.getFileLibraryInfoById(libId);
        if(StringUtils.isNotBlank(fileLibraryInfo.getParentLibId())){
            FileLibraryInfo parentLibInfo = fileLibraryService.getFileLibraryInfoById(fileLibraryInfo.getParentLibId());
            fileLibraryInfo.setParentLibName(parentLibInfo.getLibName());
        }

        return ApiEntityResponse.ok(fileLibraryInfo);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增文件夹", notes = "新增文件夹信息")
    @RequiresPermissions(Permissions.RES_FILE_DIR_CREATE)
    public ApiEntityResponse<FileLibraryInfo> saveFileLibraryInfo(@RequestBody @Valid FileLibraryAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save FileLibraryInfo  .request=[{}]",request);
        }
        SysUserInfo user = getUser();
        request.setCreateUser(user.getAccount());
        request.setLibType(Constants.FILE_LIB_TYPE_USER);
        return ApiEntityResponse.ok(fileLibraryService.saveFileLibraryInfo(request));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改文件夹信息", notes = "修改文件夹信息")
    @RequiresPermissions(Permissions.RES_FILE_DIR_EDIT)
    public ApiResponse updateFileLibraryInfo(@RequestBody @Valid FileLibraryUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update FileLibraryInfo  by fileId.request=[{}]",request);
        }
        fileLibraryService.updateFileLibraryInfo(request);
        return ApiResponse.ok();
    }

    @DeleteMapping("/deleteThis/{libId}")
    @ApiOperation(value = "删除文件夹,只删除当前文件夹", notes = "删除文件夹,只删除当前文件夹")
    @RequiresPermissions(Permissions.RES_FILE_DIR_DELETE)
    public ApiResponse deleteFileLibraryInfoThis(@PathVariable(name = "libId")  String libId){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete this FileLibraryInfo  by libId.libId=[{}]",libId);
        }
        //获取默认文件夹Id
        SysParamsInfo sysParamsInfo = systemFeginService.getSysParamsInfoByKey(Constants.DEFAULT_LIBRARY_ID);
        String defaultLibId = Optional.ofNullable(sysParamsInfo).map(SysParamsInfo::getParamValue).orElseGet(String::new);

        //查询该文件夹下的子文件夹，不包括子文件夹下的子文件夹
        FileLibraryPageRequest request = new FileLibraryPageRequest();
        request.setParentLibId(libId);
        List<FileLibraryInfo> fileLibraryInfoList =  fileLibraryService.getList(request);
        List<FileLibraryUpdateRequest> requestList = Optional.ofNullable(fileLibraryInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(fileLibraryInfo -> {
                    FileLibraryUpdateRequest updateRequest = new FileLibraryUpdateRequest();
                    updateRequest.setLibId(fileLibraryInfo.getLibId());
                    updateRequest.setParentLibId(defaultLibId);
                    return  updateRequest;
                }).collect(Collectors.toList());

        //将待删除的文件夹下的子文件夹移动到默认文件夹下
        fileLibraryService.updateFileLibraryBatch(requestList);

        //查询该文件夹下的子文件，不包括子文件夹下的子文件
        FilePageRequest filePageRequest = new FilePageRequest();
        filePageRequest.setLibId(libId);
        List<FileInfo> fileInfoList = fileService.getList(filePageRequest);
        List<FileUpdateRequest> fileRequestList = Optional.ofNullable(fileInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(fileInfo -> {
                    FileUpdateRequest updateRequest = new FileUpdateRequest();
                    updateRequest.setId(fileInfo.getId());
                    updateRequest.setLibId(defaultLibId);
                    return  updateRequest;
                }).collect(Collectors.toList());
        //将待删除的文件夹下的文件移动到默认文件夹下
        fileService.updateFileInfoBatch(fileRequestList);

        //删除该文件夹
        fileLibraryService.deleteFileLibrary(libId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/deleteAll/{libId}")
    @ApiOperation(value = "删除文件夹及所有子文件（夹）", notes = "删除文件夹及所有子文件（夹）")
    @RequiresPermissions(Permissions.RES_FILE_DIR_DELETE)
    public ApiResponse deleteFileLibraryInfoAll(@PathVariable(name = "libId")  String libId){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete FileLibraryInfo and all children  by libId.libId=[{}]",libId);
        }

        fileLibraryService.deleteFileLibraryAll(libId);
        return ApiResponse.ok();
    }
}
