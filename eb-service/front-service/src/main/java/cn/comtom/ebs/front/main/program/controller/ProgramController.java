package cn.comtom.ebs.front.main.program.controller;

import cn.comtom.domain.core.program.info.ProgramFilesInfo;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.info.ProgramStrategyInfo;
import cn.comtom.domain.core.program.request.ProgramAddRequest;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import cn.comtom.domain.core.program.request.ProgramUpdateRequest;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileUploadRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.impl.ResoFeiginServiceImpl;
import cn.comtom.ebs.front.fegin.service.impl.SystemFeginServiceImpl;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.file.service.IFileLibraryService;
import cn.comtom.ebs.front.main.file.service.IFileService;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.main.program.service.IProgramService;
import cn.comtom.ebs.front.main.user.service.ISysUserRoleService;
import cn.comtom.ebs.front.utils.FileUtil;
import cn.comtom.ebs.front.utils.TTSComponent;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/safeRest/program")
@Api(tags = "节目制播", description = "节目制播")
@Slf4j
public class ProgramController extends AuthController {

    @Autowired
    private IProgramService programService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private SystemFeginServiceImpl systemFeginService;

    @Autowired
    private ResoFeiginServiceImpl resoFeiginService;

    @Autowired
    private TTSComponent ttsComponent;

    @Autowired
    private IFileService fileService;

    @Autowired
    private IFileLibraryService fileLibraryService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private IResoFeginService resoFeginService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询节目信息", notes = "分页查询节目信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_LIST)
    public ApiPageResponse<ProgramInfo> page(@Valid ProgramPageRequest pageRequest, BindingResult bResult) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get program info by page. request=[{}]", pageRequest);
        }
        if (StringUtils.isBlank(pageRequest.getSidx())) {
            pageRequest.setOrder("desc");
            pageRequest.setSidx("createTime");
        }
        //增加数据权限控制，如果当前用户是节目制播管理员则查询所有数据，否则只查询自己的数据
        String userId = this.getUser().getUserId();
        Set<String> userRoleIds = sysUserRoleService.getUserRoleIds(userId);
        if (!userRoleIds.contains(CommonDictEnum.PROGRAM_ADMIN_ROLE.getKey())) {
            pageRequest.setCreateUser(this.getAccount());
        }
        return programService.page(pageRequest);
    }

    @GetMapping("/{programId}")
    @ApiOperation(value = "根据ID查询节目信息", notes = "根据ID查询节目信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_LIST)
    public ApiEntityResponse<ProgramInfo> getById(@PathVariable(name = "programId") String programId) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get program info by programId. programId=[{}]", programId);
        }
        ProgramInfo programInfo = programService.getById(programId);
        return ApiEntityResponse.ok(programInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新节目信息", notes = "更新节目信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_EDIT)
    public ApiResponse update(@RequestBody @Valid ProgramUpdateRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] update program info . request=[{}]", request);
        }
        request.setUpdateTime(new Date());
        Integer contentType = request.getContentType();
        if (contentType == 1) {
            request.setFilesList(null);
            Integer secondLength = ttsComponent.getProgramContentSpeechLength(request.getProgramContent().getContent());
            request.getProgramContent().setSecondLength(secondLength);
        } else if (contentType == 4 && Objects.nonNull(request.getState()) && 2 == request.getState()) {
            List<ProgramFilesInfo> textToAudio = textToAudio(request.getProgramContent().getContent());
            request.setFilesList(textToAudio);
        }
        ProgramStrategyInfo programStrategyInfo = request.getProgramStrategy();
        if (programStrategyInfo != null && programStrategyInfo.getStrategyType() == 1) {
            //一次性播放节目
            programStrategyInfo.setOverTime(null);
            programStrategyInfo.setStartTime(null);
            programStrategyInfo.setWeekMask(null);
        } else if (programStrategyInfo != null && programStrategyInfo.getStrategyType() == 2) {
            //每日播放节目
            programStrategyInfo.setPlayTime(null);
            programStrategyInfo.setWeekMask(null);
        } else if (programStrategyInfo != null && programStrategyInfo.getStrategyType() == 3) {
            //每周播放任务
            programStrategyInfo.setPlayTime(null);
        }
        //重置节目状态为新建  审核状态为 “未审核”
        if (!request.getState().equals(Integer.parseInt(StateDictEnum.PROGRAM_STATE_COMMIT.getKey()))) {
            request.setState(Integer.parseInt(StateDictEnum.PROGRAM_STATE_CREATE.getKey()));
        }
        request.setAuditResult(Integer.parseInt(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey()));
        programService.update(request);
        return ApiResponse.ok();
    }


    @PutMapping("/audit")
    @ApiOperation(value = "审核节目信息", notes = "审核节目信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_AUDIT)
    public ApiResponse audit(@RequestBody @Valid ProgramAuditRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] audit program info . request=[{}]", request);
        }

        if (request.getAuditResult().toString().equals(StateDictEnum.AUDIT_STATUS_REFUSED.getKey())) {
            //不通过
            request.setState(Integer.parseInt(StateDictEnum.PROGRAM_STATE_CANCEL.getKey()));
        }
        request.setAuditUser(this.getUser().getAccount());
        request.setAuditTime(new Date());
        programService.audit(request);
        return ApiResponse.ok();
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增节目信息", notes = "新增节目信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_ADD)
    public ApiEntityResponse<ProgramInfo> save(@RequestBody @Valid ProgramAddRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] add program info . request=[{}]", request);
        }

        Integer programType = request.getProgramType();
        if (programType.equals(Constants.PROGRAM_TYPE_RC)) {
            //日常广播没有事件类型以及事件描述
            request.setEbmEventDesc(null);
            request.setEbmEventType(null);
        }

        SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo platformInfo = resoFeiginService.getEbrPlatformById(sysParamsInfo.getParamValue());
        request.setEbrId(platformInfo.getPsEbrId());
        request.setEbrName(platformInfo.getPsEbrName());
        request.setCreateUser(getAccount());
        Integer contentType = request.getContentType();
        //如果是文本类型的节目，不保存文件列表
        if (contentType == 1) {
            request.setFilesList(null);
            Integer secondLength = ttsComponent.getProgramContentSpeechLength(request.getProgramContent().getContent());
            request.getProgramContent().setSecondLength(secondLength);
        } else if (contentType == 4 && Objects.nonNull(request.getState()) && 2 == request.getState()) {
            List<ProgramFilesInfo> textToAudio = textToAudio(request.getProgramContent().getContent());
            request.setFilesList(textToAudio);
        }
        ProgramStrategyInfo programStrategyInfo = request.getProgramStrategy();
        if (programStrategyInfo.getStrategyType() == null) {//当没有获取到播放策略类型时默认为一次性，主要是解决应急播放策略类型为空的问题
            request.getProgramStrategy().setStrategyType(1);
        }

        if (programStrategyInfo != null && programStrategyInfo.getStrategyType() == 1) {
            //一次性播放节目
            programStrategyInfo.setOverTime(null);
            programStrategyInfo.setStartTime(null);
            programStrategyInfo.setWeekMask(null);
        } else if (programStrategyInfo != null && programStrategyInfo.getStrategyType() == 2) {
            //每日播放节目
            programStrategyInfo.setPlayTime(null);
            programStrategyInfo.setWeekMask(null);
        } else if (programStrategyInfo != null && programStrategyInfo.getStrategyType() == 3) {
            //每周播放任务
            programStrategyInfo.setPlayTime(null);
        }

        ProgramInfo programInfo = programService.save(request);
        return ApiEntityResponse.ok(programInfo);
    }

    /**
     * 文转语音文件处理-更新
     */
    private List<ProgramFilesInfo> textToAudio(String contentStr) {
        try {
            //文转语类型处理
            File audioFile = ttsComponent.getTextToAudioFile(contentStr);
            //获取文件的MD5
            String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(audioFile));
            FileInfo fileByMd5 = resoFeginService.getFileByMd5(md5);

            //设置文件基本信息
            FileUploadRequest fileUploadRequest = new FileUploadRequest();
            fileUploadRequest.setAuditState("1"); //审批状态
            fileUploadRequest.setLibId("1");    //文件夹
            fileUploadRequest.setOriginName(audioFile.getName());   //文件名
            fileUploadRequest.setUploadedName(audioFile.getName()); //上传文件名
            //获取根路径
            FileLibraryInfo libraryInfoById = fileLibraryService.getFileLibraryInfoById("1");

            FileInfo fileInfo;
            if (fileByMd5 != null) {
                //该文件上传过，不再重复上传,只记录文件信息
                fileInfo = fileService.saveUploadFile(fileUploadRequest, fileByMd5.getFileUrl(), fileByMd5.getFilePath(), audioFile, md5, getUser().getAccount());
            } else {
                //保存文件到fastdfs
                String extName = audioFile.getName().substring(audioFile.getName().lastIndexOf(".") + 1);
                StorePath storePath = storageClient.uploadFile(new FileInputStream(audioFile), audioFile.length(), extName, null);
                String fullPath = storePath.getFullPath();
                //记录文件信息
                fileInfo = fileService.saveUploadFile(fileUploadRequest, libraryInfoById.getLibURI() + audioFile.getName(),
                        fullPath, audioFile, md5, getUser().getAccount());
            }
            ProgramFilesInfo programFilesInfo = new ProgramFilesInfo();
            programFilesInfo.setFileId(fileInfo.getId());
            programFilesInfo.setFileName(audioFile.getName());
            programFilesInfo.setSecondLength(fileInfo.getSecondLength());
            List<ProgramFilesInfo> filesList = Lists.newArrayList();
            filesList.add(programFilesInfo);
            return filesList;
        } catch (IOException e) {
            log.error("上传文件出错！", e);
        }
        return null;
    }

    @DeleteMapping("/delete/{programId}")
    @ApiOperation(value = "删除节目信息", notes = "删除节目信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_DELETE)
    public ApiResponse delete(@PathVariable(name = "programId") String programId) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] delete program info by programId. programId=[{}]", programId);
        }
        programService.delete(programId);
        return ApiResponse.ok();
    }


}
