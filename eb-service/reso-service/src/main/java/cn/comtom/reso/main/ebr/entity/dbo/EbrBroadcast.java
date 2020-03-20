package cn.comtom.reso.main.ebr.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "bc_ebr_broadcast")
public class EbrBroadcast implements Serializable {

	@Id
	@Column(name = "bsEbrId")
	private String bsEbrId;

	@Column(name = "bsName")
	private String bsName;

	@Column(name = "bsUrl")
	private String bsUrl;

	@Column(name = "bsType")
	private String bsType;

	@Column(name = "resType")
	private String resType;

	@Column(name = "subResType")
	private String subResType;

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

	@Column(name = "languageCode")
	private String languageCode;

	@Column(name = "equipRoom")
	private String equipRoom;

	@Column(name = "radioChannelName")
	private String radioChannelName;

	@Column(name = "radioFreq")
	private Double radioFreq;

	@Column(name = "radioPower")
	private Integer radioPower;

	@Column(name = "backup")
	private String backup;

	@Column(name = "autoSwitch")
	private String autoSwitch;

	@Column(name = "remoteControl")
	private String remoteControl;

	@Column(name = "experiment")
	private String experiment;

	@Column(name = "tvChannelName")
	private String tvChannelName;

	@Column(name = "tvFreq")
	private Integer tvFreq;

	@Column(name = "programNum")
	private String programNum;

	@Column(name = "tvChannelNum")
	private String tvChannelNum;

	@Column(name = "bsState")
	private Integer bsState;

	@Column(name = "relatedPsEbrId")
	private String relatedPsEbrId;

	@Column(name = "relatedRsEbrId")
	private String relatedRsEbrId;

	@Column(name = "relatedAsEbrId")
	private String relatedAsEbrId;

	@Column(name = "createTime")
	private Date createTime;

	@Column(name = "updateTime")
	private Date updateTime;

	@Column(name = "syncFlag")
	private String syncFlag;

	@Column(name = "statusSyncFlag")
	private String statusSyncFlag;

	@Column(name = "belong")
	private String belong;

	@Column(name = "orgName")
	@ApiModelProperty(value = "单位名称")
	private String orgName;

	@Column(name = "orgType")
	@ApiModelProperty(value = "单位类型")
	private String orgType;

	@Column(name = "remark")
	@ApiModelProperty(value = "单位类型")
	private String remark;

	@Column(name = "platLevel")
	@ApiModelProperty(value = "资源级别")
	private String platLevel;
}