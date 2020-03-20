package cn.comtom.domain.reso.ebr.request;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:liuhy
 * @date: 2019/5/10
 */
@Data
public class AdapterAddRequest implements Serializable {

	@ApiModelProperty(value = "主键")
    private String ebrAsId;

	@ApiModelProperty(value = "适配器名称")
    private String ebrAsName;

	@ApiModelProperty(value = "应急广播平台id")
    private String ebrpsId;

	@ApiModelProperty(value = "数据生产时间")
    private Date createTime;

	@ApiModelProperty(value = "网络地址")
    private String url;

	@ApiModelProperty(value = "经度")
    private String longitude;

	@ApiModelProperty(value = "纬度")
    private String latitude;

	@ApiModelProperty(value = "关联台站ID")
    private String ebrStId;
	
	@ApiModelProperty(value = "适配器信息同步标识（1：未同步 2：已同步）")
    private String syncFlag;

	@ApiModelProperty(value = "适配器状态同步标识（1：未同步 2：已同步）")
    private String statusSyncFlag;

	@ApiModelProperty(value = "适配器状态（1:运行2:停止3:故障4:故障恢复）")
    private String adapterState;

	@ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "平台类型")
    private String asType;

    @ApiModelProperty(value = "应急广播资源类型")
    private String resType;

    @ApiModelProperty(value = "应急广播子资源类型")
    private String subResType;
    
    private String srcHost;
    
	@ApiModelProperty(value = "资源级别")
    private String platLevel;
    
	@ApiModelProperty(value = "所属区域")
    private String areaCode;
	
	@ApiModelProperty(value = "资源下发渠道")
    private String resChannel;
}
