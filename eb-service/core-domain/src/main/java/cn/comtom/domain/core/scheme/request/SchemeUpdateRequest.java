package cn.comtom.domain.core.scheme.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class SchemeUpdateRequest implements Serializable {

	@NotEmpty(message = "schemeId 不能为空！")
	private String schemeId;

	private String schemeTitle;

	private Double totalArea;

	private Double totalPopu;

	private Double areaPercent;

	private Double popuPercent;

	private String auditResult;

	private String auditOpinion;

	private Date auditTime;

	private String auditUser;

	private String state;

	private Double ebrPopu;

	private Double ebrArea;

	private String planId;

	private List<SchemeEbrInfo> refEbrList;
}