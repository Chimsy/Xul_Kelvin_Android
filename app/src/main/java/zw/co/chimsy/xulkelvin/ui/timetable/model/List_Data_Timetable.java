package zw.co.chimsy.xulkelvin.ui.timetable.model;

public class List_Data_Timetable {
    private int year, semester;
    private String course_id, exam_date, exam_time, exam_venue, exam_duration;


    public List_Data_Timetable(int year, int semester, String course_id, String exam_date, String exam_time, String exam_venue, String exam_duration) {
        this.year = year;
        this.semester = semester;
        this.course_id = course_id;
        this.exam_date = exam_date;
        this.exam_time = exam_time;
        this.exam_venue = exam_venue;
        this.exam_duration = exam_duration;
    }


    public int getYear() {
        return year;
    }

    public int getSemester() {
        return semester;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getExam_date() {
        return exam_date;
    }

    public String getExam_time() {
        return exam_time;
    }

    public String getExam_venue() {
        return exam_venue;
    }

    public String getExam_duration() {
        return exam_duration;
    }
}