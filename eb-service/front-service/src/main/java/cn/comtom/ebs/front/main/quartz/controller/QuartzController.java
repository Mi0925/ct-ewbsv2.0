package cn.comtom.ebs.front.main.quartz.controller;

import cn.comtom.domain.core.quartz.QuartzInfo;
import cn.comtom.domain.core.quartz.QuartzPageRequest;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.fegin.service.IQuartzFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.quartz.entity.QuartzJob;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/safeRest/quartz")
@Api(tags = "定时器管理")
public class QuartzController extends AuthController {

    @Autowired
    IQuartzFeginService quartzFeginService;
    
    @Autowired
    ICoreFeginService coreFeginService;

    @PostMapping("/createJob")
    @ApiOperation(value = "创建定时任务", notes = "创建定时任务")
    public ApiResponse createJob(@RequestBody QuartzJob quartzJob){
        if(log.isDebugEnabled()){
            log.debug("create quartz job .quartzJob=[{}], ", JSON.toJSONString(quartzJob));
        }
        quartzFeginService.createQuartzJob(quartzJob);
        return ApiResponse.ok();
    }

    @PutMapping("/updateJob")
    @ApiOperation(value = "修改定时任务", notes = "修改消息分发定时任务")
    public ApiResponse rescheduleDispatchJob(@RequestBody QuartzJob quartzJob ){
        quartzFeginService.updateQuartzJob(quartzJob );
        return ApiResponse.ok();
    }


    @PutMapping(value="/pauseJob")
    @ApiOperation(value = "暂停定时任务", notes = "暂停定时任务")
    public ApiResponse pauseJob(@RequestBody QuartzJob quartzJob)
    {
        quartzFeginService.pauseQuartzJob(quartzJob);
        return ApiResponse.ok();
    }

    @DeleteMapping(value="/delJob")
    @ApiOperation(value = "删除定时任务", notes = "删除定时任务")
    public ApiResponse delJob(@RequestBody QuartzJob quartzJob)
    {
        quartzFeginService.deleteQuartzJob(quartzJob);
        return ApiResponse.ok();
    }



    @PutMapping(value="/resumeJob")
    @ApiOperation(value = "恢复定时任务", notes = "恢复定时任务")
    public ApiResponse resumeJob(@RequestBody QuartzJob quartzJob)
    {
        quartzFeginService.resumeQuartzJob(quartzJob);
        return ApiResponse.ok();
    }


    @GetMapping(value="/list")
    @ApiOperation(value = "获取所有的定时任务", notes = "获取所有的定时任务")
    public ApiPageResponse<QuartzJob> getAllJobs()
    {
        List<QuartzJob> quartzJobList = quartzFeginService.getAllJobs();
        return ApiPageResponse.ok(quartzJobList.size(),1,50,quartzJobList);
    }
    
    @GetMapping(value="/getAllJobsByPage")
    @ApiOperation(value = "获取所有的定时任务", notes = "获取所有的定时任务")
    public ApiPageResponse<QuartzInfo> getAllJobsByPage(QuartzPageRequest request)
    {
        return coreFeginService.getAllJobsByPage(request);
    }

}
