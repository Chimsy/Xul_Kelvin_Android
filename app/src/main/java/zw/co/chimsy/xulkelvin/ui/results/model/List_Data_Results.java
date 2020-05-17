package zw.co.chimsy.xulkelvin.ui.results.model;

public class List_Data_Results {
    private String cource_id;
    private String course_work_mark;
    private String course_exam_mark;
    private String course_exam_grade;


    public List_Data_Results(String cource_id, String course_work_mark, String course_exam_mark, String course_exam_grade) {
        this.cource_id = cource_id;
        this.course_work_mark = course_work_mark;
        this.course_exam_mark = course_exam_mark;
        this.course_exam_grade = course_exam_grade;
    }

    public String getCource_id() {
        return cource_id;
    }

    public String getCourse_work_mark() {
        return course_work_mark;
    }

    public String getCourse_exam_mark() {
        return course_exam_mark;
    }

    public String getCourse_exam_grade() {
        return course_exam_grade;
    }
}