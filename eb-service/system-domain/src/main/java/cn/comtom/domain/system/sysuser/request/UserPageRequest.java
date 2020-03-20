package cn.comtom.domain.system.sysuser.request;

import cn.comtom.domain.system.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserPageRequest extends CriterionRequest {

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "状态")
    private String deleteFlag;
}
