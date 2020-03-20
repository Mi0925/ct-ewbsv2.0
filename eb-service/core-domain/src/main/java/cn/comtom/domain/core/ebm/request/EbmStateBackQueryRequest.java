package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author:WJ
 * @date: 2019/1/15 0015
 * @time: 下午 7:26
 */
@Data
public class EbmStateBackQueryRequest {

    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @ApiModelProperty(value = "条目ID")
    private String brdItemId;
}
