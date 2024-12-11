package JoinStrategy;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HashJoin {

    public static List<Row> hashJoin(List<Row> table1, List<Row> table2) {
        Int2ObjectOpenHashMap<Row> hashMap = new Int2ObjectOpenHashMap<>();
        List<Row> joinedRows = new ArrayList<>();

        List<Row> smallerTable;
        List<Row> largerTable;

        if (table1.size() < table2.size()) {
            smallerTable = table1;
            largerTable = table2;
        } else {
            smallerTable = table2;
            largerTable = table1;
        }

        for (Row row : smallerTable) {
            hashMap.put(row.getId(), row);
        }

        for (Row row : largerTable) {
            if (hashMap.containsKey(row.getId())) {
                Row joinedRow = new Row(row.getId(), hashMap.get(row.getId()).getValue() + ", " + row.getValue());
                joinedRows.add(joinedRow);
            }
        }

        return joinedRows;
    }

    public static void main(String[] args) {
        int tableSize1 = 1_000_0000;
        int tableSize2 = 5_000_000;
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

        while(true) {
            List<Row> joinedRows = hashJoin(table1, table2);
            System.out.println("Number of joined rows: " + joinedRows.size());
        }
    }
}
