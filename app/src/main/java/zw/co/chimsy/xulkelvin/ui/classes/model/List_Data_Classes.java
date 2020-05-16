package zw.co.chimsy.xulkelvin.ui.classes.model;

public class List_Data_Classes {
    private String name;
    private String image_url;

    public List_Data_Classes(String name, String image_url) {
        this.name = name;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }
}