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
@Table(name = "bc_ebm_auxiliary")
public class EbmAuxiliary implements Serializable {

    @Id
    @Column(name="auxiliaryId")
    @ApiModelProperty(value = "主键")
    @KeySql(genId = UUIdGenId.class)
    private String auxiliaryId;

    @Column(name = "auxiliaryType")
    @ApiModelProperty(value = "辅助数据类型")
    private String auxiliaryType;

    @Column(name = "auxiliaryDesc")
    @ApiModelProperty(value = "辅助数据描述")
    private String auxiliaryDesc;

    @Column(name = "auxiliarySize")
    @ApiModelProperty(value = "辅助数据文件大小")
    private Integer auxiliarySize;

    @Column(name = "auxiliaryDigest")
    @ApiModelProperty(value = "辅助数据文件摘要")
    private String auxiliaryDigest;

    @Column(name = "ebmId")
    @ApiModelProperty(value = "消息ID")
    private String ebmId;

}
