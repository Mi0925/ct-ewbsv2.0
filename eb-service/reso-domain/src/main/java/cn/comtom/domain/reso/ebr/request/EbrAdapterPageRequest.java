package cn.comtom.domain.reso.ebr.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbrAdapterPageRequest extends CriterionRequest implements Serializable {


    @ApiModelProperty(value = "适配器名称")
    private String ebrAsName;

    @ApiModelProperty(value = "关联平台编号")
    private String ebrpsId;

    @ApiModelProperty(value = "适配器状态（1:运行2:停止3:故障4:故障恢复）")
    private String adapterState;

    @ApiModelProperty(value = "适配器信息同步标识（1：未同步 2：已同步）")
    private String syncFlag;

    @ApiModelProperty(value = "适配器状态同步标识（1：未同步 2：已同步）")
    private String statusSyncFlag;

    @ApiModelProperty(value = "查询类型（1：查询上级区域的终端 2：查询本级和下级区域的终端）")
    private String queryType;

    private String parentAreaCode;

    private String childAreaCode;

}
