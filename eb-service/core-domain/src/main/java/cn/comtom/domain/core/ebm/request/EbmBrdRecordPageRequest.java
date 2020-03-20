package cn.comtom.domain.core.ebm.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Data
public class EbmBrdRecordPageRequest extends CriterionRequest implements Serializable {

	/**
	 * 发布机构编码
	 */
	private String senderCode;

	/**
	 * 播发状态码
	 */
	private Integer brdStateCode;

	/**
	 * ebmId
	 */
	private String ebmId;

}