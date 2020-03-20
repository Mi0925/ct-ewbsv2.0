package cn.comtom.ebs.front.main.file.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:WJ
 * @date: 2018/12/29 0029
 * @time: 下午 2:43
 */
@Data
public class FileDownloadRequest {

    @ApiModelProperty(value = "文件ID")
    String fileId;

    @ApiModelProperty(value = "文件类型 1：数据包文件 2：媒体文件")
    String fileType;

}
