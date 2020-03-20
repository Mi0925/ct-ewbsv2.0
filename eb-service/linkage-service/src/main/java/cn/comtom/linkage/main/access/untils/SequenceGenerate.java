package cn.comtom.linkage.main.access.untils;

import cn.comtom.domain.system.sequence.info.SysSequenceInfo;
import cn.comtom.domain.system.sequence.request.SequenceUpdateRequest;
import cn.comtom.linkage.main.fegin.SystemFegin;
import cn.comtom.tools.response.ApiEntityResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Create By wujiang on 2018/11/19
 */
@Component
public class SequenceGenerate {


    @Autowired
    SystemFegin systemFegin;

    /**
     * 生成ID
     *
     * @param idname
     * @return String
     */
    public synchronized String createId(String idname) {

        String id = "";
        // 获取Id当前信息
        ApiEntityResponse<SysSequenceInfo> sysSequenceResponse = systemFegin.getValue(idname);
        if (!sysSequenceResponse.getSuccessful()) {
            return null;
        }
        SysSequenceInfo sysSequence=sysSequenceResponse.getData();

        // Id
        Long idLong = Long.valueOf(sysSequence.getCurValue());
        idLong = idLong + 1; // 将当前值+1做为本次ID值
        if (idLong > new Long(sysSequence.getMaxValue())) {
            // 号源用完
            if (sysSequence.getIsCircul().equals("1")) {
                // 从最小值开始循环
                idLong = new Long(sysSequence.getMinValue());
            } else {
                // 溢出
                return null;
            }
        }
        id = String.valueOf(idLong);
        // 补足
        if (StringUtils.equals(sysSequence.getIsLeftpad(), "1")) {
            id = StringUtils.leftPad(id, sysSequence.getMaxValue().length(), "0");
        }
        sysSequence.setCurValue(id);
        // 回写配置表
        sysSequence.setFormatValue(id);
        SequenceUpdateRequest updateRequest=new SequenceUpdateRequest();
        BeanUtils.copyProperties(sysSequence,updateRequest);
        systemFegin.updateById(updateRequest);

        return id;
    }

}
