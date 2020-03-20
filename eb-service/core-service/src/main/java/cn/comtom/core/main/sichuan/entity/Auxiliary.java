package cn.comtom.core.main.sichuan.entity;

public class Auxiliary {

	/*<!-- 节目类型     取值范围0～63， 标识该条辅助数据的类型，包括辅助数据的媒体类型和文件扩展名，
    字段取值  对应类型描述  对应文件扩展名
    0  保留  ——
    1  MPEG-1 LayerI/II 音频文件  MPG
    2  MPEG-1 LayerIII 音频文件  MP3
    3～20  预留（用于描述音频文件类型）  ——
    21  MPEG-2 编码音视频文件  MPG
    22  H.264 编码音视频文件(可选)  264
    23  AVS+编码音视频文件(可选)  AVS
    24～40  预留（用于描述音视频频文件类型）  ——
    41  PNG 图片文件  PNG
    42  JPEG 图片文件  JPG
    43  GIF 图片文件(可选)  GIF
    44～60  预留（用于描述图片文件类型） 
    61～255  预留  ——
	mp3\wav\JPEG\PNG\等全部多媒体附件格式
    -->*/
	private String auxiliaryType;
	
	/*<!-- 音频播发的次数整数-->*/
	private String repeatNum;
	
	/* <!-- 节目大小 0～2048K；单位为字节--> 
	<!-- 附件小于2M采用auxiliaryBody节点传输， 将文件使用base64编码 -->*/
	private String auxiliaryBody;

	/*<!-- 当前文件大小 -->*/
	private String size;
	
	/*<!-- 节目描述：URL 附件节目超过2M不采用auxiliaryBody 节点传输，采用FTP协议地址传输 -->
    <!-- ftp 地址属性节点-->*/
	private String resourceDesc;

	public String getAuxiliaryType() {
		return auxiliaryType;
	}

	public void setAuxiliaryType(String auxiliaryType) {
		this.auxiliaryType = auxiliaryType;
	}

	public String getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(String repeatNum) {
		this.repeatNum = repeatNum;
	}

	public String getAuxiliaryBody() {
		return auxiliaryBody;
	}

	public void setAuxiliaryBody(String auxiliaryBody) {
		this.auxiliaryBody = auxiliaryBody;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}
	
}
