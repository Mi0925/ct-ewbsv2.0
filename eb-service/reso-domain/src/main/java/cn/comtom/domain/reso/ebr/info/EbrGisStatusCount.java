package cn.comtom.domain.reso.ebr.info;

import lombok.Data;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/3/8 0008
 * @time: 下午 4:45
 */
@Data
public class EbrGisStatusCount {

     private String ebrTypeName;

     private String ebrType;

     private List<EbrstatusCount> data;

}
