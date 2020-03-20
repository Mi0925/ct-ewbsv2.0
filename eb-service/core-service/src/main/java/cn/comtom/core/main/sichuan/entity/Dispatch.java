package cn.comtom.core.main.sichuan.entity;

public class Dispatch {
	
	/*<!--可选 消息接收设备编号 12位字符串,指示需要对本次应急广播消息进行处理的接收设备编号；
    1、对于直接播发本次应急广播消息的播出前端/发射台站（如调频、中短波、有线电视、地面电视等），该字段指示需要调用的播出前端/发射台站所装配的消息接收设备的编号。
          消息接收设备编号为12位数字码，前8位编码方式为‘所属行政区域码（6位） +顺序号（2位），前6位为符合GB/T 2260-2007的，装配消息接收设备的播出前端/发射台站所在行政区域数字码；
    2、对于需要经由全国性的中央平台分发本次应急广播消息的播出前端（如直播卫星等）该字段的前6位为符合GB/T 2260-2007的行政区域数字码，指示分发平台需要对符合该行政区域数字码的接收终端发布本次应急广播消息，后6位取值为‘000000’；
         消息接收设备需要按照区域码通配方式响应本次应急广播消息。-->*/
	private String receiverID;
	
	 /*<!--必选 资源类型 16位二进制码标识，
     0b111111111111111  所有类型
     0b100000000000000  调频广播
     0b010000000000000  中波广播
     0b001000000000000  短波广播
     0b000100000000000  有线数字电视
     0b000010000000000  有线模拟电视 
     0b000001000000000  地面数字电视
     0b000000100000000  地面模拟电视
     0b000000010000000  直播卫星
     0b000000001000000  卫星电视（DVB-S）
     0b000000000100000  移动多媒体广播电视
     0b000000000010000  数字音频广播
     0b000000000001000  应急广播村村响
            第 13～15 位  预留-->*/
	private String sysType;
	
	/*呈现方式--，如：
	0101调频广播日常播出,0102调频广播节目插播
	0201中波广播日常播出,0202中波广播节目插播
	0301短波广播日常播出,0302短波广播节目插播
	0401有线数字电视上方字幕,0402有线数字电视中间字幕,0403有线数字电视下方字幕,0404有线数字电视MAIL
	0501有线模拟电视上方字幕,0502有线模拟电视中间字幕,0503有线模拟电视下方字幕,0504有线模拟电视MAIL
	0601地面数字电视上方字幕,0602地面数字电视中间字幕,0603地面数字电视下方字幕,0604地面数字电视MAIL
	0701地面模拟电视上方字幕,0702地面模拟电视中间字幕,0703地面模拟电视下方字幕,0704地面模拟电视MAIL
	0801直播卫星上方字幕,0802直播卫星中间字幕,0803直播卫星下方字幕,0804直播卫星MAIL
	0901卫星电视上方字幕,0902卫星电视中间字幕,0903卫星电视下方字幕,0904卫星电视MAIL
	1001移动多媒体广播电视上方字幕,1002移动多媒体广播电视中间字幕,1003移动多媒体广播电视方字幕,1004移动多媒体广播电视MAIL
	1101数字音频广播日常播出,1102数字音频广播节目插播
	1201应急广播村村响日常播出,1202应急广播村村响日常播出*/
	private String presentation;
	
	//字幕每次重复次数--，如：2次
	private String rollFrequency;
	
	//指定播出系统编号 12位数字码；可多个，0~256个；非必填
	private SysID sysID;
	
	//确认信息返回方式，0~3；0无确认信息返回，1短信方式，2网络方式，3短信和网络方式   非必填；可多个，0~4个
	private String returnWay;
	
	//确认信息接受短信号码  11位数字码；非必填
	private String returnTel;
	
	//确认信息接收IP  非必填
	private String returnIP;
	
	//确认信息接收端口号  非必填
	private String returnPort;

	public String getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public String getPresentation() {
		return presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

	public String getRollFrequency() {
		return rollFrequency;
	}

	public void setRollFrequency(String rollFrequency) {
		this.rollFrequency = rollFrequency;
	}

	public SysID getSysID() {
		return sysID;
	}

	public void setSysID(SysID sysID) {
		this.sysID = sysID;
	}

	public String getReturnWay() {
		return returnWay;
	}

	public void setReturnWay(String returnWay) {
		this.returnWay = returnWay;
	}

	public String getReturnTel() {
		return returnTel;
	}

	public void setReturnTel(String returnTel) {
		this.returnTel = returnTel;
	}

	public String getReturnIP() {
		return returnIP;
	}

	public void setReturnIP(String returnIP) {
		this.returnIP = returnIP;
	}

	public String getReturnPort() {
		return returnPort;
	}

	public void setReturnPort(String returnPort) {
		this.returnPort = returnPort;
	}
	
}
