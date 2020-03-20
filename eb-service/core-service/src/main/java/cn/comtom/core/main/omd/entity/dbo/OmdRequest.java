package cn.comtom.core.main.omd.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "bc_omd_request")
public class OmdRequest implements Serializable {

	/**
	 * 运维数据请求编号
	 */
	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "omdRequestId")
	private String omdRequestId;

	/**
	 * 运维数据类型
	 */
	@Column(name = "omdType")
	private String omdType;

	/**
	 * 记录开始时间
	 */
	@Column(name = "rptStartTime")
	private Date rptStartTime;

	/**
	 * 记录结束时间
	 */
	@Column(name = "rptEndTime")
	private Date rptEndTime;

	/**
	 * 数据包操作类型
	 */
	@Column(name = "rptType")
	private String rptType;

	/**
	 * 关联业务数据包编号（发送）
	 */
	@Column(name = "relatedEbdId")
	private String relatedEbdId;

	/**
	 * 关联运维数据请求资源编号（发送）
	 */
	@Column(name = "relatedEbrId")
	private String relatedEbrId;



}