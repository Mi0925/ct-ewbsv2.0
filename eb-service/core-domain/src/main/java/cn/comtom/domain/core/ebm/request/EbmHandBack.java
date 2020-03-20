package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author:WJ
 * @date: 2019/1/4 0004
 * @time: 下午 2:31
 */
@Data
public class EbmHandBack {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "ebmId 不能为空")
    private String ebmId;
}
