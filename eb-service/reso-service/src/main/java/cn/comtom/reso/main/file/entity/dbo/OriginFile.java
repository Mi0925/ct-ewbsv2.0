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
@Table(name="bc_origin_file_info")
public class OriginFile implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="file_id")
    private String fileId;

    @Column(name="origin_name")
    private String originName;

    @Column(name="url")
    private String url;

    @Column(name="file_type")
    private String fileType;

    @Column(name="fileExt")
    private String fileExt;

    @Column(name="md5Code")
    private String md5Code;

    @Column(name="file_desc")
    private String fileDesc;

    @Column(name="creator")
    private String creator;

    @Column(name="create_time")
    private Date createTime;

    @Column(name="status")
    private String status;

    @Column(name="file_size")
    private Long fileSize;

}
