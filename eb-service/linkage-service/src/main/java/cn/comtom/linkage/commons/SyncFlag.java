package cn.comtom.linkage.commons;

/**
 * @author nobody
 * 同步标志
 */
public enum SyncFlag {
	
	sync(2,"已同步"),nosync(1,"未同步");
	
	private Integer value;
	
	private String name;
	
	private SyncFlag(Integer value, String name){
		this.name=name;
		this.value=value;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
