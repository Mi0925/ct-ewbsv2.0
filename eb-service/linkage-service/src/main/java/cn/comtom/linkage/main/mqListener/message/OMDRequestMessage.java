package cn.comtom.linkage.main.mqListener.message;

import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import lombok.Data;

@Data
public class OMDRequestMessage {

    OMDRequest omdRequest;

    String ebdId;

}
