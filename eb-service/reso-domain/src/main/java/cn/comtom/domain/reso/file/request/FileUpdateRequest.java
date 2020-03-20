package cn.comtom.domain.reso.file.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class FileUpdateRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "文件Id不能为空")
    private String id;

    @ApiModelProperty(value = "原文件名",required = true)
    private String originName;

    @ApiModelProperty(value = "文件夹ID")
    private String libId;

    @ApiModelProperty(value = "文本广播内容")
    private String fileText;

    @ApiModelProperty(value = "文件描述")
    private String fileDesc;

    @ApiModelProperty(value = "审核状态 ")
    private String auditState;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "审核时间")
    private Date auditDate;

    @ApiModelProperty(value = "审核人")
    private String auditUser;





}
