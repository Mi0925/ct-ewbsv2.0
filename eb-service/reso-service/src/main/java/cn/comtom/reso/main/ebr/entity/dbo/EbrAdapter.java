package cn.comtom.reso.main.ebr.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebr_adapter")
public class EbrAdapter implements Serializable {

    @Id
    @Column(name = "ebrAsId")
    @ApiModelProperty(value = "主键")
    private String ebrAsId;

    @Column(name = "ebrAsName")
    private String ebrAsName;

    @Column(name = "ebrpsId")
    private String ebrpsId;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "url")
    private String url;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "ebrStId")
    private String ebrStId;
    
    @Column(name = "syncFlag")
    private String syncFlag;

    @Column(name = "statusSyncFlag")
    private String statusSyncFlag;

    @Column(name = "adapterState")
    private String adapterState;

    @Column(name = "updateTime")
    private Date updateTime;
    
    @Column(name = "asType")
    private String asType;
    
    @Column(name = "resType")
    private String resType;
    
    @Column(name = "subResType")
    private String subResType;
    
    @Column(name = "platLevel")
    private String platLevel;
    
    @Column(name = "areaCode")
    private String areaCode;
    
}
