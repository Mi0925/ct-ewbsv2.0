package cn.comtom.core.main.scheme.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "bc_scheme")
public class Scheme implements Serializable {

	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "schemeId")
	private String schemeId;

	@Column(name = "schemeTitle")
	private String schemeTitle;

	@Column(name = "flowId")
	private String flowId;

	@Column(name = "totalArea")
	private Double totalArea;

	@Column(name = "totalPopu")
	private Double totalPopu;

	@Column(name = "areaPercent")
	private Double areaPercent;

	@Column(name = "popuPercent")
	private Double popuPercent;

	@Column(name = "auditResult")
	private String auditResult;

	@Column(name = "auditOpinion")
	private String auditOpinion;

	@Column(name = "auditTime")
	private Date auditTime;

	@Column(name = "auditUser")
	private String auditUser;

	@Column(name = "state")
	private String state;

	@Column(name = "ebmId")
	private String ebmId;

	@Column(name = "programId")
	private String programId;

	@Column(name = "ebdId")
	private String ebdId;

	@Column(name = "infoId")
	private String infoId;

	@Column(name = "planId")
	private String planId;

	@Column(name = "schemeType")
	private String schemeType;

	@Column(name = "ebrPopu")
	private Double ebrPopu;

	@Column(name = "ebrArea")
	private Double ebrArea;

	@Column(name = "createTime")
	private Date createTime;



}