package cn.comtom.domain.reso.file.info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VFileLibInfo implements Serializable {

    private String id;

    private String libName;

    private String url;

    private String parentLibId;

    private String parentLibName;

    private Date createDate;

    private String createUser;

    private String type;

    private String typeName;

    private String byteSize;

    private String secondLength;

    private String auditState;
}
