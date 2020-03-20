package cn.comtom.domain.core.ebm.info;

import lombok.Data;

@Data
public class EbmStateRequestInfo {

	/**
	 * 消息播发状态请求编号
	 */
	private String ebmStateRequestId;

	/**
	 * 消息编号
	 */
	private String ebmId;

	/**
	 * 关联的资源编号
	 */
	private String relatedEbrId;

	/**
	 * 关联数据包编号
	 */
	private String relatedEbdId;
}