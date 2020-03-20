package cn.comtom.quartz.job;

import cn.comtom.quartz.service.ILinkageService;
import cn.comtom.quartz.utils.SpringContextUtils;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 资源状态维护
 */
@Slf4j
public class ScanResourceStateJob implements Job {

    private ILinkageService linkageService = (ILinkageService) SpringContextUtils.getBean("linkageServiceImpl");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(log.isDebugEnabled()){
            log.debug("\"============================start scan resource state job====================================\"");
        }

        ApiResponse response = linkageService.scanResourceState();

        if(log.isDebugEnabled()){
            log.debug("=======scan resource state job end======== result=[{}]",response.getSuccessful());
        }
    }
}
