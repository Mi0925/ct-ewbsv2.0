package cn.comtom.core.main.quartz.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.core.main.program.service.IProgramService;
import cn.comtom.core.main.program.service.IProgramUnitService;
import cn.comtom.core.main.quartz.service.IQuartzService;
import cn.comtom.domain.core.program.info.ProgramDecomposeInfo;
import cn.comtom.domain.core.program.info.ProgramFilesInfo;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.request.EbmCreateRequest;
import cn.comtom.domain.core.quartz.QuartzInfo;
import cn.comtom.domain.core.quartz.QuartzPageRequest;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/core/quartz")
@Api(tags = "核心定时调度")
@Slf4j
public class QuartzController extends BaseController {

    @Autowired
    private IProgramService programService;

    @Autowired
    private IProgramUnitService programUnitService;
    
    @Autowired
    private IQuartzService quartzService;

    @GetMapping("/programDecompose")
    @ApiOperation(value = "节目预处理", notes = "对周期性节目进行拆解")
    public ApiResponse programDecompose() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        for(int i=0;i<7;i++){
            if(i!=0)c.add(Calendar.DAY_OF_MONTH, 1);
            String date = sdf.format(c.getTime());
            Date cuDate=null;
            try {
             cuDate= sdf.parse(date);
            }catch (Exception e){ }
            List<ProgramDecomposeInfo> list= programService.findprogramDecomposeByDate(date);
            log.info("【programDecompose】~节目预处理拆解日期："+date+"  符合条件数据(未去除周循环判断)有"+list.size()+"条");
            for(ProgramDecomposeInfo p:list){
                if(p.getStrategyType()==null)continue;

                if(p.getStrategyType().equals(Constants.PROGRAM_STRATEGY_TYPE_WEEK)){
                    //每周的需要判断当前日期是否是周循环内的日期
                   boolean flag= containcWeek(cuDate,p.getWeekMask());
                   if(!flag)continue;
                }
                if(p.getStrategyType().equals(Constants.PROGRAM_STRATEGY_TYPE_MONTH)) {
                	 //每月的需要判断当前日期是否是月循环内的日期
                	boolean flag= containcMonth(cuDate, p.getDayOfMonth());
                    if(!flag)continue;
                }
                ProgramUnit programUnit =decomposeInfoToProgramUnit(p,cuDate);

                //判断当前任务是否是按次，如果是，则需要计算播放结束时间
                if(null!=p.getDurationTime()){
                    //播放时长=次数*文件长度
                    ProgramInfo programInfo = programService.getProgramInfoById(p.getProgramId());
                    Integer secondLength=0;
                    //1、当前节目内容类型为文本，则直接获取节目内容表中的长度。
                    if(null!=p.getContentType()&&p.getContentType().equals(Constants.PROGRAM_CONTENT_TYPE_TXT)){
                        secondLength=programInfo.getProgramContent().getSecondLength();
                    }else{
                        //2、当前节目为文件，则叠加文件时长。
                        List<ProgramFilesInfo> fileList = programInfo.getFilesList();
                        for(ProgramFilesInfo f:fileList){
                            if(f.getSecondLength()!=null) secondLength+=f.getSecondLength();
                        }
                    }
                    String endTime=getEndTime(p.getStartTime(),p.getDurationTime()*secondLength);
                    programUnit.setEndTime(endTime);
                }

                if(null!=programUnit){
                    programUnitService.save(programUnit);
                }

                //判断如果是应急广播节目，则需要直接调用审核方法
                if(p.getProgramType().equals(Constants.PROGRAM_TYPE_YJ) || p.getProgramType().equals(Constants.PROGRAM_TYPE_YL)){
                    log.info("【programDecompose】~节目预处理，应急节目自动审核");
                    

                    EbmCreateRequest request=new EbmCreateRequest();
                    request.setUnitIds(Arrays.asList((programUnit.getId())));
                    programUnitService.createEbms(request);
                    //审核
                    programUnit.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
                    programUnit.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
                    programUnit.setAuditTime(new Date());
                    programUnit.setAuditUser(Constants.DEFAULT_USER);
                    programUnitService.update(programUnit);
                }

            }
        }
        return ApiResponse.ok();
    }


    private boolean containcMonth(Date cuDate, Integer dayofMonth) {
    	if(dayofMonth==null) {
    		return false;
    	}
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(cuDate);
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	
		return day==dayofMonth;
	}


	/**
     * 根据开始时间计算结束时间
     * @param startTime
     * @param secondLength
     * @return
     */
    private String getEndTime(String startTime, int secondLength) {
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
            try{
                Date startDate = sdf.parse(startTime);
                long start = startDate.getTime();
                start = start + secondLength*1000;
                Date endDate = new Date();
                endDate.setTime(start);
                return sdf.format(endDate);
            }catch (Exception e){}
            return startTime;
     }

    /**
     * 判断当前日期是否在周循环内
     * @param cuDate
     * @param weekMask
     * @return
     */
    private boolean containcWeek(Date cuDate, Integer weekMask) {
        if(null==weekMask)return false;
        try {
            Integer index = dateToWeek(cuDate);
            String mask=weekMask.toString();
            //先判断当前长度是否小于7位，小于则补零
            if(mask.length()<7){
                Integer len=mask.length();
                for(int i=0;i<7-len;i++){
                    mask="0"+mask;
                }
            }
            if(mask.substring(index,index+1).equals("1")){
                return true;
            }
        }catch (Exception e){
        }
        return false;
    }

    /**
     * 日期转星期
     *
     * @param date
     * @return
     */
    public static Integer dateToWeek(Date date) {
        Integer[] weekDays = { 0, 6, 5, 4, 3, 2, 1 };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    private ProgramUnit decomposeInfoToProgramUnit(ProgramDecomposeInfo p,Date cuDate) {
        ProgramUnit programUnit =new ProgramUnit();
        programUnit.setTimeId(p.getTimeId());
        programUnit.setProgramId(p.getProgramId());
        programUnit.setStartTime(p.getStartTime());
        programUnit.setPlayDate(cuDate);
        programUnit.setEndTime(p.getOverTime());
        programUnit.setId(UUIDGenerator.getUUID());
        if(p.getProgramType().equals(Constants.PROGRAM_TYPE_YJ)){
            programUnit.setState(StateDictEnum.PROGRAM_UNIT_STATE_DONE.getKey());
        }else {
            programUnit.setState(StateDictEnum.PROGRAM_UNIT_STATE_CREATE.getKey());
        }
        programUnit.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
        return programUnit;
    }
    
    @GetMapping("/getJobPage")
    @ApiOperation(value = "分页查询定时任务信息", notes = "分页查询定时任务信息")
    public ApiPageResponse<QuartzInfo> getJobPage(@ModelAttribute QuartzPageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get job info by page. request = [{}]",request);
        }
        startPage(request);
        List<QuartzInfo> quartzInfoList = quartzService.findQuartzInfoList(request);
        return ApiPageResponse.ok(quartzInfoList,page);
    } 
}
