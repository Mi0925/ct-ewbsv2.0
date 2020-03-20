package cn.comtom.domain.core.ebm.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Data
public class EbmBrdRecordRequest {

	/**
	 * 播发记录条目ID
	 */
	private String brdItemId;
	
	/**
	 * 信息上报资源的ID
	 */
	@NotEmpty(message = "resourceId 不能为空")
	private String resourceId;
	
	/**
	 * 消息ID
	 */
	@NotEmpty(message = "ebmId 不能为空")
	private String ebmId;


	/**
	 * 下发最新消息ID
	 */
	@NotEmpty(message = "newEbmId 不能为空")
	private String newEbmId;


	/**
	 * 消息类型
	 */
	private Integer msgType;
	
	/**
	 * 发布机构名称
	 */
	private String sendName;
	
	/**
	 * 发布机构编码
	 */
	private String senderCode;
	
	/**
	 * 发布时间
	 */
	private Date sendTime;
	
	/**
	 * 事件类型编码
	 */
	private String eventType;
	
	/**
	 * 事件级别
	 */
	private Integer severity;
	
	/**
	 * 播发开始时间
	 */
	private Date startTime;
	
	/**
	 * 播发结束时间
	 */
	private Date endTime;
	
	/**
	 * 语种代码
	 */
	private String languageCode;
	
	/**
	 * 消息标题文本
	 */
	private String msgTitle;
	
	/**
	 * 消息内容文本
	 */
	private String msgDesc;
	
	/**
	 * 覆盖区域编码
	 */
	private String areaCode;
	
	/**
	 * 参考业务节目号
	 */
	private Integer programNum;
	
	/**
	 * 播发状态码
	 */
	private Integer brdStateCode;
	
	/**
	 * 播发状态描述
	 */
	private String brdStateDesc;
	
	/**
	 * 覆盖率
	 */
	private Double coverageRate;
	
	/**
	 * coverageAreaCode
	 */
	private String coverageAreaCode;

	private Date updateTime;

	/**
	 *记录是否已同步标识（1：未同步 2：已同步）
	 */
	private Integer syncFlag;

	/**
	 * 资源统计
	 */
	private String resBrdStat;
}