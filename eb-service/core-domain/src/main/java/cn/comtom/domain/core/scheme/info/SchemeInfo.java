package cn.comtom.domain.core.scheme.info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SchemeInfo implements Serializable {

	private String schemeId;

	private String schemeTitle;

	private String flowId;

	private Double totalArea;

	private Double totalPopu;

	private Double areaPercent;

	private Double popuPercent;

	private String auditResult;

	private String auditOpinion;

	private Date auditTime;

	private String auditUser;

	private String state;

	private String ebmId;

	private String programId;

	private String ebdId;

	private String infoId;

	private String schemeType;

	private Double ebrPopu;

	private Double ebrArea;

	private Date createTime;

	private String planId;



}