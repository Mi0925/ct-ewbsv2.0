package cn.comtom.domain.reso.file.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FileLibraryAddRequest implements Serializable {


    @ApiModelProperty(value = "文件夹名称")
    private String libName;

    @ApiModelProperty(value = "文件夹URI")
    private String libURI;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "父文件夹ID")
    private String parentLibId;

    @ApiModelProperty(value = "文件夹类型")
    private String libType;


}
