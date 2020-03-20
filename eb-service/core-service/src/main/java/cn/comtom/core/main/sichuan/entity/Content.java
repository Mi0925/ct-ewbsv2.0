package cn.comtom.core.main.sichuan.entity;

public class Content {
	
	/* <!-- 语种应急广播文本内容的语种代码，该代码应符合GB/T 4880.2-2000的3字母语种代码要求示例：汉语的3字符代码“zho”-->*/
	private String languageCod;
	
	/*<!-- 编码字符集 0~7；0：18030-2005  1：GB GB 2312-1980（可选）2：GB 13000-2010（可选）3：GB 21669-2008（可选）4：GB 16959-1997（可选）5~7预留-->*/
	private String codeSet;
	
	/*<!-- 预警内容描述，字数不超过500个字 -->*/
	private String description;
	
	/*<!-- 参考业务节目号  0~65535；非必填-->*/
	private String programNum;
	
	/*<!-- 预警节目信息，必选 -->*/
	private Auxiliary auxiliary;

	public String getLanguageCod() {
		return languageCod;
	}

	public void setLanguageCod(String languageCod) {
		this.languageCod = languageCod;
	}

	public String getCodeSet() {
		return codeSet;
	}

	public void setCodeSet(String codeSet) {
		this.codeSet = codeSet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProgramNum() {
		return programNum;
	}

	public void setProgramNum(String programNum) {
		this.programNum = programNum;
	}

	public Auxiliary getAuxiliary() {
		return auxiliary;
	}

	public void setAuxiliary(Auxiliary auxiliary) {
		this.auxiliary = auxiliary;
	}
}
