package cn.comtom.domain.reso.ebr.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @author:WJ
 * @date: 2018/12/5 0005
 * @time: 上午 10:42
 */
@Data
public class PlatformUpdateRequest {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "psEbrId 不能为空！")
    private String psEbrId;

    @ApiModelProperty(value = "平台网络地址")
    private String psUrl;

    @ApiModelProperty(value = "平台名称")
    private String psEbrName;

    @ApiModelProperty(value = "平台状态")
    private Integer psState;

    @ApiModelProperty(value = "平台覆盖区域编码")
    private String areaCode;

    @ApiModelProperty(value = "平台类型")
    private String psType;
    
	private String resType;

	private String subResType;

    @ApiModelProperty(value = "父平台资源编号")
    private String parentPsEbrId;

    @ApiModelProperty(value = "平台地址")
    private String psAddress;

    @ApiModelProperty(value = "联系人名称")
    private String contact;

    @ApiModelProperty(value = "联系人电话")
    private String phoneNumber;

    @ApiModelProperty(value = "平台经度")
    private String longitude;

    @ApiModelProperty(value = "平台纬度")
    private String latitude;

    @ApiModelProperty(value = "平台级别")
    private String platLevel;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否已同步标识")
    private Integer syncFlag;

    @ApiModelProperty(value = "覆盖面积")
    private Double square;

    @ApiModelProperty(value = "覆盖人口")
    private Double population;

    @ApiModelProperty(value = "平台状态同步标识")
    private Integer statusSyncFlag;

    private String srcHost;

    private String remark;

    private String orgName;

    private String orgType;
    
    @ApiModelProperty(value = "资源下发渠道")
    private String resChannel;
}
