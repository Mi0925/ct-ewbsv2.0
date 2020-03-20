package cn.comtom.core.main.statistics.domain.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangbingyan
 * @date 2020/1/9
 * @desc 广播目标资源信息
 */
@Data
public class EbrDto implements Serializable {

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

    private Integer dispatchState;

}
