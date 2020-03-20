package cn.comtom.domain.reso.file.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class FileLibraryUpdateRequest implements Serializable {


    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "libId不能为空")
    private String libId;

    @ApiModelProperty(value = "文件夹名称")
    private String libName;

    @ApiModelProperty(value = "文件夹URI")
    private String libURI;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "父文件夹ID")
    private String parentLibId;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @ApiModelProperty(value = "文件夹类型")
    private String libType;



}
