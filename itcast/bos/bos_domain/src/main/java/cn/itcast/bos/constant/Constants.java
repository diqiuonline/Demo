package cn.itcast.bos.constant;

/**
 * User: 李锦卓
 * Time: 2018/8/31 20:52
 */
public class Constants {
    public static final String BOS_MANAGEMENT_HOST = "http://localhost:8080";
    public static final String CRM_MANAGEMENT_HOST = "http://localhost:9002";
    public static final String BOS_FORE_HOST = "http://localhost:9003";
    public static final String BOS_SMS_HOST = "http://localhost:9004";

    private static final String BOS_MANAGEMENT_CONTEXT = "/bos";
    private static final String CRM_MANAGEMENT_CONTEXT = "/crm_management";
    private static final String BOS_FORE_CONTEXT = "/bos_fore";
    private static final String BOS_SMS_CONTEXT = "/bos_sms";

    public static final String BOS_MANAGEMENT_URL = BOS_MANAGEMENT_HOST + BOS_MANAGEMENT_CONTEXT;
    public static final String CRM_MANAGEMENT_URL = CRM_MANAGEMENT_HOST + CRM_MANAGEMENT_CONTEXT;
    public static final String BOS_FORE_URL = BOS_FORE_HOST + BOS_FORE_CONTEXT;
    public static final String BOS_SMS_URL = BOS_SMS_HOST + BOS_SMS_CONTEXT;
}