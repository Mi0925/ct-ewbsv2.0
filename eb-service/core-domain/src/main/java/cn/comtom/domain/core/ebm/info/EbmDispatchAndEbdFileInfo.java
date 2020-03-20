package cn.comtom.domain.core.ebm.info;

import lombok.Data;

import java.io.Serializable;

@Data
public class EbmDispatchAndEbdFileInfo implements Serializable {

    private String ebrId;

    private String ebdId;

    private String ebmId;

    private Integer state;

    private String url;

    private String fileId;

    private String fileSize;

    private String ebrName;

    private String stateName;

}
