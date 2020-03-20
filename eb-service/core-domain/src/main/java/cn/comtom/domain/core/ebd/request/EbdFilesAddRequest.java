package cn.comtom.domain.core.ebd.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbdFilesAddRequest implements Serializable {

    @ApiModelProperty(value = "业务数据包ID")
    private String ebdId;

    @ApiModelProperty(value = "文件ID")
    private String fileId;

}
