package cn.comtom.domain.core.ebm.info;


import lombok.Data;

@Data
public class EbmResInfo {

	/**
	 * 消息资源ID
	 */
	private String ebmResourceId;
	
	/**
	 * 关联记录ID
	 */
	private String brdItemId;

	/**
	 * 资源ID
	 */
	private String resId;

	/**
	 * 资源类型
	 */
	private String resType;
}