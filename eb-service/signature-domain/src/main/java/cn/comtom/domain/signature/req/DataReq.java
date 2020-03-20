package cn.comtom.domain.signature.req;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:liuhy
 * @date: 2019/5/23
 */
@Data
public class DataReq implements Serializable {

	@ApiModelProperty(value = "签名数据-字节数组",allowableValues = "[]",required = true, dataType = "byte", example = "[1,2,3]")
	@NotEmpty(message = "数据不能为空")
	private byte[] data;
	
	@ApiModelProperty(value = "签名值",example = "00208D14000011010001054474C6C613A0566C9B75AE05CAE2F833A094342A3AAD04BB4F07827993575617E1B1B6185CAB1632FB3B824C2507948C3EC42FE6A621BE059CE67589B23486")
	private String signValue;
}
