package cn.comtom.domain.core.ebd.request;

import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class EbdFilesAddBatchRequest implements Serializable {

    @ApiModelProperty(value = "业务数据包ID")
    @NotEmpty(message = "ebdFilesInfoList 不能为空！")
    private List<EbdFilesInfo> ebdFilesInfoList;


}
