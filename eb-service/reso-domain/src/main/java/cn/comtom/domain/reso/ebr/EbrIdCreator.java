package cn.comtom.domain.reso.ebr;

import lombok.Data;

import java.io.Serializable;

@Data
public class EbrIdCreator implements Serializable {

    private String sourceLevel;

    private String areaCode;

    private String sourceTypeCode;

    private String sourceTypeSN;

    private String sourceSubTypeCode;

    private String sourceSubTypeSN;

    private String ebrId;
}
