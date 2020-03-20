package cn.comtom.linkage.main.access.model.ebd.validate;

public interface Validator {
  /**
   * 校验实体对象实例，返回出错信息
   * 
   * @param entity
   * @return
   */
  public String validateEntity(Object entity);
}
