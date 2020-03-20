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
 * 台站信息上报
 * @author:liuhy
 * @date: 2019/5/9
 * @time: 下午 2:15
 */
@Slf4j
@DisallowConcurrentExecution
public class EBRSTInfoReportJob implements Job {

    private ILinkageService linkageService = (ILinkageService) SpringContextUtils.getBean("linkageServiceImpl");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("=====台站信息资源上报开始====[{}]",new Date());

        ApiResponse response = linkageService.ebrstInfoReport();

        log.info("=====台站信息资源上报上报结束====[{}],result=[{}]",new Date(),response.getSuccessful());
    }
}
