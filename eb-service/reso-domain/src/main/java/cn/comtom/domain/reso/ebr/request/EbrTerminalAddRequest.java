package cn.comtom.domain.reso.ebr.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class EbrTerminalAddRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "terminalEbrId不能为空")
    private String terminalEbrId;

    @ApiModelProperty(value = "终端资源名称")
    @NotEmpty(message = "terminalEbrName不能为空")
    private String terminalEbrName;

    @ApiModelProperty(value = "关联平台编号")
    private String relatedPsEbrId;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "终端状态（1:运行2:停止3:故障4:故障恢复）")
    private String terminalState;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "终端信息同步标识（1：未同步 2：已同步）")
    private String syncFlag;

    @ApiModelProperty(value = "终端状态同步标识（1：未同步 2：已同步）")
    private String statusSyncFlag;

}
