package cn.comtom.domain.core.ebm.request;

import lombok.Data;

@Data
public class EbmStateRequest {

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