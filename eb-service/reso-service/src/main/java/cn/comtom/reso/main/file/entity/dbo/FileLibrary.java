package cn.comtom.reso.main.file.entity.dbo;

import cn.comtom.reso.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="bc_file_library")
public class FileLibrary implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="libId")
    private String libId;

    @Column(name="libName")
    private String libName;

    @Column(name="libURI")
    private String libURI;

    @Column(name="libType")
    private String libType;

    @Column(name="createUser")
    private String createUser;

    @Column(name="parentLibId")
    private String parentLibId;

    @Column(name="createDate")
    private Date createDate;


}
