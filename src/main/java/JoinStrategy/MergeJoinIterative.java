package JoinStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MergeJoinIterative {

    public static List<Row> mergeJoin(List<Row> table1, List<Row> table2) {

        iterativeMergeSort(table1);
        iterativeMergeSort(table2);

        List<Row> joinedRows = new ArrayList<>();
        int i = 0, j = 0;

        while (i < table1.size() && j < table2.size()) {
            Row row1 = table1.get(i);
            Row row2 = table2.get(j);

            if (row1.getId() == row2.getId()) {
                Row joinedRow = new Row(row1.getId(), row1.getValue() + ", " + row2.getValue());
                joinedRows.add(joinedRow);
                i++;
                j++;
            } else if (row1.getId() < row2.getId()) {
                i++;
            } else {
                j++;
            }
        }

        return joinedRows;
    }

    private static void iterativeMergeSort(List<Row> table) {
        int n = table.size();
        int currSize;
        int leftStart;

        for (currSize = 1; currSize <= n - 1; currSize = 2 * currSize) {
            for (leftStart = 0; leftStart < n - 1; leftStart += 2 * currSize) {
                int mid = Math.min(leftStart + currSize - 1, n - 1);
                int rightEnd = Math.min(leftStart + 2 * currSize - 1, n - 1);

                merge(table, leftStart, mid, rightEnd);
            }
        }
    }

    private static void merge(List<Row> table, int left, int mid, int right) {
        List<Row> leftList = new ArrayList<>(table.subList(left, mid + 1));
        List<Row> rightList = new ArrayList<>(table.subList(mid + 1, right + 1));
        int i = 0, j = 0, k = left;

        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i).getId() <= rightList.get(j).getId()) {
                table.set(k++, leftList.get(i++));
            } else {
                table.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            table.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()) {
            table.set(k++, rightList.get(j++));
        }
    }

    public static void main(String[] args) {
        int tableSize1 = 10_000_000;
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

        while (true) {
            List<Row> joinedRows = mergeJoin(table1, table2);
            System.out.println("Number of joined rows: " + joinedRows.size());
        }
    }
}
