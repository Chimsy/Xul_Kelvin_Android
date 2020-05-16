package zw.co.chimsy.xulkelvin.ui.results.model;

public class List_Data_Classes {
    private String cource_id;
    private String course_name;
    private String course_description;

    public List_Data_Classes(String cource_id, String course_name, String course_description) {
        this.cource_id = cource_id;
        this.course_name = course_name;
        this.course_description = course_description;
    }


    public String getCource_id() {
        return cource_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_description() {
        return course_description;
    }

}