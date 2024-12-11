package JoinStrategy;

public class Row {
    private int id;
    private String value;

    public Row(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Row{id=" + id + ", value='" + value + "'}";
    }
}