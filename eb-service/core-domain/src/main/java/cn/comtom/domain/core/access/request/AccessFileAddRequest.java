package cn.comtom.domain.core.access.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class AccessFileAddRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "信息编号")
    @NotEmpty(message = "infoId can not be empty")
    private String infoId;

    @ApiModelProperty(value = "文件ID")
    @NotEmpty(message = "fileId can not be empty")
    private String fileId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;
}
