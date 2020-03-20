package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdItemUnit;
import cn.comtom.core.main.ebm.service.IEbmBrdItemUnitService;
import cn.comtom.domain.core.ebm.info.EbmBrdItemUnitInfo;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/core/ebmBrd/itemUnit")
@Api(tags = "播发记录的播放部门人员信息")
@Slf4j
public class EbmBrdItemUnitController extends BaseController {

    @Autowired
    private IEbmBrdItemUnitService ebmBrdItemUnitService;


    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存播发记录的播放部门人员信息", notes = "批量保存播发记录的播放部门人员信息")
    public ApiEntityResponse<Integer> saveBatch(@RequestBody @Valid List<EbmBrdItemUnitInfo> ebmBrdItemUnitInfoList, BindingResult bindResult) {
        List<EbmBrdItemUnit> ebmBrdItemUnitList = Optional.ofNullable(ebmBrdItemUnitInfoList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ebmBrdItemUnitInfo -> {
                    EbmBrdItemUnit ebmBrdItemUnit = new EbmBrdItemUnit();
                    BeanUtils.copyProperties(ebmBrdItemUnitInfo, ebmBrdItemUnit);
                    if (StringUtils.isBlank(ebmBrdItemUnit.getId())) {
                        ebmBrdItemUnit.setId(UUIDGenerator.getUUID());
                    }
                    return ebmBrdItemUnit;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(ebmBrdItemUnitService.saveList(ebmBrdItemUnitList));
    }


}
