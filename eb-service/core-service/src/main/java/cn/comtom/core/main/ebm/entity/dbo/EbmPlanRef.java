package cn.comtom.core.main.ebm.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "bc_ebm_plan_ref")
public class EbmPlanRef implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @ApiModelProperty(value = "主键")
    @Column(name = "id")
    private String id;

    @Column(name = "ebmId")
    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @Column(name = "plan_name")
    @ApiModelProperty(value = "预案名称")
    private String planName;

    @Column(name = "severity")
    @ApiModelProperty(value = "事件级别")
    private String severity;

    @Column(name = "target_areas")
    @ApiModelProperty(value = "目标区域")
    private String targetAreas;

    @Column(name = "flow_type")
    @ApiModelProperty(value = "流程类型")
    private String flowType;
}
