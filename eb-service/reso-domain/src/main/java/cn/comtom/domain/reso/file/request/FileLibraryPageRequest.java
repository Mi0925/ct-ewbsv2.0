package cn.comtom.domain.reso.file.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FileLibraryPageRequest extends CriterionRequest implements Serializable {


    @ApiModelProperty(value = "文件夹名称")
    private String libName;

    @ApiModelProperty(value = "父文件夹ID")
    private String parentLibId;

    @ApiModelProperty(value = "文件夹类型")
    private String libType;


}
