package cn.comtom.domain.core.ebm.request;

import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EbmAuxiliaryAddBatchRequest implements Serializable {

    @ApiModelProperty(value = "辅助数据类型列表")
    private List<EbmAuxiliaryInfo> auxiliaryInfoList;


}
