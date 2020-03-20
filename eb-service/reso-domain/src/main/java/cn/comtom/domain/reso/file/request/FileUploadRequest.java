package cn.comtom.domain.reso.file.request;

import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

@Data
public class FileUploadRequest implements Serializable {
	
    private  String libId;

    private String originName;

    private String uploadedName;

    private String auditState;
}
