package cn.comtom.ebs.front.main.ebr.model;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import lombok.Data;

import java.util.List;

@Data
public class EbrTypeEntity {

    private String ebrTypeName;

    private String ebrType;
    
    private String resType;

    private List<EbrInfo> ebrInfoList;
}
