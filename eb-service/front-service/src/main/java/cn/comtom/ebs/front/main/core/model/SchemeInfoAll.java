package cn.comtom.ebs.front.main.core.model;

import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SchemeInfoAll  extends SchemeInfo implements Serializable {

    private SysPlanMatchInfo sysPlanMatchInfo;

    private List<SchemeEbrInfo> schemeEbrInfoList;

}
