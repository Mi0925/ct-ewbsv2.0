package cn.comtom.linkage.main.access.constant;

/**
 * Create By wujiang on 2018/11/16
 */
public enum FileEnum {

    ;
    public  enum FileTypeEnum{
        receive_file("1","接收文件"),
        send_file("2","发送文件");

        private String type;
        private String msg;

        FileTypeEnum(String type, String msg) {
            this.type=type;
            this.msg=msg;
        }


        public String getType() {
            return type;
        }
    }

    public  enum FileStatuEnum{
        normal_statu("1","正常"),
        del_statu("0","删除");

        private String statu;
        private String msg;

        FileStatuEnum(String statu, String msg) {
            this.statu=statu;
            this.msg=msg;
        }


        public String getStatu() {
            return statu;
        }
    }

}
