package Server;

public class Group {
    public boolean updated;
    public String title;
    public int course;
    public int id;
    public int external_id;
    public boolean actual;
    public int institute_id;

    public Group()
    {
        this.updated = false;
        this.title = null;
        this.course = 0;
        this.id = 0;
        this.external_id = 0;
        this.actual = false;
        this.institute_id = 0;
    }
}
