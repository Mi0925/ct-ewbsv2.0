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
 * 终端资源状态
 * @author:WJ
 * @date: 2019/3/5 0005
 * @time: 下午 2:15
 */
@Slf4j
@DisallowConcurrentExecution
public class EBRDTStateReportJob implements Job {

    private ILinkageService linkageService = (ILinkageService) SpringContextUtils.getBean("linkageServiceImpl");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("=====终端资源状态信息主动上报开始====[{}]",new Date());

        ApiResponse response = linkageService.ebrdtStateReport();

        log.info("=====终端资源状态信息主动上报结束====[{}],result=[{}]",new Date(),response.getSuccessful());

    }
}
