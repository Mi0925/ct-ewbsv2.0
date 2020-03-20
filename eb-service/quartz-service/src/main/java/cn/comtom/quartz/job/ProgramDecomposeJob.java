package cn.comtom.quartz.job;

import cn.comtom.quartz.service.ICoreService;
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
 * @author:WJ
 * @date: 2019/4/8 0008
 * @time: 上午 10:53
 * 日常节目预处理，将周期性节目拆解成最小单元（广科院协议支持维度）
 *
 */
@Slf4j
@DisallowConcurrentExecution
public class ProgramDecomposeJob implements Job {

    private ICoreService coreService = (ICoreService) SpringContextUtils.getBean("coreServiceImpl");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("=====日常节目分解定时器开始====[{}]",new Date());
        ApiResponse response = coreService.programDecompose();
        log.info("=====日常节目分解定时器结束====[{}],result=[{}]",new Date(),response.getSuccessful());
    }
}
