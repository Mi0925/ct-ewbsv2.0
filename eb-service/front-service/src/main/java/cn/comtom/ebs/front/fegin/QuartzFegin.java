package cn.comtom.ebs.front.fegin;

import cn.comtom.ebs.front.main.quartz.entity.QuartzJob;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Create By baichun on 2019/4/3
 */
@FeignClient(value = "quartz-service")
public interface QuartzFegin {

    @PostMapping("/quartz/createJob")
    ApiResponse createQuartzJob(@RequestBody QuartzJob quartzJob);

    @PutMapping("/quartz/updateJob")
    ApiResponse updateQuartzJob(@RequestBody QuartzJob quartzJob);

    @PutMapping("/quartz/pauseJob")
    ApiResponse pauseQuartzJob(@RequestBody QuartzJob quartzJob);

    @DeleteMapping(value="/quartz/delJob")
    ApiResponse deleteQuartzJob(@RequestBody QuartzJob quartzJob);

    @PutMapping(value="/quartz/resumeJob")
    ApiResponse resumeQuartzJob(@RequestBody QuartzJob quartzJob);

    @GetMapping(value="/quartz/getAllJobs")
    ApiEntityResponse<List<Map<String,Object>>> getAllJobs();
}
