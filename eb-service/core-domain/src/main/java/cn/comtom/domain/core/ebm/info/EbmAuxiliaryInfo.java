package cn.comtom.domain.core.ebm.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbmAuxiliaryInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String auxiliaryId;

    @ApiModelProperty(value = "辅助数据类型")
    private String auxiliaryType;

    @ApiModelProperty(value = "辅助数据描述")
    private String auxiliaryDesc;

    @ApiModelProperty(value = "辅助数据文件大小")
    private Integer auxiliarySize;

    @ApiModelProperty(value = "辅助数据文件摘要")
    private String auxiliaryDigest;

    @ApiModelProperty(value = "消息ID")
    private String ebmId;

}
