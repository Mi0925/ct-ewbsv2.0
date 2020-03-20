package cn.comtom.domain.reso.ebr.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EbrStateUpdateRequest implements Serializable {

    @NotEmpty(message = "ebrId 不能为空")
    private String ebrId;

    @NotEmpty(message = "state 不能为空")
    private Integer state;

    private Integer syncFlag;

    private Integer statusSyncFlag;

}
