package cn.comtom.reso.main.ebr.entity.dbo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "v_bc_ebr_info")
public class VEbr implements Serializable {

    @Id
    @Column(name = "ebrId")
    private String ebrId;

    @Column(name = "ebrName")
    private String ebrName;

    @Column(name = "ebrUrl")
    private String ebrUrl;

    @Column(name = "ebrType")
    private String ebrType;

    @Column(name = "ebrTypeName")
    private String ebrTypeName;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "square")
    private Double square;

    @Column(name = "population")
    private Double population;

    @Column(name = "areaCode")
    private String areaCode;

    @Column(name = "areaName")
    private String areaName;

    @Column(name = "ebrLevel")
    private String ebrLevel;

    @Column(name = "ebrState")
    private String ebrState;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "updateTime")
    private Date updateTime;

    @Column(name = "syncFlag")
    private String syncFlag;
    
    @Column(name = "resType")
    private String resType;
}
