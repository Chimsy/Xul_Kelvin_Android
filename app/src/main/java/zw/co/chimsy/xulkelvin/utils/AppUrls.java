package zw.co.chimsy.xulkelvin.utils;

public class AppUrls {

    private static String IP_ADDRESS = "192.168.43.187";

    //Web Hook
    public static String WEB_HOOK = "https://webhook.site/b705f6f0-1bc9-40eb-b8ab-5907edf9de30";

    // Server user login url
    public static String API_LOGIN = "http://" + IP_ADDRESS + "/api/login ";

    /* Student Registration */
    public static String API_PROGRAM_IDS = "http://" + IP_ADDRESS + "/api/program";
    public static String API_REGISTER = "http://" + IP_ADDRESS + "/api/registration";
    /* Student Registration End*/

    // Current Courses End-Point
    public static String API_CURRENT_COURSES = "http://" + IP_ADDRESS + "/api/v1/current-courses";

    // Course Results End-Point
    public static String API_COURSES_RESULTS = "http://" + IP_ADDRESS + "/api/v1/results";

    // Course Timetable End-Point
    public static String API_TIMETABLE = "http://" + IP_ADDRESS + "/api/v1/timetable";

    // Messages
    public static String API_POST_MESSAGES = "http://" + IP_ADDRESS + "/api/v1/send-msg-help-desk";
    public static String API_GET_MESSAGES = "http://" + IP_ADDRESS + "/api/v1/receive-msg-help-desk";

    // Payments
    public static String API_PAYMENT_KYC = "http://" + IP_ADDRESS + "/api/v1/customer-details";
    public static String API_PAYMENT_PROCESS = "http://" + IP_ADDRESS + "/api/v1/process-payment";

    //PASS
    public static String API_PAYMENT_PASS_CLASS = "http://" + IP_ADDRESS + "/api/v1/pass-class";
    public static String API_PAYMENT_PASS_EXAM = "http://" + IP_ADDRESS + "/api/v1/pass-exam";



}
