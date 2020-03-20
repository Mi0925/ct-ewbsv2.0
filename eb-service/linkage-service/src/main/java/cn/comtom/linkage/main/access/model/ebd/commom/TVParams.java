package cn.comtom.linkage.main.access.model.ebd.commom;

/**
 * @author nobody
 * 数字电视附加参数
 */
public class TVParams {
	
	/**
	 * 频道名称
	 */
	private String channelName;
	
	/**
	 * 频道频率
	 */
	private Integer freq;

	/**
	 * 节目号
	 */
	private String programNum;
	
	/**
	 * 频道号
	 */
	private String channelNum;

	/**
	 * @return the 频道名称
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @param 频道名称 the channelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * @return the 频道频率
	 */
	public Integer getFreq() {
		return freq;
	}

	/**
	 * @param 频道频率 the freq to set
	 */
	public void setFreq(Integer freq) {
		this.freq = freq;
	}

	/**
	 * @return the 节目号
	 */
	public String getProgramNum() {
		return programNum;
	}

	/**
	 * @param 节目号 the programNum to set
	 */
	public void setProgramNum(String programNum) {
		this.programNum = programNum;
	}

	/**
	 * @return the 频道号
	 */
	public String getChannelNum() {
		return channelNum;
	}

	/**
	 * @param 频道号 the channelNum to set
	 */
	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}
	
}
