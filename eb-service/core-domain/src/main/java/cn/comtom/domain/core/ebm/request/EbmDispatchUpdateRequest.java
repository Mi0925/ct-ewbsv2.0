package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class EbmDispatchUpdateRequest implements Serializable {


    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "dispatchId 不能为空")
    private String dispatchId;

    @ApiModelProperty(value = "状态 调度分发状态（0:待调度 1:已调度 2:调度失败）")
    private String state;

    @ApiModelProperty(value = "调度分发时间")
    private Date dispatchTime;

    @ApiModelProperty(value = "响应时间")
    private Date responseTime;

    @ApiModelProperty(value = "消息匹配后生成的EBD数据包Id")
    private String matchEbdId;

    @ApiModelProperty(value = "失败次数")
    private Integer failCount;

}
