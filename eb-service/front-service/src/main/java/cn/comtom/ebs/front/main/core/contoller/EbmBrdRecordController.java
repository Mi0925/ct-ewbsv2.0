package cn.comtom.ebs.front.main.core.contoller;


import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordPageRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.core.service.IEbmBrdRecordService;
import cn.comtom.tools.response.ApiPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/safeRest/ebmBrdRecord")
@Api(tags = "播发记录管理")
@Slf4j
@AuthRest
public class EbmBrdRecordController extends AuthController {

    @Autowired
    private IEbmBrdRecordService ebmBrdRecordService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询播发记录", notes = "分页查询播发记录")
   // @RequiresPermissions(Permissions.CORE_BRD_RECORD_QUERY)
    public ApiPageResponse<EbmBrdRecordInfo> page(EbmBrdRecordPageRequest req){
        List<EbmBrdRecordInfo> infoList = ebmBrdRecordService.page(req);
        return ApiPageResponse.ok(infoList);
    }


}
