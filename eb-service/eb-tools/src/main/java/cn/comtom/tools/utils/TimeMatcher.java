package cn.comtom.tools.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TimeMatcher implements Serializable {

    private Date startTime;

    private Date endTime;

}
