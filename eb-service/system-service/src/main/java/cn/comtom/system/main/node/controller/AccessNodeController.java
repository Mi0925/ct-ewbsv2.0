package cn.comtom.system.main.node.controller;


import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.accessnode.request.AccessNodeRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.node.entity.dbo.AccessNode;
import cn.comtom.system.main.node.service.IAccessNodeService;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/node")
@Api(tags = "接入节点接口")
@Slf4j
public class AccessNodeController extends BaseController {

    @Autowired
    private IAccessNodeService accessNodeService;

    @GetMapping("/isWhite")
    @ApiOperation(value = "判断是否在白名单中", notes = "判断是否在白名单中")
    public ApiEntityResponse isWhite(@RequestParam(name = "ip") String ip) {
        Boolean b = accessNodeService.isWhite(ip);
        if(b){
            return ApiEntityResponse.ok(b);
        }else{
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.IP_NOT_IN_WHITE_LIST);
        }
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存接入节点信息", notes = "保存接入节点信息")
    public ApiEntityResponse<AccessNodeInfo> save(@RequestBody @Valid AccessNodeRequest request, BindingResult result) {
        //统一ip字段的格式，每个IP地址后面都带必须带一个，号
        if(StringUtils.isNotBlank(request.getIp()) && request.getIp().lastIndexOf(SymbolConstants.GENERAL_SEPARATOR) != request.getIp().length() - 1){
            request.setIp(request.getIp()+SymbolConstants.GENERAL_SEPARATOR);
        }
        AccessNode accessNode = new AccessNode();
        BeanUtils.copyProperties(request,accessNode);
        String nodeId = UUIDGenerator.getUUID();
        accessNode.setId(nodeId);
        if(StringUtils.isBlank(accessNode.getStatus())){
            //默认设置状态为启用
            accessNode.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
        }

        accessNodeService.save(accessNode);
        AccessNodeInfo info = new AccessNodeInfo();
        BeanUtils.copyProperties(accessNode,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/getByPlatformId")
    @ApiOperation(value = "根据平台ID查询接入节点信息", notes = "根据平台ID查询接入节点信息")
    public ApiEntityResponse<AccessNodeInfo> getByPlatformId(@RequestParam(name = "platformId") String platformId) {
        AccessNodeInfo nodeInfo = accessNodeService.getByPlatformId(platformId);
        if (nodeInfo==null) {
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(nodeInfo);
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除接入节点信息", notes = "删除接入节点信息")
    public ApiResponse deleteByEbrId(@PathVariable(name = "ebrId") String ebrId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete access node by ebrId . ebrId=[{}]",ebrId);
        }
        AccessNode accessNode = new AccessNode();
        accessNode.setPlatformId(ebrId);
        accessNodeService.delete(accessNode);
        return ApiResponse.ok();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新接入节点信息", notes = "更新接入节点信息")
    public ApiResponse update(@RequestBody AccessNodeRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update access node  . request=[{}]",request);
        }
        AccessNode accessNode = new AccessNode();
        BeanUtils.copyProperties(request,accessNode);
        accessNodeService.update(accessNode);
        return ApiResponse.ok();
    }


}
