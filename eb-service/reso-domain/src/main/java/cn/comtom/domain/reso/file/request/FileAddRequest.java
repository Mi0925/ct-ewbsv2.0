package cn.comtom.domain.reso.file.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class FileAddRequest implements Serializable {


    @ApiModelProperty(value = "原文件名",required = true)
    @NotEmpty(message = "originName must not be empty")
    private String originName;

    @ApiModelProperty(value = "上传后的文件名",required = true)
    @NotEmpty(message = "uploadedName must not be empty")
    private String uploadedName;

    @ApiModelProperty(value = "文件类型",required = true)
    @NotEmpty(message = "fileType must not be empty")
    private String fileType;

    @ApiModelProperty(value = "文件后缀",required = true)
    @NotEmpty(message = "fileExt must not be empty")
    private String fileExt;

    @ApiModelProperty(value = "文件MD5值",required = true)
    @NotEmpty(message = "md5Code must not be empty")
    private String md5Code;

    @ApiModelProperty(value = "文件播放时长，单位秒")
    private Integer secondLength;

    @ApiModelProperty(value = "文件大小，单位字节")
    private Long byteSize;

    @ApiModelProperty(value = "文件夹ID")
    private String libId;

    @ApiModelProperty(value = "文本广播内容")
    private String fileText;

    @ApiModelProperty(value = "文件描述")
    private String fileDesc;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "文件虚拟路径")
    private String fileUrl;

    @ApiModelProperty(value = "文件审核状态")
    private String auditState;

    @ApiModelProperty(value = "文件fastdfs路径")
    private String filePath;
}
