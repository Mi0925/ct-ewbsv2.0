package cn.comtom.ebs.front.main.core.contoller;

import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import cn.comtom.domain.core.ebm.request.EbmStateBackQueryRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.core.service.IEbmStateBackService;
import cn.comtom.tools.response.ApiEntityResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/1/4 0004
 * @time: 下午 3:40
 */
@RestController
@RequestMapping("/safeRest/ebmStateBack")
@Api(tags = "预警反馈")
@Slf4j
@AuthRest
public class EbmStateBackController extends AuthController {

    @Autowired
    private IEbmStateBackService ebmStateBackService;

    @GetMapping("/list")
    @ApiOperation(value = "根据EbmId查询反馈记录", notes = "根据EbmId查询反馈记录")
    public ApiEntityResponse<List<EbmStateBackInfo>> getByList(EbmStateBackQueryRequest request){
        List<EbmStateBackInfo> resultList = ebmStateBackService.list(request);
        return  ApiEntityResponse.ok(resultList);
    }

}
