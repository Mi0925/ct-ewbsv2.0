package cn.comtom.core.main.scheme.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.scheme.dao.SchemeEbrDao;
import cn.comtom.core.main.scheme.entity.dbo.SchemeEbr;
import cn.comtom.core.main.scheme.service.ISchemeEbrService;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemeEbrServiceImpl extends BaseServiceImpl<SchemeEbr,String> implements ISchemeEbrService {

    @Autowired
    private SchemeEbrDao dao;

    @Override
    public BaseDao<SchemeEbr, String> getDao() {
        return dao;
    }

    @Override
    public List<SchemeEbrInfo> getSchemeEbrListBySchemeId(String schemeId) {
       return  dao.getSchemeEbrListBySchemeId(schemeId);
    }

	@Override
	public Boolean deleteBySchemeId(String schemeId) {
		SchemeEbr schemeEbr = new SchemeEbr();
		schemeEbr.setSchemeId(schemeId);
		int delete = dao.delete(schemeEbr);
		return true;
	}
}
