package cn.comtom.domain.reso.ebr.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BroadcastPageRequest extends CriterionRequest implements Serializable {

    private String bsEbrId;

    private String bsName;

    private String bsType;

    private List<String> areaCodes;

    private String bsState;

    private String areaCode;

    private String syncFlag;

    private String statusSyncFlag;

    private String parentAreaCode;

    private String childAreaCode;

    private String queryType;


}
