package com.ant.common.constant;

public class WareConstant {
    public enum PurchaseStatusEnum{
        CREATED(0,"�½�"),
        ASSIGNED(1,"�ѷ���"),
        RECEIVE(2,"����ȡ"),
        FINISH(3,"�����"),
        HASERR(4,"���쳣");

        private int code;

        private String msg;

        PurchaseStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum PurchaseDetailStatusEnum{
        CREATED(0,"�½�"),
        ASSIGNED(1,"�ѷ���"),
        BUYING(2,"���ڲɹ�"),
        FINISH(3,"�����"),
        FAILED(4,"�ɹ�ʧ��");

        private int code;

        private String msg;

        PurchaseDetailStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
