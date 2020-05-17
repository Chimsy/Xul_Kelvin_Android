package zw.co.chimsy.xulkelvin.ui.classes.model;

public class List_Data_Classes {
    private String course_id;
    private String course_name;
    private String course_description;

    public List_Data_Classes(String course_id, String course_name, String course_description) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_description = course_description;
    }


    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_description() {
        return course_description;
    }

}