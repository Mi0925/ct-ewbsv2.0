package cn.comtom.core.main.ebm.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author:WJ
 * @date: 2019/1/4 0004
 * @time: 下午 3:46
 */
@Data
@Table(name = "bc_ebm_state_back")
public class EbmStateBack implements Serializable {

    @Id
    @Column(name="id")
    @ApiModelProperty(value = "主键")
    private String id;

    @Column(name = "ebmId")
    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @Column(name = "backTime")
    @ApiModelProperty(value = "反馈时间")
    private Date backTime;

    @Column(name = "backType")
    @ApiModelProperty(value = "反馈类型")
    private String backType;

    @Column(name = "backStatus")
    @ApiModelProperty(value = "反馈状态")
    private String backStatus;

    @Column(name = "brdItemId")
    @ApiModelProperty(value = "条目ID")
    private String brdItemId;

}
