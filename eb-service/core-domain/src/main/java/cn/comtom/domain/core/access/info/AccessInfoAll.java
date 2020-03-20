package cn.comtom.domain.core.access.info;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AccessInfoAll extends AccessInfo implements Serializable {

    private List<AccessAreaInfo> accessAreaInfoList;

    private List<AccessStrategyAll> accessStrategyList;

    private List<AccessFileInfo> accessFileInfoList;



}
