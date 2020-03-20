package cn.comtom.core.main.ebm.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "bc_ebm_brd_record")
public class EbmBrdRecord implements Serializable {

	/**
	 * 播发记录条目ID
	 */
	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "brdItemId")
	private String brdItemId;
	
	/**
	 * 信息上报资源的ID
	 */
	@Column(name = "resourceId")
	private String resourceId;
	
	/**
	 * 消息ID
	 */
	@Column(name = "ebmId")
	private String ebmId;

	@Column(name = "newEbmId")
	private String newEbmId;

	/**
	 * 消息类型
	 */
	@Column(name = "msgType")
	private Integer msgType;
	
	/**
	 * 发布机构名称
	 */
	@Column(name = "sendName")
	private String sendName;
	
	/**
	 * 发布机构编码
	 */
	@Column(name = "senderCode")
	private String senderCode;
	
	/**
	 * 发布时间
	 */
	@Column(name = "sendTime")
	private Date sendTime;
	
	/**
	 * 事件类型编码
	 */
	@Column(name = "eventType")
	private String eventType;
	
	/**
	 * 事件级别
	 */
	@Column(name = "severity")
	private Integer severity;
	
	/**
	 * 播发开始时间
	 */
	@Column(name = "startTime")
	private Date startTime;
	
	/**
	 * 播发结束时间
	 */
	@Column(name = "endTime")
	private Date endTime;
	
	/**
	 * 语种代码
	 */
	@Column(name = "languageCode")
	private String languageCode;
	
	/**
	 * 消息标题文本
	 */
	@Column(name = "msgTitle")
	private String msgTitle;
	
	/**
	 * 消息内容文本
	 */
	@Column(name = "msgDesc")
	private String msgDesc;
	
	/**
	 * 覆盖区域编码
	 */
	@Column(name = "areaCode")
	private String areaCode;
	
	/**
	 * 参考业务节目号
	 */
	@Column(name = "programNum")
	private Integer programNum;
	
	/**
	 * 播发状态码
	 */
	@Column(name = "brdStateCode")
	private Integer brdStateCode;
	
	/**
	 * 播发状态描述
	 */
	@Column(name = "brdStateDesc")
	private String brdStateDesc;
	
	/**
	 * 覆盖率
	 */
	@Column(name = "coverageRate")
	private Double coverageRate;


	/**
	 * 资源统计
	 */
	@Column(name = "resBrdStat")
	private String resBrdStat;

	/**
	 * coverageAreaCode
	 */
	@Column(name = "coverageAreaCode")
	private String coverageAreaCode;

	@Column(name = "updateTime")
	private Date updateTime;

	/**
	 *记录是否已同步标识（1：未同步 2：已同步）
	 */
	@Column(name = "syncFlag")
	private Integer syncFlag;

}