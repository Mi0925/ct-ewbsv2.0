package cn.comtom.ebs.front.fegin.service.impl;

import cn.comtom.ebs.front.fegin.QuartzFegin;
import cn.comtom.ebs.front.fegin.service.IQuartzFeginService;
import cn.comtom.ebs.front.fw.BaseServiceImpl;
import cn.comtom.ebs.front.main.quartz.entity.QuartzJob;
import cn.comtom.tools.response.ApiEntityResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuartzServiceImpl extends BaseServiceImpl implements IQuartzFeginService {


    @Autowired
    private QuartzFegin quartzFegin;


    @Override
    public Boolean createQuartzJob(QuartzJob quartzJob) {
        return getBoolean(quartzFegin.createQuartzJob(quartzJob));
    }

    @Override
    public Boolean updateQuartzJob(QuartzJob quartzJob) {
        return getBoolean(quartzFegin.updateQuartzJob(quartzJob));
    }

    @Override
    public Boolean pauseQuartzJob(QuartzJob quartzJob) {
        return getBoolean(quartzFegin.pauseQuartzJob(quartzJob));
    }

    @Override
    public Boolean deleteQuartzJob(QuartzJob quartzJob) {
        return getBoolean(quartzFegin.deleteQuartzJob(quartzJob));
    }

    @Override
    public Boolean resumeQuartzJob(QuartzJob quartzJob) {
        return getBoolean(quartzFegin.resumeQuartzJob(quartzJob));
    }

    @Override
    public List<QuartzJob> getAllJobs() {
        ApiEntityResponse<List<Map<String,Object>>> response = quartzFegin.getAllJobs();
        List<Map<String,Object>> list = response.getData();
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(map ->{
                    QuartzJob quartzJob = new QuartzJob();
                    try {
                        BeanUtils.populate(quartzJob,map);
                    }  catch (Exception e) {
                        log.error("map转QuartzJob对象异常",e);
                    }
                    return quartzJob;
   /*                 quartzJob.setGroupName(map.get("groupName") ==null?null:map.get("groupName").toString());
                    quartzJob.setJobClassName(map.get("jobClassName") ==null?null:map.get("jobClassName").toString());
                    quartzJob.setJobCronExpression(map.get("jobCronExpression") ==null?null:map.get("jobCronExpression").toString());
                    quartzJob.setJobDescription(map.get("jobDescription") ==null?null:map.get("jobDescription").toString());
                    quartzJob.setGroupName(map.get("groupName") ==null?null:map.get("groupName").toString());
                    quartzJob.setGroupName(map.get("groupName") ==null?null:map.get("groupName").toString());
                    quartzJob.setGroupName(map.get("groupName") ==null?null:map.get("groupName").toString());*/
                })
                .sorted(Comparator.comparing(QuartzJob::getJobDetailName))
                .collect(Collectors.toList());

    }
}
