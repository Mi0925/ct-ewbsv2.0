package cn.comtom.domain.system.role.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SysRoleInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
