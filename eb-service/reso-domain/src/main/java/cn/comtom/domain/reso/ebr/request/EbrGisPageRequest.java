package cn.comtom.domain.reso.ebr.request;

import cn.comtom.domain.reso.fw.CriterionRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author:WJ
 * @date: 2019/3/7 0007
 * @time: 下午 4:46
 */
@Data
public class EbrGisPageRequest extends CriterionRequest implements Serializable {

    private Integer status;

    private String relatedPsEbrId;

    private String ebrType;

}
