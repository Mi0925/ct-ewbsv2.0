package cn.comtom.quartz.job;

import cn.comtom.quartz.service.ILinkageService;
import cn.comtom.quartz.utils.SpringContextUtils;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class HeartBeatJob implements Job {

    private ILinkageService linkageService = (ILinkageService) SpringContextUtils.getBean("linkageServiceImpl");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(log.isDebugEnabled()){
            log.debug("\"============================start hartBeat job====================================\"");
        }

        ApiResponse response = linkageService.sendHartBeat();

        if(log.isDebugEnabled()){
            log.debug("=======hartBeat job end======== result=[{}]",response.getSuccessful());
        }
    }
}
