package cn.comtom.system.main.eventtype.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "sys_event_type")
public class EventType implements Serializable {

    @Id
    @Column(name = "eventCode")
    private String eventCode;

    @Column(name = "eventDesc")
    private String eventDesc;

    @Column(name = "parentCode")
    private String parentCode;


}
