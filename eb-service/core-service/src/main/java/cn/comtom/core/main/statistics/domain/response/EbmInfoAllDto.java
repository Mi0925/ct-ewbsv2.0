package cn.comtom.core.main.statistics.domain.response;

import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangbingyan
 * @date 2020/1/9
 * @desc
 */
@Data
public class EbmInfoAllDto extends Ebm implements Serializable {

    /**
     * 本平台的经纬度
     * */
    private String longitude;

    private String latitude;

    /**
     * 消息发送者的经纬度
     * */
    private String pLongitude;

    private String platitude;

    /**
     * 消息下发资源
     */
    private List<EbrDto> ebrDtoList ;
    /**
     * 流程状态
     */
    private Byte flowstage;

    /**
     * 流程状态
     */
    private Byte flowstate;

    /**
     * 区域名称
     */
    private List<String> areaNames;

    /**
     * 本级平台资源id
     */
    private String ebrId;

}
