package cn.comtom.domain.reso.file.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FileLibraryInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String libId;

    @ApiModelProperty(value = "文件夹名称")
    private String libName;

    @ApiModelProperty(value = "文件夹URI")
    private String libURI;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "父文件夹ID")
    private String parentLibId;

    @ApiModelProperty(value = "父文件夹名称")
    private String parentLibName;

    @ApiModelProperty(value = "文件夹类型")
    private String libType;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;


}
