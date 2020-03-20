package cn.comtom.domain.reso.ebr.info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;


@Data
public class EbrBroadcastInfo implements Serializable {

	private String bsEbrId;

	private String bsName;

	private String bsUrl;

	private String bsType;

	private String resType;

	private String subResType;

	private String longitude;

	private String latitude;

	private Double square;

	private Double population;

	private String areaCode;

	private String areaName;

	private String languageCode;

	private String equipRoom;

	private String radioChannelName;

	private Double radioFreq;

	private Integer radioPower;

	private String backup;

	private String autoSwitch;

	private String remoteControl;

	private String experiment;

	private String tvChannelName;

	private Integer tvFreq;

	private String programNum;

	private String tvChannelNum;

	private Integer bsState;

	private String relatedPsEbrId;

	private String relatePsName;

	private String relatedRsEbrId;

	private String relatedRsName;

	private String relatedAsEbrId;

	private String relatedAsName;

	private Date createTime;

	private Date updateTime;

	private String syncFlag;

	private String statusSyncFlag;

	private String belong;

	private String orgName;

	private String orgType;

	private String remark;

	private String srcHost;
	
	private String platLevel;

	@ApiModelProperty(value = "资源下发渠道")
    private String resChannel;
}