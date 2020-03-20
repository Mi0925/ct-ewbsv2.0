package cn.comtom.linkage.main.monitor.controller;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.fw.BaseController;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.other.ConnectionCheck;
import cn.comtom.linkage.main.access.model.ebd.ebm.DEST;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;
import cn.comtom.linkage.main.access.model.signature.Signature;
import cn.comtom.linkage.main.access.service.impl.SignValidateService;
import cn.comtom.linkage.main.access.untils.HttpRequestUtil;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.main.monitor.service.IMonitorService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.FileUtil;
import cn.comtom.linkage.utils.TarFileUtil;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.constants.RedisKeyConstants;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 *   信息接入入口
 */
@RestController
@RequestMapping(value = "linkage/monitor")
@Slf4j
@Api(tags = "资源监控管理", description = "资源监控管理接口")
public class MonitorController extends BaseController {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private IMonitorService monitorService;

    @Autowired
    private IResoFeginService resoFeginService;
    
    @Autowired
    private SignValidateService signValidateService;


    @RequestMapping("/scanResourceStatus")
    @ApiOperation(value = "资源状态维护", notes = "资源状态维护")
    public void scanResourceStatus() {
        if(log.isDebugEnabled()){
            log.debug("############  开始 对系统资源状态进行扫描 ################## 时间：[{}]",DateUtil.getDateTime());
        }
        Set<String> keySet = stringRedisTemplate.keys(RedisKeyConstants.CONNECTION_CHECK_KEY_PREFIX + SymbolConstants.PATTERN_START);
        List<EbrStateUpdateRequest> ebrStateUpdateRequestList = new ArrayList<>();
        Optional.ofNullable(keySet).orElse(Collections.emptySet())
                .stream()
                .filter(Objects::nonNull)
                .filter(key -> {
                    String[] arr = key.split(SymbolConstants.REDIS_KEY_SPLIT);
                    if(arr.length == 2){
                        return true;
                    }
                    return false;
                })
                .forEach(key ->{
                    String ebrId = key.split(SymbolConstants.REDIS_KEY_SPLIT)[1];
                    String value = stringRedisTemplate.opsForValue().get(key);
                    //当前时间
                    Long currentTimes = new Date().getTime();
                    Long rptTimes = value == null ? 0L :DateTimeUtil.stringToDate(value, "yyyy-MM-dd HH:mm:ss").getTime();

                    //资源状态定时扫描间隔
                    String scanTimeValue = commonService.getSysParamValue(Constants.STATE_SCAN_INTERVAL);
                    Long scanTime = scanTimeValue == null ? 0L : Long.valueOf(scanTimeValue)*1000;

                    //资源监控状态缓存失效时长
                    String expire = commonService.getSysParamValue(Constants.CONNECT_CHECK_EXPIRE_DURATION);
                    Long expireTimes = expire == null ? 0L : Long.valueOf(expire) * 1000;

                    //if(currentTimes - rptTimes < scanTime * 3 ){
                        //最后一次上报时间大于扫描时间的3倍，说明资源一直处于下线状态，不用更新状态。
                        EbrStateUpdateRequest request = new EbrStateUpdateRequest();
                        request.setEbrId(ebrId);
                        if (currentTimes - rptTimes > 3 * expireTimes){
                            //上报状态已经过期
                            request.setState(Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey()));
                        }else{
                            //上报状态未过期
                            request.setState(Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey()));
                        }
                        //状态修改后，设置同步状态为，未同步。以便将最新状态同步到上级平台
                        request.setSyncFlag(SyncFlag.nosync.getValue());
                        ebrStateUpdateRequestList.add(request);
                   // }

                });
        log.debug("需要更新的记录总数：[{}]",ebrStateUpdateRequestList.size());
        monitorService.updateResourceState(ebrStateUpdateRequestList);


    }

    @RequestMapping("/sendHartBeat")
    @ApiOperation(value = "发送心跳包", notes = "发送心跳包")
    public void sendHartBeat() {
        try {
			//心跳包发送的上级平台URL
			String parentUrl = commonService.getParentPlatUrl();
			//查询本级平台信息
			String localPlatformId = commonService.getEbrPlatFormID();
			EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(localPlatformId);
			
			//从本地获取心跳数据包，心跳包存在则在原有数据包上修改，不存在则创建数据包
			String filePath = commonService.getSendTempPath(EBDType.ConnectionCheck.name());
			FileUtil.deleteFile(new File(filePath));
			File tarFile = null;
			File xmlFile = null;
			File tempFile = new File(filePath );
			File[] files = tempFile.listFiles();
			List<File> fileList = null;
			if(files != null && files.length > 0){
			    fileList = Arrays.asList(files);
			    fileList = fileList.stream()
			            .filter(File::isFile)
			            .collect(Collectors.toList());
			}

			if(fileList != null && !fileList.isEmpty()){
			    for(File file : fileList){
			        int i = file.getName().lastIndexOf(SymbolConstants.SUFFIX_SPLIT + TypeDictEnum.FILE_TYPE_TAR.getDesc());
			        int j = file.getName().lastIndexOf(SymbolConstants.SUFFIX_SPLIT + TypeDictEnum.FILE_TYPE_XML.getDesc());
			        if(tarFile == null && i > 0){
			            tarFile = file;
			        }else if(xmlFile == null && j > 0){
			            xmlFile = file;
			        }
			    }
			}

			EBD ebd = new EBD();
			String dateString = DateUtil.getDateTime();
			String ebdId = "0000000000000000";
			ebd.setEBDID(Constants.EBD_HB_TYPE + ebrPlatformInfo.getPsEbrId() + ebdId);
			ebd.setEBDVersion(CommonDictEnum.EBM_VERSION_INIT.getKey());
			ConnectionCheck connectionCheck = new ConnectionCheck();
			connectionCheck.setRptTime(dateString);
			ebd.setConnectionCheck(connectionCheck);
			ebd.setEBDType(EBDType.ConnectionCheck.name());
			SRC src = new SRC();
			src.setEBRID(localPlatformId);
			src.setURL(ebrPlatformInfo.getPsUrl());
			ebd.setSRC(src);
			DEST dest = new DEST();
			dest.setEBRID(ebrPlatformInfo.getParentPsEbrId());
			ebd.setDEST(dest);
			ebd.setEBDTime(dateString);
			File file = null;
			if(xmlFile == null){
			    //xml文件不存在，则重新生成xml文件
			    file = FileUtil.converFile(filePath, ebd);
			}else{
			    //xml文件存在，则覆盖原xml文件
			    file = FileUtil.converFile(filePath, ebd,xmlFile.getName());
			}
			if(file == null){
			    return;
			}
			List<File> fileList1 = Lists.newArrayList(file);
			String isSign = commonService.getSysParamValue(LinkageConstants.VERIFY_SIGN);
	        if (StringUtils.equals("1", isSign)) {
	            // 生成签名和签名文件============
	            Signature signature = signValidateService.buildSignature(file, ebd.getEBDID());
	            File signFile = FileUtil.converFile(filePath, signature);
	            fileList1.add(signFile);
	        }
			if(tarFile != null){
			    //心跳tar包存在，则覆盖原有tar包
			    tarFile = TarFileUtil.compressorsTar(fileList1, filePath,tarFile.getName());
			}else{
			    //心跳tar包不存在，则重新生成心跳tar包
			    tarFile = TarFileUtil.compressorsTar(ebd,fileList1, filePath);
			}
			if(log.isDebugEnabled()){
			    log.debug("$=: send hartBeat package .ebdPackage=[{}] ",JSON.toJSONString(ebd));
			}
			HttpRequestUtil.sendFile(tarFile, parentUrl, filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

}
