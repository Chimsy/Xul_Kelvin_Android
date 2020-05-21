package zw.co.chimsy.xulkelvin.ui.notifications.model;

public class List_Data_Notifications {
    private String msg_title;
    private String msg_body;


    public List_Data_Notifications(String msg_title, String msg_body) {
        this.msg_title = msg_title;
        this.msg_body = msg_body;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public String getMsg_body() {
        return msg_body;
    }
}
