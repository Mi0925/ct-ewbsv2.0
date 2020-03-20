package cn.comtom.domain.system.org;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class OrganizationInfo {
	
	@ApiModelProperty(value = "主键")
	private String id;
    
    @ApiModelProperty(value = "单位名称")
    private String companyName;
    
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;
    
    @ApiModelProperty(value = "联系人电话")
    private String contactTel;
    
    @ApiModelProperty(value = "所属区域编码id")
    private String areaId;

    @ApiModelProperty(value = "所属区域名称")
    private String areaName;
    
    @ApiModelProperty(value = "关联用户id")
    private String userId;
    
    @ApiModelProperty(value = "关联用户名称")
    private String userName;
    
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
