package cn.comtom.linkage.main.access.constant;

/**
 * Create By wujiang on 2018/11/17
 */
public enum AccessRecordEnum {
    ;
    public  enum accessRecordStatuEnum{
        IP_CHECK_FAIL("0","IP效验失败"),
        RECEIVE_DATA_FAIL("1","数据接收失败"),
        PARSE_DATA_FAIL("2","数据解析失败"),
        AUTH_FAIL("3","身份认证失败"),
        VERIFY_DATA_FAIL("4","数据校验失败"),
        SAVE_DATA_FAIL("5","数据存储失败"),
        SUCCESS("6","接入成功");

        private String statu;
        private String msg;

        accessRecordStatuEnum(String statu, String msg) {
            this.statu=statu;
            this.msg=msg;
        }


        public String getStatu() {
            return statu;
        }
    }
}
