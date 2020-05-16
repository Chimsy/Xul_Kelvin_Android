package zw.co.chimsy.xulkelvin.utils;

import okhttp3.MediaType;

public class AppConstants {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");



    //Keys
    public static final String KEY_DATA = "data";
    public static final String KEY_RESULT = "result";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_ERROR = "error";
    public static final String KEY_USER = "user";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_REG_NUMBER = "reg_num";
    public static final String KEY_YEAR = "year";
    public static final String KEY_SEMESTER = "semester";
    public static final String KEY_PROGRAM = "program";
    public static final String KEY_ACTUAL_TOKEN = "actual_token";



    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "android_api";

    // Login table name
    public static final String TABLE_USER = "user";

    // Login Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";


}
