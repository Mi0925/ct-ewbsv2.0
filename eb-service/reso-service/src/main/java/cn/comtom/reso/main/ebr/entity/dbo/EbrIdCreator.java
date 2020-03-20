package cn.comtom.reso.main.ebr.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Table(name = "bc_ebr_id_creator")
public class EbrIdCreator implements Serializable {

    @Id
    @Column(name = "id")
    @NotEmpty(message = "id")
    @ApiModelProperty(value = "主键")
    private String id;

    @Column(name = "sourceLevel")
    @NotEmpty(message = "sourceLevel不能为空")
    @ApiModelProperty(value = "资源级别")
    private String sourceLevel;

    @Column(name = "areaCode")
    @NotEmpty(message = "areaCode不能为空")
    @ApiModelProperty(value = "区域编码")
    private String areaCode;

    @Column(name = "sourceTypeCode")
    @NotEmpty(message = "sourceTypeCode不能为空")
    @ApiModelProperty(value = "资源类型码")
    private String sourceTypeCode;

    @Column(name = "sourceTypeSN")
    @NotEmpty(message = "sourceTypeSN不能为空")
    @ApiModelProperty(value = "资源类型序列")
    private String sourceTypeSN;

    @Column(name = "sourceSubTypeCode")
    @NotEmpty(message = "sourceSubTypeCode不能为空")
    @ApiModelProperty(value = "资源子类型码")
    private String sourceSubTypeCode;

    @Column(name = "sourceSubTypeSN")
    @NotEmpty(message = "sourceSubTypeSN不能为空")
    @ApiModelProperty(value = "资源子类型序列")
    private String sourceSubTypeSN;

    @Column(name = "ebrId")
    @NotEmpty(message = "ebrId不能为空")
    @ApiModelProperty(value = "最终生成的EBRID")
    private String ebrId;
}
