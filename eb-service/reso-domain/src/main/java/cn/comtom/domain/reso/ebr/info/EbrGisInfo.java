package cn.comtom.domain.reso.ebr.info;

import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.StateDictEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:WJ
 * @date: 2019/3/7 0007
 * @time: 下午 4:16
 */
public class EbrGisInfo {

    private String longitude;

    private String latitude;

    private String name;

    private Integer style;

    private String ebrType;

    private Integer status;

    private List<Double> lnglat;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * 重写style的get方法
     * @param
     */
    public Integer getStyle() {
        if(this.getStatus() == null){
            return 0;
        }
        //根据资源类型以及资源状态确定返回的样式
      if(this.getEbrType().equals(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM)) {
          if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_PLAYING.getKey())){
              return 6;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey())){
              return 7;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey())){
              return 8;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_FAULT.getKey())){
              return 9;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RECOVER.getKey())){
              return 10;
          }
      } else if(this.getEbrType().equals(Constants.EBR_SUB_SOURCE_TYPE_BROADCAST)) {
          if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_PLAYING.getKey())){
              return 11;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey())){
              return 12;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey())){
              return 13;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_FAULT.getKey())){
              return 14;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RECOVER.getKey())){
              return 15;
          }
      }else if(this.getEbrType().equals(Constants.EBR_SUB_SOURCE_TYPE_TERMINAL)) {
          if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_PLAYING.getKey())){
              return 1;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey())){
              return 2;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey())){
              return 3;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_FAULT.getKey())){
              return 4;
          }else if(this.getStatus()==Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RECOVER.getKey())){
              return 5;
          }
      }
        return 0;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public String getEbrType() {
        return ebrType;
    }

    public void setEbrType(String ebrType) {
        this.ebrType = ebrType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 重写lnglat的get方法
     * @return
     */
    public List<Double> getLnglat() {
        List<Double> list=new ArrayList<Double>();
        if(StringUtils.isNotEmpty(longitude)&&StringUtils.isNotEmpty(latitude)){
            list.add(Double.parseDouble(longitude));
            list.add(Double.parseDouble(latitude));
        }
        return list;
    }

    public void setLnglat(List<Double> lnglat) {
        this.lnglat = lnglat;
    }
}
