package cn.comtom.quartz.job;

import cn.comtom.quartz.service.ILinkageService;
import cn.comtom.quartz.utils.SpringContextUtils;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class EbmDispatchJob implements Job {

    private ILinkageService linkageService = (ILinkageService) SpringContextUtils.getBean("linkageServiceImpl");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(log.isDebugEnabled()){
            log.debug("\"============================start ebm dispatch job====================================\"");
        }

        ApiResponse response = linkageService.dispatch();

        if(log.isDebugEnabled()){
            log.debug("=======ebm dispatch job end======== result:[{}]",response.getSuccessful());
        }
    }
}
