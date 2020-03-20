package cn.comtom.domain.core.scheme.request;

import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SchemeEbrAddBatchRequest implements Serializable {

    private List<SchemeEbrInfo> schemeEbrInfoList;
}
