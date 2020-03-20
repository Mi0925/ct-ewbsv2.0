package cn.comtom.domain.reso.ebr.info;

import lombok.Data;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/3/8 0008
 * @time: 下午 3:29
 */
@Data
public class EbrGisInitParame {

    private Integer zoom;

    private List<Double> center;
}
