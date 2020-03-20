package cn.comtom.domain.core.ebd.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbdFilesInfo implements Serializable {

    @ApiModelProperty(value = "业务数据包ID")
    private String ebdId;

    @ApiModelProperty(value = "文件ID")
    private String fileId;

    @ApiModelProperty(value = "主键")
    private String id;

}
