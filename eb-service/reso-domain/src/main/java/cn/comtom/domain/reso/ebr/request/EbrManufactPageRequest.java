package cn.comtom.domain.reso.ebr.request;

import java.io.Serializable;
import java.util.Date;

import cn.comtom.domain.reso.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EbrManufactPageRequest extends CriterionRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @ApiModelProperty(value = "主联系人名称")
    private String mainContactName;

    @ApiModelProperty(value = "主联系人电话")
    private Date mainContactTel;

    @ApiModelProperty(value = "技术人员名称")
    private String technologyName;

    @ApiModelProperty(value = "技术人员电话")
    private String technologyTel;

}
