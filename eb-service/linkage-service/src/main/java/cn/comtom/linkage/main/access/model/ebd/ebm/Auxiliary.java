package cn.comtom.linkage.main.access.model.ebd.ebm;

/**
 * @author nobody
 * 辅助数据
 */
public class Auxiliary {
	
	
	/**
	 * 辅助数据类型(参考辅助数据类型说明)
	 */
	private Integer auxiliaryType;
	
	/**
	 * 辅助数据描述，标识辅助数据文件名（包含提取该文件的位置）;
	 * 示例1:应急音频.mp3
	 * 示例2:(暂不使用):http://127.0.0.1/20140101090000_11B01_3_1.jpg
	 */
	private String auxiliaryDesc;
	
	/**
	 * 文件大小(可选)
	 */
	private Integer size;
	
	/**
	 * 辅助数据文件摘要(可选)
	 * 采用SHA-1算法。判断文件是否被篡改。默认为空
	 */
	private String digest;

	/**
	 * @return the 辅助数据类型(参考辅助数据类型说明)
	 */
	public Integer getAuxiliaryType() {
		return auxiliaryType;
	}

	/**
	 * @param 辅助数据类型(参考辅助数据类型说明) the auxiliary to set
	 */
	public void setAuxiliaryType(Integer auxiliaryType) {
		this.auxiliaryType = auxiliaryType;
	}

	/**
	 * @return the 辅助数据描述，标识辅助数据文件名（包含提取该文件的位置）;示例1:应急音频.mp3示例2:(暂不使用):http:127.0.0.120140101090000_11B01_3_1.jpg
	 */
	public String getAuxiliaryDesc() {
		return auxiliaryDesc;
	}

	/**
	 * @param 辅助数据描述，标识辅助数据文件名（包含提取该文件的位置）;示例1:应急音频.mp3示例2:(暂不使用):http:127.0.0.120140101090000_11B01_3_1.jpg the auxiliaryDesc to set
	 */
	public void setAuxiliaryDesc(String auxiliaryDesc) {
		this.auxiliaryDesc = auxiliaryDesc;
	}

	/**
	 * @return the 文件大小(可选)
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param 文件大小(可选) the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @return the 辅助数据文件摘要(可选)采用SHA-1算法。判断文件是否被篡改。默认为空
	 */
	public String getDigest() {
		return digest;
	}

	/**
	 * @param 辅助数据文件摘要(可选)采用SHA-1算法。判断文件是否被篡改。默认为空 the digest to set
	 */
	public void setDigest(String digest) {
		this.digest = digest;
	}
	
}
