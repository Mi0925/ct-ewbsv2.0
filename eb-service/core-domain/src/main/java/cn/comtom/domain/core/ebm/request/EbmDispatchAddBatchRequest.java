package cn.comtom.domain.core.ebm.request;

import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EbmDispatchAddBatchRequest implements Serializable {

    private List<EbmDispatchInfo> ebmDispatchInfoList;
}
