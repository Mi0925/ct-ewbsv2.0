package cn.comtom.reso.main.ebr.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebr_terminal")
public class EbrTerminal implements Serializable {

    @Id
    @Column(name = "terminalEbrId")
    @ApiModelProperty(value = "主键")
    private String terminalEbrId;

    @Column(name = "terminalEbrName")
    private String terminalEbrName;

    @Column(name = "relatedPsEbrId")
    private String relatedPsEbrId;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "terminalState")
    private String terminalState;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "updateTime")
    private Date updateTime;

    @Column(name = "syncFlag")
    private String syncFlag;

    @Column(name = "statusSyncFlag")
    private String statusSyncFlag;
}
