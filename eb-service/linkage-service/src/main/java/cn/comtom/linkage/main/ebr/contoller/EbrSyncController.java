package cn.comtom.linkage.main.ebr.contoller;

import cn.comtom.linkage.fw.BaseController;
import cn.comtom.linkage.main.constants.LinkageErrorEnum;
import cn.comtom.linkage.main.ebr.service.IEbrSyncService;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源同步接口
 *
 * @author:WJ
 * @date: 2019/3/5 0005
 * @time: 下午 3:12
 */
@RestController
@RequestMapping(value = "linkage/ebr")
@Slf4j
@Api(tags = "资源同步管理", description = "资源同步接口")
public class EbrSyncController extends BaseController {

    @Autowired
    private IEbrSyncService ebrSyncService;

    @RequestMapping("/psInfoReport")
    @ApiOperation(value = "平台资源上报", notes = "平台资源上报")
    public ApiResponse psInfoReport() {
        try {
            ebrSyncService.psInfoReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }

    @RequestMapping("/bsInfoReport")
    @ApiOperation(value = "播出资源上报", notes = "播出资源上报")
    public ApiResponse bsInfoReport() {
        try {
            ebrSyncService.bsInfoReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }

    @RequestMapping("/dtInfoReport")
    @ApiOperation(value = "终端资源上报", notes = "终端资源上报")
    public ApiResponse dtInfoReport() {
        try {
            ebrSyncService.dtInfoReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }

    @RequestMapping("/psStateReport")
    @ApiOperation(value = "平台资源状态上报", notes = "平台资源状态上报")
    public ApiResponse ebrStateReport() {
        try {
            ebrSyncService.psStateReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }

    @RequestMapping("/bsStateReport")
    @ApiOperation(value = "播出资源状态上报", notes = "播出资源状态上报")
    public ApiResponse bsStateReport() {
        try {
            ebrSyncService.bsStateReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }

    @RequestMapping("/dtStateReport")
    @ApiOperation(value = "终端资源状态上报", notes = "终端资源状态上报")
    public ApiResponse dtStateReport() {
        try {
            ebrSyncService.dtStateReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }

    @RequestMapping("/brdLogReport")
    @ApiOperation(value = "播发记录上报", notes = "播发记录上报")
    public ApiResponse brdLogReport() {
        try {
            ebrSyncService.brdLogReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }
    
    @RequestMapping("/stInfoReport")
    @ApiOperation(value = "台站资源信息上报", notes = "台站资源信息上报")
    public ApiResponse stInfoReport() {
        try {
            ebrSyncService.stInfoReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }
    
    @RequestMapping("/asInfoReport")
    @ApiOperation(value = "适配器资源信息上报", notes = "适配器资源信息上报")
    public ApiResponse asInfoReport() {
        try {
            ebrSyncService.asInfoReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }
    
    @RequestMapping("/asStateReport")
    @ApiOperation(value = "适配器资源状态上报", notes = "适配器资源状态上报")
    public ApiResponse asStateReport() {
        try {
            ebrSyncService.asStateReport();
        } catch (Exception e) {
            return ApiResponse.error(LinkageErrorEnum.SYNC_DATA_FAILUE.getCode(), LinkageErrorEnum.SYNC_DATA_FAILUE.getMsg());
        }
        return ApiResponse.ok();
    }
    
}
