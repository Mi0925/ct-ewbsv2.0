package cn.comtom.domain.reso.ebr.request;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class EbrBroadcastUpdateRequest implements Serializable {

	@NotEmpty(message = "bsEbrId不能为空")
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

	private String languageCode;

	private String equipRoom;

	private String radioChannelName;

	private Integer radioFreq;

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

	private String relatedRsEbrId;

	private String relatedAsEbrId;

	private Date updateTime;

	private String syncFlag;

	private String statusSyncFlag;

	private String belong;

	private String remark;

	private String orgName;

	private String orgType;

	private String srcHost;
	
	@ApiModelProperty(value = "资源级别")
	private String platLevel;
	
	@ApiModelProperty(value = "资源下发渠道")
    private String resChannel;
}