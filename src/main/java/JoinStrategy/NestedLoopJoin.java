package JoinStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NestedLoopJoin {


    public static List<Row> nestedLoopJoin(List<Row> table1, List<Row> table2) {
        List<Row> joinedRows = new ArrayList<>();

        for (Row row1 : table1) {
            for (Row row2 : table2) {
                if (row1.getId() == row2.getId()) {
                    Row joinedRow = new Row(row1.getId(), row1.getValue() + ", " + row2.getValue());
                    joinedRows.add(joinedRow);
                }
            }
        }

        return joinedRows;
    }

    public static void main(String[] args) {
        int tableSize1 = 1_000_000;
        int tableSize2 = 500_000;
        List<Row> table1 = new ArrayList<>(tableSize1);
        List<Row> table2 = new ArrayList<>(tableSize2);
        Random random = new Random();

        for (int i = 0; i < tableSize1; i++) {
            int id = random.nextInt(tableSize1);
            String value = "Value1_" + i;
            table1.add(new Row(id, value));
        }

        for (int i = 0; i < tableSize2; i++) {
            int id = random.nextInt(tableSize2);
            String value = "Value2_" + i;
            table2.add(new Row(id, value));
        }
        long pid = ProcessHandle.current().pid();
        System.out.println("PID " + pid);

        while (true) {
            List<Row> joinedRows = nestedLoopJoin(table1, table2);
            System.out.println("Number of joined rows: " + joinedRows.size());
        }
    }
}
