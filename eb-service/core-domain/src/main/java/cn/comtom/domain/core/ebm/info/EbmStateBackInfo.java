package cn.comtom.domain.core.ebm.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author:WJ
 * @date: 2019/1/4 0004
 * @time: 下午 3:44
 */
@Data
public class EbmStateBackInfo {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @ApiModelProperty(value = "反馈时间")
    private Date backTime;

    @ApiModelProperty(value = "反馈类型")
    private String backType;

    @ApiModelProperty(value = "反馈状态")
    private String backStatus;

    @ApiModelProperty(value = "反馈状态名称")
    private String backStatusName;

    @ApiModelProperty(value = "条目ID")
    private String brdItemId;

}
