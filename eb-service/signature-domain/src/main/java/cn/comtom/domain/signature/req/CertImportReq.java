
package cn.comtom.domain.signature.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:liuhy
 * @date: 2019/5/23
 */
@Data
public class CertImportReq implements Serializable {

	@ApiModelProperty(value = "证书链")
	private byte[] certauth;
	
	@ApiModelProperty(value = "证书数据")
	private byte[] certh;
}

