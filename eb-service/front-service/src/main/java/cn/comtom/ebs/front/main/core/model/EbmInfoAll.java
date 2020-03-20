package cn.comtom.ebs.front.main.core.model;

import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EbmInfoAll extends EbmInfo implements Serializable {


    /**
     * ebd数据包文件
     */
    private OriginFileInfo originFileInfo;


    private List<EbmAuxiliaryInfo> ebmAuxiliaryInfoList;

    private  List<EbmDispatchInfo> ebmDispatchInfoList;

    private List<EbrInfo> ebrInfoList ;

    private SchemeInfoAll schemeInfoAll;

    private DispatchFlowInfo dispatchFlowInfo;

    private EbmDispatchInfoInfo ebmDispatchInfoInfo;


}
