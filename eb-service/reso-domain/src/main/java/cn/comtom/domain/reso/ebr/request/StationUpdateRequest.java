package cn.comtom.domain.reso.ebr.request;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:liuhy
 * @date: 2019/5/9
 */
@Data
public class StationUpdateRequest implements Serializable {

	@ApiModelProperty(value = "主键")
	@NotEmpty(message = "ebrStId 不能为空！")
    private String ebrStId;

	@ApiModelProperty(value = "台站名称")
    private String ebrStName;

	@ApiModelProperty(value = "关联应急广播平台ID")
    private String ebrPsId;

	@ApiModelProperty(value = "数据操作生成时间")
    private Date createTime;

	@ApiModelProperty(value = "台站地址")
    private String address;

	@ApiModelProperty(value = "联系人")
    private String contact;

	@ApiModelProperty(value = "联系电话")
    private String phoneNumber;

	@ApiModelProperty(value = "平台经度")
    private String longitude;

	@ApiModelProperty(value = "平台纬度")
    private String latitude;
	
	@ApiModelProperty(value = "终端信息同步标识（1：未同步 2：已同步）")
    private String syncFlag;

	@ApiModelProperty(value = "终端状态同步标识（1：未同步 2：已同步）")
    private String statusSyncFlag;

	@ApiModelProperty(value = "台站状态（1:运行2:停止3:故障4:故障恢复）")
    private String stationState;

	@ApiModelProperty(value = "更新时间")
    private Date updateTime;

	private String srcHost;
	
    @ApiModelProperty(value = "平台类型")
    private String stType;

    @ApiModelProperty(value = "应急广播资源类型")
    private String resType;

    @ApiModelProperty(value = "应急广播子资源类型")
    private String subResType;
    
	@ApiModelProperty(value = "资源级别")
    private String platLevel;
    
	@ApiModelProperty(value = "所属区域")
    private String areaCode;
	
	@ApiModelProperty(value = "资源下发渠道")
    private String resChannel;
}
