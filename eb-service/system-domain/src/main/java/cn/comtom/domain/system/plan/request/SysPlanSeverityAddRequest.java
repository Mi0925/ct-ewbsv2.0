package cn.comtom.domain.system.plan.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysPlanSeverityAddRequest implements Serializable {

    @ApiModelProperty(value = "事件级别")
    private Integer severity;

}