package cn.comtom.linkage.main.access.model.ebd.commom;

/**
 * @author nobody
 * 模拟广播(发射机)附近参数
 */
public class RadioParams {
	
	/**
	 * 频道名称
	 */
	private String channelName;
	
	/**
	 * 频道频率
	 */
	private Integer freq;
	
	/**
	 * 发送功率单位瓦
	 */
	private Integer power;
	
	/**
	 * 是否备机1:是2:不是
	 */
	private Integer backup;
	
	/**
	 * 是否自动切换1:是2:不是
	 */
	private Integer autoSwitch;
	
	/**
	 * 是否远程控制开机 1:能2:不能
	 */
	private Integer remoteControl;
	
	/**
	 * 实验/覆盖发射 1:实实验2:覆盖发射
	 */
	private Integer experiment;

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
	 * @return the 发送功率单位瓦
	 */
	public Integer getPower() {
		return power;
	}

	/**
	 * @param 发送功率单位瓦 the power to set
	 */
	public void setPower(Integer power) {
		this.power = power;
	}

	/**
	 * @return the 是否备机1:是2:不是
	 */
	public Integer getBackup() {
		return backup;
	}

	/**
	 * @param 是否备机1:是2:不是 the backup to set
	 */
	public void setBackup(Integer backup) {
		this.backup = backup;
	}

	/**
	 * @return the 是否自动切换1:是2:不是
	 */
	public Integer getAutoSwitch() {
		return autoSwitch;
	}

	/**
	 * @param 是否自动切换1:是2:不是 the autoSwitch to set
	 */
	public void setAutoSwitch(Integer autoSwitch) {
		this.autoSwitch = autoSwitch;
	}

	/**
	 * @return the 是否远程控制开机1:能2:不能
	 */
	public Integer getRemoteControl() {
		return remoteControl;
	}

	/**
	 * @param 是否远程控制开机1:能2:不能 the remoteControl to set
	 */
	public void setRemoteControl(Integer remoteControl) {
		this.remoteControl = remoteControl;
	}

	/**
	 * @return the 实验覆盖发射1:实实验2:覆盖发射
	 */
	public Integer getExperiment() {
		return experiment;
	}

	/**
	 * @param 实验覆盖发射1:实实验2:覆盖发射 the experiment to set
	 */
	public void setExperiment(Integer experiment) {
		this.experiment = experiment;
	}
	
}
