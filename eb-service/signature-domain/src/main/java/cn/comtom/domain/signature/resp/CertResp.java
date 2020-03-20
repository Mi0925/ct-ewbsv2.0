
package cn.comtom.domain.signature.resp;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:liuhy
 * @date: 2019/5/23
 */
@Data
public class CertResp implements Serializable {

	@ApiModelProperty(value = "证书链")
	private String certauth;
	
	@ApiModelProperty(value = "证书数据")
	private List<String> certh;
}

