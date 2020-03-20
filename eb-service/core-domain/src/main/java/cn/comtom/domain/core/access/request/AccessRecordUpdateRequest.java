package cn.comtom.domain.core.access.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class AccessRecordUpdateRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "id can not be empty")
    private String id;

    @ApiModelProperty(value = "接入节点")
    private String nodeId;

    @ApiModelProperty(value = "接入状态")
    private String status;

    @ApiModelProperty(value = "关联文件ID")
    private String fileId;

    @ApiModelProperty(value = "信息类型")
    private String type;


}
