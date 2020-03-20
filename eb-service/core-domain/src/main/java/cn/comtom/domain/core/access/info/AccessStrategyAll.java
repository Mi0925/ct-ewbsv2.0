package cn.comtom.domain.core.access.info;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AccessStrategyAll extends AccessStrategyInfo implements Serializable {

    private List<AccessTimeInfo> accessTimeInfoList;
}
