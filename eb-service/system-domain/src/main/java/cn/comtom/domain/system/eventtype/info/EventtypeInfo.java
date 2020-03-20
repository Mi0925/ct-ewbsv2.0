package cn.comtom.domain.system.eventtype.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EventtypeInfo implements Serializable {

    @ApiModelProperty(value = "事件编码")
    private String eventCode;

    @ApiModelProperty(value = "事件描述")
    private String eventDesc;

    @ApiModelProperty(value = "父类编码")
    private String parentCode;

}
