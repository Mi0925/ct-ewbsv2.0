package cn.comtom.domain.system.accessnode.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccessNodeInfo {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "平台ID")
    private String platformId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "IP地址")
    private String ip;
}
