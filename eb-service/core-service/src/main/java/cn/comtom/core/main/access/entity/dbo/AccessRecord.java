package cn.comtom.core.main.access.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="bc_access_record")
public class AccessRecord implements Serializable {
    @Id
    @KeySql(genId = UUIdGenId.class)
    @ApiModelProperty(value = "主键")
    @Column(name = "id")
    private String id;

    @ApiModelProperty(value = "接入节点")
    @Column(name = "nodeId")
    private String nodeId;

    @ApiModelProperty(value = "接入时间")
    @Column(name = "receiveTime")
    private Date receiveTime;

    @ApiModelProperty(value = "接入状态")
    @Column(name = "status")
    private String status;

    @ApiModelProperty(value = "关联文件ID")
    @Column(name = "fileId")
    private String fileId;

    @ApiModelProperty(value = "信息类型")
    @Column(name = "type")
    private String type;

    @ApiModelProperty(value = "请求IP")
    @Column(name = "reqIp")
    private String reqIp;

    @Column(name = "ebdId")
    private String ebdId;

}
