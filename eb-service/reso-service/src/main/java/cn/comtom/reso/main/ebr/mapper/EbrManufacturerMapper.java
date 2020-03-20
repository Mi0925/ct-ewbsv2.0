package cn.comtom.reso.main.ebr.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrManufacturer;

@Mapper
@Repository
public interface EbrManufacturerMapper extends ResoMapper<EbrManufacturer, String> {

}
