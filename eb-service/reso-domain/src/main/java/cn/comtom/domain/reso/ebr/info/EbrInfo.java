package cn.comtom.domain.reso.ebr.info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EbrInfo implements Serializable {

    private String ebrId;

    private String ebrName;

    private String ebrUrl;

    private String ebrType;

    private String resType;

    private String ebrTypeName;

    private String longitude;

    private String latitude;

    private Double square;

    private Double population;

    private String areaCode;

    private String areaName;

    private String ebrLevel;

    private String languageCode;

    private String ebrState;

    private Date createTime;

    private Date updateTime;

    private String syncFlag;

    private String srcHost;

    private String contact;

    private String  phoneNumber;

    private String orgName;

    private String orgType;

    private String remark;
}
