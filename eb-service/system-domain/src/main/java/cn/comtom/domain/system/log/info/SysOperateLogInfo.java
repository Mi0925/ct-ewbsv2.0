package cn.comtom.domain.system.log.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysOperateLogInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String logId;

    @ApiModelProperty(value = "操作人员")
    private String operator;

    @ApiModelProperty(value = "模块")
    private String module;

    @ApiModelProperty(value = "操作类型简介")
    private String operation;

    @ApiModelProperty(value = "操作具体描述")
    private String description;

    @ApiModelProperty(value = "日志记录时间")
    private Date logTime;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "客户端IP")
    private String clientIp;
}
