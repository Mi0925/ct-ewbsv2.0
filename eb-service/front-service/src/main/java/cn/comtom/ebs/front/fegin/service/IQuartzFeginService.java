package cn.comtom.ebs.front.fegin.service;

import cn.comtom.ebs.front.main.quartz.entity.QuartzJob;

import java.util.List;

public interface IQuartzFeginService {

    Boolean createQuartzJob(QuartzJob quartzJob);

    Boolean updateQuartzJob(QuartzJob quartzJob );


    Boolean pauseQuartzJob(QuartzJob quartzJob);


    Boolean deleteQuartzJob(QuartzJob quartzJob);


    Boolean resumeQuartzJob(QuartzJob quartzJob );


    List<QuartzJob> getAllJobs();
}
