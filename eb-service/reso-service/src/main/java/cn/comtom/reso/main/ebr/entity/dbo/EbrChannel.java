package cn.comtom.reso.main.ebr.entity.dbo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.comtom.reso.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Table(name = "bc_ebr_channel")
public class EbrChannel implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name = "id")
    @ApiModelProperty(value = "主键")
    private String id;

    @Column(name = "ebrId")
    private String ebrId;

    @Column(name = "sendChannel")
    private String sendChannel;

}
