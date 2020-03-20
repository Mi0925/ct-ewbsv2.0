package cn.comtom.core.main.ebm.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebm_dispatch")
public class EbmDispatch implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="dispatchId")
    @ApiModelProperty(value = "主键")
    private String dispatchId;

    @Column(name = "languageCode")
    @ApiModelProperty(value = "语种代码")
    private String languageCode;

    @Column(name = "psEbrId")
    @ApiModelProperty(value = "应急广播平台ID")
    private String psEbrId;

    @Column(name = "bsEbrId")
    @ApiModelProperty(value = "播出系统ID")
    private String bsEbrId;

    @Column(name = "asEbrId")
    @ApiModelProperty(value = "适配器资源ID")
    private String asEbrId;
    
    @Column(name = "stEbrId")
    @ApiModelProperty(value = "台站资源ID")
    private String stEbrId;
    
    @Column(name = "brdSysType")
    @ApiModelProperty(value = "播出系统类型")
    private String brdSysType;

    @Column(name = "brdSysInfo")
    @ApiModelProperty(value = "播出系统信息")
    private String brdSysInfo;

    @Column(name = "ebdId")
    @ApiModelProperty(value = "数据包Id")
    private String ebdId;

    @Column(name = "matchEbdId")
    @ApiModelProperty(value = "消息匹配后生成的EBD数据包Id")
    private String matchEbdId;

    @Column(name = "ebmId")
    @ApiModelProperty(value = "关联EBM")
    private String ebmId;

    @Column(name = "state")
    @ApiModelProperty(value = "状态 调度分发状态（0:待调度 1:已调度 2:调度失败）")
    private String state;

    @Column(name = "dispatchTime")
    @ApiModelProperty(value = "调度分发时间")
    private Date dispatchTime;

    @Column(name = "responseTime")
    @ApiModelProperty(value = "响应时间")
    private Date responseTime;

    @Column(name = "failCount")
    @ApiModelProperty(value = "失败次数")
    private Integer failCount;
    
    @Column(name = "playTime")
    @ApiModelProperty(value = "播放时间")
    private Date playTime;

}
