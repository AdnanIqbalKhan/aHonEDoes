package chefcharlesmich.smartappphonebook.Models;

public class Category {
    public int id, group_id;
    public String name;

    public Category(int id, int group_id, String name) {
        this.id = id;
        this.group_id = group_id;
        this.name = name;
    }
}
