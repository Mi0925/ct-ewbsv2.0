package cn.comtom.domain.reso.ebr.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EbrPlatformPageRequest extends CriterionRequest implements Serializable {

    private String psEbrId;

    private String parentPsEbrId;

    private String psEbrName;

    private String psType;

    private List<String> areaCodes;

    private String psState;

    private String areaCode;

    private String parentAreaCode;

    private String childAreaCode;

    private String platLevel;

    /**
     * 1,查询上级平台
     * 2.查询下级平台
     */
    private String queryType;

}
