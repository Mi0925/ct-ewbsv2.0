package cn.comtom.core.main.ebd.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "bc_ebd_files")
public class EbdFiles implements Serializable {

    @Column(name="ebdId")
    @ApiModelProperty(value = "业务数据包ID")
    private String ebdId;


    @Column(name = "fileId")
    @ApiModelProperty(value = "文件ID")
    private String fileId;

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "主键")
    private String id;

}
