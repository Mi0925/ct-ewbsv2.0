package cn.comtom.domain.system.sysresource.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysResourceAddRequest implements Serializable {


    @ApiModelProperty(value = "父ID")
    private String pid;

    @ApiModelProperty(value = "资源名称")
    private String name;

    @ApiModelProperty(value = "资源类型")
    private String type;

    @ApiModelProperty(value = "资源图标")
    private String icon;

    @ApiModelProperty(value = "资源权限")
    private String perms;

    @ApiModelProperty(value = "资源URL")
    private String url;

    @ApiModelProperty(value = "排序号")
    private Integer orderNum;
}
