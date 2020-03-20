package cn.comtom.linkage.main.access.model.ebd.commom;

import lombok.Data;

/**
 * Create By wujiang on 2018/11/17
 */
@Data
public class AccessRecord {

    private String nodeId;

    private String status;

    private String fileId;

    private String type;

    private String reqIp;

    private String ebdId;
}
