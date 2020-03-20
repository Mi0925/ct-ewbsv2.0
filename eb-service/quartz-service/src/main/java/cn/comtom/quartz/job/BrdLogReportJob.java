package cn.comtom.quartz.job;

import cn.comtom.quartz.service.ILinkageService;
import cn.comtom.quartz.utils.SpringContextUtils;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * 平台资源状态
 * @author:WJ
 * @date: 2019/3/5 0005
 * @time: 下午 2:15
 */
@Slf4j
@DisallowConcurrentExecution
public class BrdLogReportJob implements Job {

    private ILinkageService linkageService = (ILinkageService) SpringContextUtils.getBean("linkageServiceImpl");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("=====播发记录主动上报开始====[{}]",new Date());

        ApiResponse response = linkageService.brdLogReport();

        log.info("=====播发记录主动上报开始====[{}],result=[{}]",new Date(),response.getSuccessful());

    }
}
