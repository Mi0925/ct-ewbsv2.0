package cn.comtom.linkage.main.access.untils;

import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.info.EbdResponseInfo;
import cn.comtom.domain.core.ebd.request.EbdAddRequest;
import cn.comtom.domain.core.ebd.request.EbdResponseAddRequest;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.main.access.constant.FileEnum;
import cn.comtom.linkage.main.access.constant.SendFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBDResponse;
import cn.comtom.linkage.main.access.model.ebd.ebm.DEST;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;
import cn.comtom.linkage.main.access.model.signature.Signature;
import cn.comtom.linkage.main.access.service.impl.SignValidateService;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.utils.*;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:WJ
 * @date: 2018/12/4 0004
 * @time: 下午 4:47
 */
@Component
@Slf4j
public class SendEbdUtil {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private SignValidateService signValidateService;

    /**
     * 发送数据包
     *
     * @param ebd
     * @return
     */
    public void sendEBD(EBD ebd, String requestURL) {
        if(StringUtils.isBlank(requestURL)){
            log.error("接收数据包的平台URL为空。");
            return;
        }
        // 发送文件的临时路径（根据文件类型区分文件夹）
        String sendFilePath =commonService.getSendTempPath(ebd.getEBDType());
        // 对象转换文件
        File file = FileUtil.converFile(sendFilePath, ebd);

        List<File> files = new ArrayList<>();
        files.add(file);

        //获取配置文件信息，是否进行签名
        String isSign = commonService.getSysParamValue(LinkageConstants.VERIFY_SIGN);
        if (StringUtils.equals("1", isSign)) {
            // 生成签名和签名文件
            Signature signature = signValidateService.buildSignature(file, ebd.getEBDID());//EBDModelBuild.buildSignature(file, ebd.getEBDID());
            File signFile = FileUtil.converFile(sendFilePath, signature);
    		files.add(signFile);
        }
        // 文件压缩
        File tar = TarFileUtil.compressorsTar(ebd, files, sendFilePath);
        // 接收文件保存路径
        String receviceFilePath = commonService.getReceiveTempPath();
        // 文件发送
        log.debug("======OMDPSInfo  sync===== requestURL=[{}]",requestURL);
        EBD ebdResponse = HttpRequestUtil.sendFile(tar, requestURL, receviceFilePath);

        //增加发送包上传到FastDFS
        String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(tar));
        OriginFileInfo originFileInfo = commonService.toFastDFS(FileEnum.FileTypeEnum.send_file.getType(),tar.getName(), tar.getAbsolutePath(),md5,EBDType.EBM.name());
        // 保存数据包记录
        recordEbd(ebd, originFileInfo.getFileId(), SendFlag.send,ebdResponse == null ? LinkageConstants.EBD_STATE_SEND_FAILED : LinkageConstants.EBD_STATE_SEND_SUCCESS);

        if (ebdResponse == null) {
            log.error("发送数据失败.");
            return;
        }
        // 接收数据包成功
        EBDResponse response = ebdResponse.getEBDResponse();
        if (response == null) {
            log.error("发送数据异常.");
            return;
        }

        //增加反馈包上传到FastDFS
        commonService.saveAndUploadResponseEbdPack(ebdResponse,StateDictEnum.EBD_STATE_SEND_SUCCESS.getKey(),CommonDictEnum.FLAG_RECEIVE.getKey());


        recordEbdResponse(ebd, response, SendFlag.receive);
    }

    /**
     * 保存接收发送数据包响应数据
     *
     * @param ebd
     * @param
     */
    public void recordEbdResponse(EBD ebd, EBDResponse eBDResponse, SendFlag sendFlag) {

        String ebdVersion = ebd.getEBDVersion();
        String ebdSrcEbrId = ebd.getSRC().getEBRID();
        String relatedEbdId = null;
        if (ebd.getRelatedEBD() != null) {
            relatedEbdId = ebd.getRelatedEBD().getEBDID();
        }
        // 必选
        String ebdId = ebd.getEBDID();
        // 必选
        String ebdType = ebd.getEBDType();
        // 必选
        String ebdTimeString = ebd.getEBDTime();
        Date ebdTime = DateTimeUtil.stringToDate(ebdTimeString,
                DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
        Integer resultCode = eBDResponse.getResultCode();
        String resultDesc = eBDResponse.getResultDesc();

        EbdResponseAddRequest addRequest=new EbdResponseAddRequest();
        addRequest.setEbdId(ebdId);
        addRequest.setEbdTime(ebdTime);
        addRequest.setEbdType(ebdType);
        addRequest.setResultCode(resultCode+"");
        addRequest.setResultDesc(resultDesc);
        addRequest.setSendFlag(sendFlag.getValue()+"");
        addRequest.setEbdVersion(ebdVersion);
        addRequest.setRelatedEbdId(relatedEbdId);
        addRequest.setEbdSrcEbrId(ebdSrcEbrId);
        EbdResponseInfo info= coreFeginService.saveEbdResponse(addRequest);
    }


    /**
     * 保存接收、发送据包记录
     *
     * @param ebd
     * @param fileId
     */
    public void recordEbd(EBD ebd, String fileId, SendFlag sendFlag, Integer ebdState) {
    	log.info("通用保存接收、发送EBD数据包记录，ebdid:{},fileId:{}",ebd.getEBDID(),fileId);
        // 必选
        String ebdVersion = ebd.getEBDVersion();
        // 必选
        String ebdId = ebd.getEBDID();
        // 必选
        String ebdType = ebd.getEBDType();
        // 必选
        String ebdTimeString = ebd.getEBDTime();
        Date ebdTime = DateTimeUtil.stringToDate(ebdTimeString,
                DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
        // 必选
        SRC src = ebd.getSRC();
        // 必选资源ID
        String ebdSrcEbrId = src.getEBRID();
        // 可选
        String ebdSrcUrl = src.getURL();
        // 可选
        DEST dest = ebd.getDEST();
        String ebdDestEbrId = null;
        if (dest != null) {
            // 目标对象的资源ID 必选
            ebdDestEbrId = dest.getEBRID();
        }
        // 可选
        RelatedEBD relatedEBD = ebd.getRelatedEBD();
        String relateEbdId = null;
        if (relatedEBD != null) {
            // 必选
            relateEbdId = relatedEBD.getEBDID();
        }

        EbdAddRequest addRequest=new EbdAddRequest();
        addRequest.setEbdId(ebdId);
        addRequest.setEbdVersion(ebdVersion);
        addRequest.setEbdType(ebdType);
        addRequest.setEbdName(FileNameUtil.generateTarFileName(ebdId));
        addRequest.setEbdSrcEbrId(ebdSrcEbrId);
        addRequest.setEbdDestEbrId(ebdDestEbrId);
        addRequest.setEbdTime(ebdTime);
        addRequest.setRelateEbdId(relateEbdId);
        addRequest.setEbdSrcUrl(ebdSrcUrl);
        addRequest.setSendFlag(sendFlag.getValue()+"");
        addRequest.setEbdState(ebdState+"");
        addRequest.setFileId(fileId);

        if (SendFlag.receive.equals(sendFlag)) {
            addRequest.setEbdRecvTime(new Date());
        } else if (SendFlag.send.equals(sendFlag)) {
            addRequest.setEbdSendTime(new Date());
        }
        addRequest.setFlowId(null);

        // 如果是EBM消息，设置EBD关联的EBM消息Id
        if (ebd.getEBM() != null) {
            addRequest.setEbmId(ebd.getEBM().getEBMID());
        }
        EbdInfo info=coreFeginService.saveEbd(addRequest);
    }

}
