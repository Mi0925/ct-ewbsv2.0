package cn.comtom.domain.system.sequence.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SysSequenceInfo {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "序列号名称")
    @NotEmpty(message = "name must not be empty")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "前缀")
    private String prefix;

    @ApiModelProperty(value = "起始值")
    private String start;

    @ApiModelProperty(value = "递增步长")
    private String step;

    @ApiModelProperty(value = "当前值")
    private String curValue;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "连接符")
    private String connector;

    @ApiModelProperty(value = "后缀")
    private String suffix;

    @ApiModelProperty(value = "DBSequence名称")
    private String dbSeqName;

    @ApiModelProperty(value = "最大值")
    private String maxValue;

    @ApiModelProperty(value = "是否循环")
    private String isCircul;

    @ApiModelProperty(value = "最小值")
    private String minValue;

    @ApiModelProperty(value = "是否左补足")
    private String isLeftpad;

    @ApiModelProperty(value = "当前格式化值")
    private String formatValue;

    @ApiModelProperty(value = "备注")
    private String remark;









}
