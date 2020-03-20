package cn.comtom.quartz.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.comtom.quartz.fegin.LinkageFegin;
import cn.comtom.quartz.model.QuartzJob;
import cn.comtom.quartz.service.ILinkageService;
import cn.comtom.quartz.utils.ScheduleUtils;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.request.ApiPageRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/quartz")
@Api(tags = "定时器管理")
public class QuartzController {

    @Autowired
    private Scheduler scheduler;
    
    @Autowired
    private ILinkageService linkageService;
    
    @Autowired
    private LinkageFegin linkageFegin;
    
    private static Timer timerTask;
    
    private static int period = 45;
    
    @PostMapping("/createJob")
    @ApiOperation(value = "创建定时任务", notes = "创建定时任务")
    public ApiResponse createJob( @RequestBody QuartzJob quartzJob){
        try {
            String jobName = URLDecoder.decode(quartzJob.getJobDetailName(),Constants.DECODE_TYPE_UTF8);
            String jobGroup = URLDecoder.decode(quartzJob.getGroupName(),Constants.DECODE_TYPE_UTF8);
            String jobDescription = URLDecoder.decode(quartzJob.getJobDescription(),Constants.DECODE_TYPE_UTF8);
            ScheduleUtils.createJob(scheduler,quartzJob.getJobCronExpression(),jobName,jobGroup,jobDescription,Class.forName(quartzJob.getJobClassName()));
        } catch (Exception e) {
            log.error("格式不支持",e);
        }
        return ApiResponse.ok();
    }

    @PutMapping("/updateJob")
    @ApiOperation(value = "修改定时任务", notes = "修改消息分发定时任务")
    public ApiResponse rescheduleDispatchJob(@RequestBody QuartzJob quartzJob){
        String jobName="";
        String jobGroup="";
        try {
            jobName = URLDecoder.decode(quartzJob.getJobDetailName(),Constants.DECODE_TYPE_UTF8);
            jobGroup = URLDecoder.decode(quartzJob.getGroupName(),Constants.DECODE_TYPE_UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error("格式不支持",e);
        }
        if("HeartBeatJob".equals(quartzJob.getJobDetailName())) {
    		stopHeartHeat();
    		period = Integer.valueOf(quartzJob.getJobCronExpression());
    		return ApiResponse.ok();
    	}else {
    		ScheduleUtils.updateJob(scheduler,quartzJob.getJobCronExpression(),jobName,jobGroup);
    	}
        
        return ApiResponse.ok();
    }


    @PutMapping(value="/pauseJob")
    @ApiOperation(value = "暂停定时任务", notes = "暂停定时任务")
    public ApiResponse pauseJob(@RequestBody QuartzJob quartzJob)
    {
        String jobName="";
        String jobGroup="";
        try {
            jobName = URLDecoder.decode(quartzJob.getJobDetailName(),Constants.DECODE_TYPE_UTF8);
            jobGroup = URLDecoder.decode(quartzJob.getGroupName(),Constants.DECODE_TYPE_UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error("格式不支持",e);
        }
        ScheduleUtils.pauseJob(scheduler,jobName, jobGroup);
        if("HeartBeatJob".equals(quartzJob.getJobDetailName())) {
    		stopHeartHeat();
    		return ApiResponse.ok();
    	}
        return ApiResponse.ok();
    }

    @DeleteMapping(value="/delJob")
    @ApiOperation(value = "删除定时任务", notes = "删除定时任务")
    public ApiResponse delJob(@RequestBody QuartzJob quartzJob)
    {
        String jobName="";
        String jobGroup="";
        try {
            jobName = URLDecoder.decode(quartzJob.getJobDetailName(),Constants.DECODE_TYPE_UTF8);
            jobGroup = URLDecoder.decode(quartzJob.getGroupName(),Constants.DECODE_TYPE_UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error("格式不支持",e);
        }
        ScheduleUtils.delJob(scheduler,jobName, jobGroup);
        return ApiResponse.ok();
    }



    @PutMapping(value="/resumeJob")
    @ApiOperation(value = "恢复定时任务", notes = "恢复定时任务")
    public ApiResponse resumeJob(@RequestBody QuartzJob quartzJob)
    {
        String jobName="";
        String jobGroup="";
        try {
            jobName = URLDecoder.decode(quartzJob.getJobDetailName(),Constants.DECODE_TYPE_UTF8);
            jobGroup = URLDecoder.decode(quartzJob.getGroupName(),Constants.DECODE_TYPE_UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error("格式不支持",e);
        }
        if("HeartBeatJob".equals(quartzJob.getJobDetailName())) {
    		startHeartHeat();
    		return ApiResponse.ok();
    	}else {
    		ScheduleUtils.resumeJob(scheduler,jobName, jobGroup);
    	}
        return ApiResponse.ok();
    }


    @GetMapping(value="/getAllJobs")
    @ApiOperation(value = "获取所有的定时任务", notes = "获取所有的定时任务")
    public ApiEntityResponse<List<Map<String,Object>>> getAllJobs(ApiPageRequest pageRequest)
    {
        List<Map<String, Object>> allJobs = ScheduleUtils.getAllJobs(scheduler);
        return ApiEntityResponse.ok(allJobs);
    }
    
    /**
           * 发送心跳
     */
    private void startHeartHeat() {
    	stopHeartHeat();
    	timerTask = new Timer();
		timerTask.schedule(new TimerTask() {
			@Override
			public void run() {
				log.info("心跳发送====》");
				try {
					linkageFegin.sendHartBeat();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, period*1000);
    }
    
    /**
           * 停止心跳
     */
    private void stopHeartHeat() {
    	try {
			if(timerTask != null) {
				timerTask.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @PostConstruct
    public void someMethod(){
    	startHeartHeat();
    } 
    
}
