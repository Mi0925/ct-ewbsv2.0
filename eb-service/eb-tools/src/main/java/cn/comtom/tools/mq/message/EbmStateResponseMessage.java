package cn.comtom.tools.mq.message;

import lombok.Data;

@Data
public class EbmStateResponseMessage {

    private String ebmId;
    //触发类型
    private String handType;

    private String ebdId;
    
    private String broadcastState;
}
