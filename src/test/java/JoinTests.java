import JoinStrategy.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinTests {

    @Test
    public void testAllJoinsWithEmptyTables() {
        List<Row> table1 = new ArrayList<>();
        List<Row> table2 = new ArrayList<>();

        List<Row> mergeJoinResult = MergeJoin.mergeJoin(table1, table2);
        List<Row> nestedLoopJoinResult = NestedLoopJoin.nestedLoopJoin(table1, table2);
        List<Row> hashJoinResult = HashJoin.hashJoin(table1, table2);

        assertTrue(mergeJoinResult.isEmpty());
        assertTrue(nestedLoopJoinResult.isEmpty());
        assertTrue(hashJoinResult.isEmpty());
    }

    @Test
    public void testAllJoinsWithSingleMatch() {
        List<Row> table1 = List.of(new Row(1, "Value1"));
        List<Row> table2 = List.of(new Row(1, "Value2"));

        List<Row> mergeJoinResult = MergeJoin.mergeJoin(table1, table2);
        List<Row> nestedLoopJoinResult = NestedLoopJoin.nestedLoopJoin(table1, table2);
        List<Row> hashJoinResult = HashJoin.hashJoin(table1, table2);

        assertEquals(1, mergeJoinResult.size());
        assertEquals("Value1, Value2", mergeJoinResult.get(0).getValue());

        assertEquals(1, nestedLoopJoinResult.size());
        assertEquals("Value1, Value2", nestedLoopJoinResult.get(0).getValue());

        assertEquals(1, hashJoinResult.size());
        assertTrue("Value1, Value2".equals(hashJoinResult.get(0).getValue()) || "Value2, Value1".equals(hashJoinResult.get(0).getValue()));


    }

    @Test
    public void testAllJoinsWithMultipleMatches() {
        List<Row> table1 = new ArrayList<>(Arrays.asList(
                new Row(1, "Value1_1"),
                new Row(2, "Value1_2"),
                new Row(3, "Value1_3")
        ));
        List<Row> table2 = new ArrayList<>(Arrays.asList(
                new Row(1, "Value2_1"),
                new Row(2, "Value2_2"),
                new Row(4, "Value2_4")
        ));

        List<Row> mergeJoinResult = MergeJoin.mergeJoin(table1, table2);
        List<Row> nestedLoopJoinResult = NestedLoopJoin.nestedLoopJoin(table1, table2);
        List<Row> hashJoinResult = HashJoin.hashJoin(table1, table2);

        assertEquals(2, mergeJoinResult.size());
        assertEquals("Value1_1, Value2_1", mergeJoinResult.get(0).getValue());
        assertEquals("Value1_2, Value2_2", mergeJoinResult.get(1).getValue());

        assertEquals(2, nestedLoopJoinResult.size());
        assertEquals("Value1_1, Value2_1", nestedLoopJoinResult.get(0).getValue());
        assertEquals("Value1_2, Value2_2", nestedLoopJoinResult.get(1).getValue());

        assertEquals(2, hashJoinResult.size());
        assertTrue("Value1_1, Value2_1".equals(hashJoinResult.get(0).getValue()) || "Value2_1, Value1_1".equals(hashJoinResult.get(0).getValue()));
        assertTrue("Value1_2, Value2_2".equals(hashJoinResult.get(1).getValue()) || "Value2_2, Value1_2".equals(hashJoinResult.get(1).getValue()));

    }

    @Test
    public void testAllJoinsWithNoMatches() {
        List<Row> table1 = new ArrayList<>(Arrays.asList(
                new Row(1, "Value1_1"),
                new Row(2, "Value1_2"),
                new Row(3, "Value1_3")
        ));
        List<Row> table2 = new ArrayList<>(Arrays.asList(
                new Row(4, "Value2_4"),
                new Row(5, "Value2_5"),
                new Row(6, "Value2_6")
        ));

        List<Row> mergeJoinResult = MergeJoin.mergeJoin(table1, table2);
        List<Row> mergeJoinResultIterative = MergeJoinIterative.mergeJoin(table1, table2);
        List<Row> nestedLoopJoinResult = NestedLoopJoin.nestedLoopJoin(table1, table2);
        List<Row> hashJoinResult = HashJoin.hashJoin(table1, table2);

        assertTrue(mergeJoinResult.isEmpty());
        assertTrue(mergeJoinResultIterative.isEmpty());
        assertTrue(nestedLoopJoinResult.isEmpty());
        assertTrue(hashJoinResult.isEmpty());
    }

    @Test
    public void testAllJoinsWithLargeTables() {
        int tableSize = 1000000;
        List<Row> table1 = new ArrayList<>(tableSize);
        List<Row> table2 = new ArrayList<>(tableSize);

        for (int i = 0; i < tableSize; i++) {
            table1.add(new Row(i, "Value1_" + i));
            table2.add(new Row(i, "Value2_" + i));
        }

        List<Row> mergeJoinResult = MergeJoin.mergeJoin(table1, table2);
        List<Row> mergeJoinResultIterative = MergeJoinIterative.mergeJoin(table1, table2);
        List<Row> hashJoinResult = HashJoin.hashJoin(table1, table2);
        //List<Row> nestedLoopJoinResult = NestedLoopJoin.nestedLoopJoin(table1, table2);


        assertEquals(tableSize, mergeJoinResult.size());
        assertEquals(tableSize, mergeJoinResultIterative.size());
        assertEquals(tableSize, hashJoinResult.size());
        //assertEquals(tableSize, nestedLoopJoinResult.size());


        for (int i = 0; i < tableSize; i++) {
            assertEquals("Value1_" + i + ", Value2_" + i, mergeJoinResult.get(i).getValue());
            assertEquals("Value1_" + i + ", Value2_" + i, mergeJoinResultIterative.get(i).getValue());
            assertTrue(("Value1_" + i + ", Value2_" + i).equals(hashJoinResult.get(i).getValue()) ||
                    ("Value2_" + i + ", Value1_" + i).equals(hashJoinResult.get(i).getValue()));
            //assertEquals("Value1_" + i + ", Value2_" + i, nestedLoopJoinResult.get(i).getValue());


        }
    }
}
