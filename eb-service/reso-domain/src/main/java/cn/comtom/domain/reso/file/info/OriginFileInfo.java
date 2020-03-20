package cn.comtom.domain.reso.file.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class OriginFileInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String fileId;

    @ApiModelProperty(value = "原文件名",required = true)
    @NotEmpty(message = "originName must not be empty")
    private String originName;

    @ApiModelProperty(value = "上传后的文件名",required = true)
    @NotEmpty(message = "url must not be empty")
    private String url;

    @ApiModelProperty(value = "文件类型",required = true)
    @NotEmpty(message = "fileType must not be empty")
    private String fileType;

    @ApiModelProperty(value = "文件后缀",required = true)
    @NotEmpty(message = "fileExt must not be empty")
    private String fileExt;

    @ApiModelProperty(value = "文件MD5值",required = true)
    @NotEmpty(message = "md5Code must not be empty")
    private String md5Code;

    @ApiModelProperty(value = "文件描述")
    private String fileDesc;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "文件创建日期")
    private Date createTime;

    @ApiModelProperty(value = "文件状态")
    private String status;





}
