package cn.comtom.domain.reso.file.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OriginFilePageRequest extends CriterionRequest implements Serializable {


    @ApiModelProperty(value = "原文件名")
    private String originName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件后缀")
    private String fileExt;

    @ApiModelProperty(value = "文件MD5值")
    private String md5Code;

    @ApiModelProperty(value = "文件状态")
    private String status;


}
