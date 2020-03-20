package cn.comtom.reso.main.ebr.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.comtom.reso.fw.UUIdGenId;

import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebr_manufacturer")
public class EbrManufacturer implements Serializable {

    @Id
    @Column(name = "id")
    @KeySql(genId = UUIdGenId.class)
    @ApiModelProperty(value = "主键")
    private String id;

    @Column(name = "companyName")
    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @Column(name = "mainContactName")
    @ApiModelProperty(value = "主联系人名称")
    private String mainContactName;

    @Column(name = "mainContactTel")
    @ApiModelProperty(value = "主联系人电话")
    private String mainContactTel;

    @Column(name = "technologyName")
    @ApiModelProperty(value = "技术人员名称")
    private String technologyName;

    @Column(name = "technologyTel")
    @ApiModelProperty(value = "技术人员电话")
    private String technologyTel;
    
    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    
    @Column(name = "updateTime")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
