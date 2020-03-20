package cn.comtom.linkage.main.access.model.ebd.ebm;

import java.util.List;

import cn.comtom.linkage.utils.RegionUtil;


/**
 * @author nobody
 * 应急广播消息内容
 */
public class MsgContent {
	
	/**
	 * 语种代码
	 */
	private String languageCode;
	
	/**
	 * 消息标题
	 */
	private String msgTitle;
	
	/**
	 * 消息内容
	 */
	private String msgDesc;
	
	/**
	 * 覆盖区域编码集合，多个区域用英文逗号,分开
	 */
	private String areaCode;
	
	/**
	 * 参考业务节目号(可选)
	 */
	private Integer programNum;

	/**
	 * 辅助数据,可为0个或多个
	 */
	private List<Auxiliary> auxiliaryList;
	
	/**
	 * @return the 语种代码
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * @param 语种代码 the languageCode to set
	 */
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	/**
	 * @return the 消息标题
	 */
	public String getMsgTitle() {
		return msgTitle;
	}

	/**
	 * @param 消息标题 the msgTitle to set
	 */
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	/**
	 * @return the 消息内容
	 */
	public String getMsgDesc() {
		return msgDesc;
	}

	/**
	 * @param 消息内容 the msgDesc to set
	 */
	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	/**
	 * @return the 覆盖区域编码集合
	 */
	public String getAreaCode() {
		return RegionUtil.areaLong2Short(areaCode);
	}

	/**
	 * @param 覆盖区域编码集合 the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @return the 参考业务节目号(可选)
	 */
	public Integer getProgramNum() {
		return programNum;
	}

	/**
	 * @param 参考业务节目号(可选) the programNum to set
	 */
	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}

	/**
	 * @return the 辅助数据可为0个或多个
	 */
	public List<Auxiliary> getAuxiliaryList() {
		return auxiliaryList;
	}

	/**
	 * @param 辅助数据可为0个或多个 the auxiliaryList to set
	 */
	public void setAuxiliaryList(List<Auxiliary> auxiliaryList) {
		this.auxiliaryList = auxiliaryList;
	}
	
}
