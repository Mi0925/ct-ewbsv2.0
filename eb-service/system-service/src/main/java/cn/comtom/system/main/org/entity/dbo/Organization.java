package cn.comtom.system.main.org.entity.dbo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.comtom.system.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Table(name = "sys_organization")
public class Organization implements Serializable {

	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "id")
	@ApiModelProperty(value = "主键")
	private String id;
    
    @Column(name = "companyName")
    @ApiModelProperty(value = "单位名称")
    private String companyName;
    
    @Column(name = "contactName")
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;
    
    @Column(name = "contactTel")
    @ApiModelProperty(value = "联系人电话")
    private String contactTel;
    
    @Column(name = "areaId")
    @ApiModelProperty(value = "所属区域编码id")
    private String areaId;
    
    @Column(name = "userId")
    @ApiModelProperty(value = "关联用户id")
    private String userId;
    
    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    
    @Column(name = "updateTime")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
