package cn.comtom.domain.reso.ebr.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author:WJ
 * @date: 2018/12/5 0005
 * @time: 上午 9:31
 */
@Data
public class TerminalConditionRequest implements Serializable {

    @ApiModelProperty(value = "区域码")
    public List<String> areCodes;

    @ApiModelProperty(value = "状态")
    private String terminalState;

}
