package cn.comtom.core.fw;

import tk.mybatis.mapper.genid.GenId;

import java.util.UUID;

/**
 * UUID主键生成类
 */
public class UUIdGenId implements GenId<String> {
    @Override
    public String genId(String table, String column) {
        return UUID.randomUUID().toString().replace("-","");
    }
}