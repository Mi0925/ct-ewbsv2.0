package cn.comtom.reso.main.file.entity.dbo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="v_file_lib")
public class VFileLib  implements Serializable {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="libName")
    private String libName;

    @Column(name="url")
    private String url;

    @Column(name="parentLibId")
    private String parentLibId;

    @Column(name="parentLibName")
    private String parentLibName;

    @Column(name="createDate")
    private Date createDate;

    @Column(name="createUser")
    private String createUser;

    @Column(name="type")
    private String type;

    @Column(name="typeName")
    private String typeName;

    @Column(name="byteSize")
    private String byteSize;

    @Column(name="secondLength")
    private String secondLength;

    @Column(name="auditState")
    private String auditState;
}
