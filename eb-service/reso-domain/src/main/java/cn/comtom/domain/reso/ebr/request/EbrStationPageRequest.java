package cn.comtom.domain.reso.ebr.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbrStationPageRequest extends CriterionRequest implements Serializable {


    @ApiModelProperty(value = "台站名称")
    private String ebrStName;

    @ApiModelProperty(value = "关联平台编号")
    private String ebrPsId;

    @ApiModelProperty(value = "台站状态（1:运行2:停止3:故障4:故障恢复）")
    private String stationState;

    @ApiModelProperty(value = "台站信息同步标识（1：未同步 2：已同步）")
    private String syncFlag;

    @ApiModelProperty(value = "台站状态同步标识（1：未同步 2：已同步）")
    private String statusSyncFlag;

    @ApiModelProperty(value = "查询类型（1：查询上级区域的终端 2：查询本级和下级区域的终端）")
    private String queryType;

    private String parentAreaCode;

    private String childAreaCode;

}
