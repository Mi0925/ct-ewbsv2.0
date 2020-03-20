package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EbmAuxiliaryAddRequest implements Serializable {

    @ApiModelProperty(value = "辅助数据类型")
    private String auxiliaryType;

    @ApiModelProperty(value = "辅助数据描述")
    private String auxiliaryDesc;

    @ApiModelProperty(value = "辅助数据文件大小")
    private Integer auxiliarySize;

    @ApiModelProperty(value = "辅助数据文件摘要")
    private String auxiliaryDigest;

    @ApiModelProperty(value = "消息ID")
    @NotEmpty(message = "ebmId 不能为空")
    private String ebmId;

}
