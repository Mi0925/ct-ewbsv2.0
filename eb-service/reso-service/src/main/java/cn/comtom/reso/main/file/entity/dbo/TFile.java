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
@Table(name="bc_file_info")
public class TFile implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="id")
    private String id;

    @Column(name="originName")
    private String originName;

    @Column(name="uploadedName")
    private String uploadedName;

    @Column(name="fileType")
    private String fileType;

    @Column(name="fileUrl")
    private String fileUrl;

    @Column(name="fileExt")
    private String fileExt;

    @Column(name="md5Code")
    private String md5Code;

    @Column(name="secondLength")
    private Integer secondLength;

    @Column(name="byteSize")
    private Long byteSize;

    @Column(name="libId")
    private String libId;

    @Column(name="fileText")
    private String fileText;

    @Column(name="fileDesc")
    private String fileDesc;

    @Column(name="createUser")
    private String createUser;

    @Column(name="createDate")
    private Date createDate;

    @Column(name="auditState")
    private String auditState;

    @Column(name="auditComment")
    private String auditComment;

    @Column(name="auditDate")
    private Date auditDate;

    @Column(name="auditUser")
    private String auditUser;

    @Column(name = "filePath")
    private String filePath;



}
