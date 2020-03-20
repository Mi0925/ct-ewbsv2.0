package cn.comtom.domain.reso.file.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class VFileLibPageRequest  extends CriterionRequest implements Serializable {

    private String libName;

    private String parentLibId;

    private String parentLibName;

    private String type;

    private String auditState;


}
