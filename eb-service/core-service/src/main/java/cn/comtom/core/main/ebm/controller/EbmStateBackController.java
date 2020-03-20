package cn.comtom.core.main.ebm.controller;

import cn.comtom.core.fw.UUIdGenId;
import cn.comtom.core.main.ebm.entity.dbo.EbmStateBack;
import cn.comtom.core.main.ebm.service.IEbmStateBackService;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.domain.core.ebm.request.EbmStateBackAddRequest;
import cn.comtom.domain.core.ebm.request.EbmStateBackQueryRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author:WJ
 * @date: 2019/1/4 0004
 * @time: 下午 3:40
 */
@RestController
@RequestMapping("/core/ebmStateBack")
@Api(tags = "预警反馈")
@Slf4j
public class EbmStateBackController {

    @Autowired
    private IEbmStateBackService ebmStateBackService;


    @PostMapping("/save")
    @ApiOperation(value = "保存预警反馈记录", notes = "保存预警反馈记录")
    public ApiEntityResponse<EbmStateBackInfo> save(@RequestBody @Valid EbmStateBackAddRequest request, BindingResult bindResult) {
        EbmStateBack stateBack = new EbmStateBack();
        BeanUtils.copyProperties(request,stateBack);
        stateBack.setId(UUIDGenerator.getUUID());
        //特殊处理
        if(StringUtils.isEmpty(stateBack.getBackType())){
            stateBack.setBackType("自动");
        }
        ebmStateBackService.save(stateBack);
        EbmStateBackInfo info=new EbmStateBackInfo();
        BeanUtils.copyProperties(stateBack,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/list")
    @ApiOperation(value = "根据EbmId查询反馈记录", notes = "根据EbmId查询反馈记录")
    public ApiEntityResponse<List<EbmStateBackInfo>> getByPage(@ModelAttribute EbmStateBackQueryRequest request){
        List<EbmStateBackInfo> resultList = ebmStateBackService.list(request);
        return  ApiEntityResponse.ok(resultList);
    }

}
