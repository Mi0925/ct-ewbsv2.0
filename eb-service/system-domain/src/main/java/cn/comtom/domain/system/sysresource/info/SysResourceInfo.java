package cn.comtom.domain.system.sysresource.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysResourceInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

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

    @ApiModelProperty(value = "排序号")
    private List<SysResourceInfo> subList;

}
