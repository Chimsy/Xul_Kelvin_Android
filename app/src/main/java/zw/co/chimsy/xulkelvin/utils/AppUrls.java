package zw.co.chimsy.xulkelvin.utils;

public class AppUrls {

    private static String IP_ADDRESS = "192.168.43.187";

    //Web Hook
    public static String WEB_HOOK = "https://webhook.site/b705f6f0-1bc9-40eb-b8ab-5907edf9de30";

    // Server user login url
    public static String API_LOGIN = "http://" + IP_ADDRESS + "/api/login ";

    // Server user register url
    public static String API_REGISTER = "http://" + IP_ADDRESS + "/android_login_api/register.php";

    // Current Courses End-Point
//    public static String API_CURRENT_COURSES = "https://uniqueandrocode.000webhostapp.com/hiren/androidweb.php";
    public static String API_CURRENT_COURSES = "http://" + IP_ADDRESS + "/api/v1/current-courses";

}
