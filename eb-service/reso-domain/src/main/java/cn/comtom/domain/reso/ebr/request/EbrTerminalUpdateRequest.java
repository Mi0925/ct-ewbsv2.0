package cn.comtom.domain.reso.ebr.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EbrTerminalUpdateRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    private String terminalEbrId;

    @ApiModelProperty(value = "终端资源名称")
    private String terminalEbrName;

    @ApiModelProperty(value = "关联平台编号")
    private String relatedPsEbrId;

    @ApiModelProperty(value = "管理平台名称")
    private String psEbrName;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "终端状态（1:运行2:停止3:故障4:故障恢复）")
    private String terminalState;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "终端信息同步标识（1：未同步 2：已同步）")
    private String syncFlag;

    @ApiModelProperty(value = "终端状态同步标识（1：未同步 2：已同步）")
    private String statusSyncFlag;

}
