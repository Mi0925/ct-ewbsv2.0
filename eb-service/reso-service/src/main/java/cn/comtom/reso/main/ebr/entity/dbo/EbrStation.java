package cn.comtom.reso.main.ebr.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebr_station")
public class EbrStation implements Serializable {
	
    @Id
    @Column(name = "ebrStId")
    @ApiModelProperty(value = "主键")
    private String ebrStId;

    @Column(name = "ebrStName")
    private String ebrStName;

    @Column(name = "ebrPsId")
    private String ebrPsId;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;
    
    @Column(name = "syncFlag")
    private String syncFlag;

    @Column(name = "statusSyncFlag")
    private String statusSyncFlag;

    @Column(name = "stationState")
    private String stationState;

    @Column(name = "updateTime")
    private Date updateTime;
    
    @Column(name = "stType")
    private String stType;
    
    @Column(name = "resType")
    private String resType;
    
    @Column(name = "subResType")
    private String subResType;

    @Column(name = "platLevel")
    private String platLevel;
    
    @Column(name = "areaCode")
    private String areaCode;
}
