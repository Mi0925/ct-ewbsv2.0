package cn.comtom.domain.system.org;

import cn.comtom.domain.system.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrgPageRequest extends CriterionRequest {
	
	@ApiModelProperty(value = "联系人名称")
	private String contactName;
	
    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @ApiModelProperty(value = "所属区域名称")
    private String areaName;
    
    @ApiModelProperty(value = "关联用户名称")
    private String userName;
}
