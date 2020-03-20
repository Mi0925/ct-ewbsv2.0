package cn.comtom.core.main.access.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Table(name="bc_access_area")
public class AccessArea implements Serializable {
    @Id
    @KeySql(genId = UUIdGenId.class)
    @ApiModelProperty(value = "主键")
    @Column(name = "id")
    private String id;

    @ApiModelProperty(value = "信息编号")
    @NotEmpty(message = "infoId can not be empty")
    @Column(name = "infoId")
    private String infoId;

    @ApiModelProperty(value = "区域编码")
    @NotEmpty(message = "areaCode can not be empty")
    @Column(name = "areaCode")
    private String areaCode;
}
