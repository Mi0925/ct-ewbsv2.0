package cn.comtom.domain.signature.req;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:liuhy
 * @date: 2019/5/23
 */
@Data
public class CertApplyReq implements Serializable {

	@ApiModelProperty(value = "源信任证书列表",allowableValues = "[]",required = true, dataType = "String", example = "[\"00000000000e\",\"00000000000f\"]")
	private List<String> sourceCertNos;

	@ApiModelProperty(value = "目标设备证书号",required = true, dataType = "String", example = "0000000012fc")
	@NotEmpty(message = "目标设备证书号不能为空")
	private String destCertNo;

}
