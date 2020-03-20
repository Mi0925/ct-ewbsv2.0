package cn.comtom.reso.main.ebr.controller;

import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.entity.dbo.EbrIdCreator;
import cn.comtom.reso.main.ebr.service.IEbrIdCreatorService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.EntityUtil;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/res/ebr/id/creator")
@Api(tags = "资源ID生成序列")
@Slf4j
public class EbrIdCreatorController extends BaseController {

    @Autowired
    private IEbrIdCreatorService ebrIdCreatorService;

    @GetMapping("/create")
    @ApiOperation(value = "根据EbrId查询终端资源信息", notes = "根据EbrId查询终端资源信息")
    public ApiEntityResponse<EbrIdCreatorInfo> create(@ModelAttribute EbrIdCreatorInfo creatorInfo){
        if(log.isDebugEnabled()){
            log.debug("[!~]create ebr Id. creatorInfo=[{}] " ,JSON.toJSONString(creatorInfo));
        }
        StringBuilder sb = new StringBuilder();
        String areaCode = creatorInfo.getAreaCode();
        //区域编码不足12位，用0补齐12位
        if(StringUtils.isNotBlank(areaCode) && areaCode.length() < 12){
            StringBuilder builder = new StringBuilder(areaCode);
            for(int i=0;i < 12 - areaCode.length();i++){
                builder.append("0");
            }
            areaCode = builder.toString();
        }
        sb.append(creatorInfo.getSourceLevel())
            .append(areaCode)
                .append(creatorInfo.getSourceTypeCode());
        EbrIdCreator ebrIdCreator = ebrIdCreatorService.getByCondition(creatorInfo);
        String sourceTypeCodeSN = "01";
        String sourceSubTypeCodeSn = "01";
        if(EntityUtil.isReallyEmpty(ebrIdCreator)){
            sourceTypeCodeSN = ebrIdCreatorService.getSourceTypeCodeSN(creatorInfo);
        }else{
            sourceTypeCodeSN = ebrIdCreator.getSourceTypeSN();
            Integer sn = Integer.valueOf(ebrIdCreator.getSourceSubTypeSN()) + 1;
            if(sn > 9 ){
                sourceSubTypeCodeSn = sn.toString();
            }else{
                sourceSubTypeCodeSn = "0"+sn;
            }
        }
        if(Integer.valueOf(sourceTypeCodeSN) > 99 || Integer.valueOf(sourceSubTypeCodeSn) > 99){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.OUT_OF_SEQUENCE_BOUNDS);
        }

        sb.append(sourceTypeCodeSN)
                .append(creatorInfo.getSourceSubTypeCode())
                .append(sourceSubTypeCodeSn);

        EbrIdCreator creator = new EbrIdCreator();
        BeanUtils.copyProperties(creatorInfo,creator);
        creator.setId(UUIDGenerator.getUUID());
        creator.setSourceTypeSN(sourceTypeCodeSN);
        creator.setSourceSubTypeSN(sourceSubTypeCodeSn);
        creator.setEbrId(sb.toString());
        ebrIdCreatorService.save(creator);
        EbrIdCreatorInfo ebrIdCreatorInfo = new EbrIdCreatorInfo();
        BeanUtils.copyProperties(creator,ebrIdCreatorInfo);
        return ApiEntityResponse.ok(ebrIdCreatorInfo);
    }


}
