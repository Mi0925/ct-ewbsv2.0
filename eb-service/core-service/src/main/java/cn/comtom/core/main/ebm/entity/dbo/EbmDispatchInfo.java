package cn.comtom.core.main.ebm.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebm_dispatch_info")
public class EbmDispatchInfo implements Serializable {

    @Id
    @Column(name="infoId")
    @ApiModelProperty(value = "主键")
    private String infoId;

    @Column(name = "ebmId")
    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @Column(name = "infoTitle")
    @ApiModelProperty(value = "消息标题")
    private String infoTitle;

    @Column(name = "auditResult")
    @ApiModelProperty(value = "审核结果")
    private String auditResult;

    @Column(name = "auditOpinion")
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @Column(name = "auditTime")
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @Column(name = "auditUser")
    @ApiModelProperty(value = "审核人")
    private String auditUser;
}
