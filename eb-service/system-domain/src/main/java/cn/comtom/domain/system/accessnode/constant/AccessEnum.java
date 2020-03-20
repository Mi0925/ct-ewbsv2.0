package cn.comtom.domain.system.accessnode.constant;

/**
 * Create By wujiang on 2018/11/16
 */
public enum AccessEnum {
    ;
    public  enum AccessStatuEnum{
        NORMAL_STATU("1","正常"),
        STOP_STATU("0","停用");

        private String statu;
        private String msg;

        AccessStatuEnum(String statu, String msg) {
            this.statu=statu;
            this.msg=msg;
        }
        public String getStatu() {
            return statu;
        }
    }

}
