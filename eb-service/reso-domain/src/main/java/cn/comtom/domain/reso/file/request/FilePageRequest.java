package cn.comtom.domain.reso.file.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FilePageRequest extends CriterionRequest implements Serializable {


    @ApiModelProperty(value = "原文件名")
    private String originName;

    @ApiModelProperty(value = "上传后的文件名")
    private String uploadedName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件FTP下载地址")
    private String fileUrl;

    @ApiModelProperty(value = "文件后缀")
    private String fileExt;

    @ApiModelProperty(value = "文件MD5值")
    private String md5Code;

    @ApiModelProperty(value = "文件夹ID")
    private String libId;

    @ApiModelProperty(value = "审核状态 ")
    private String auditState;


}
