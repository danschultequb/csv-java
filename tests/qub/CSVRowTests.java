package qub;

public interface CSVRowTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CSVRow.class, () ->
        {
            runner.testGroup("create(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final CSVRow row = CSVRow.create("a");
                    test.assertEqual(Iterable.create("a"), row.getCells());
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final CSVRow row = CSVRow.create("a", "b", "c");
                    test.assertEqual(Iterable.create("a", "b", "c"), row.getCells());
                });

                runner.test("with argument with comma in it", (Test test) ->
                {
                    final CSVRow row = CSVRow.create("a", "b,c", "d");
                    test.assertEqual(Iterable.create("a", "b,c", "d"), row.getCells());
                });

                runner.test("with argument with newline in it", (Test test) ->
                {
                    final CSVRow row = CSVRow.create("a", "b\nc", "d");
                    test.assertEqual(Iterable.create("a", "b\nc", "d"), row.getCells());
                });

                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> CSVRow.create((String[])null),
                        new PreConditionFailure("cells cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CSVRow row = CSVRow.create(new String[0]);
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with array with one element", (Test test) ->
                {
                    final CSVRow row = CSVRow.create(new String[] { "a" });
                    test.assertEqual(Iterable.create("a"), row.getCells());
                });

                runner.test("with array with multiple elements", (Test test) ->
                {
                    final CSVRow row = CSVRow.create(new String[] { "a", "b", "c" });
                    test.assertEqual(Iterable.create("a", "b", "c"), row.getCells());
                });
            });

            runner.testGroup("create(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CSVRow.create((Iterable<String>)null),
                        new PreConditionFailure("cells cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CSVRow row = CSVRow.create(Iterable.create());
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with one value", (Test test) ->
                {
                    final CSVRow row = CSVRow.create(Iterable.create("a"));
                    test.assertEqual(Iterable.create("a"), row.getCells());
                });

                runner.test("with multiple values", (Test test) ->
                {
                    final CSVRow row = CSVRow.create(Iterable.create("a", "b", "c"));
                    test.assertEqual(Iterable.create("a", "b", "c"), row.getCells());
                });
            });

            runner.testGroup("getCell(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    test.assertThrows(() -> row.getCell(-1),
                        new PreConditionFailure("columnIndex (-1) must be greater than or equal to 0."));
                });

                final Action3<CSVRow,Integer,String> getCellStringTest = (CSVRow row, Integer columnIndex, String expected) ->
                {
                    runner.test("with " + English.andList(row, columnIndex), (Test test) ->
                    {
                        test.assertEqual(expected, row.getCell(columnIndex));
                    });
                };

                getCellStringTest.run(CSVRow.create(), 0, null);
                getCellStringTest.run(CSVRow.create(), 1, null);
                getCellStringTest.run(CSVRow.create("a"), 0, "a");
                getCellStringTest.run(CSVRow.create("a"), 1, null);
                getCellStringTest.run(CSVRow.create("\"a,b\""), 0, "\"a,b\"");
                getCellStringTest.run(CSVRow.create("a,b"), 0, "a,b");
                getCellStringTest.run(CSVRow.create("a", "b", "c", "d"), 3, "d");
            });

            runner.testGroup("addCell(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    test.assertThrows(() -> row.addCell(null),
                        new PreConditionFailure("cell cannot be null."));
                    test.assertEqual(0, row.getCellCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellResult = row.addCell("");
                    test.assertSame(row, addCellResult);
                    test.assertEqual(Iterable.create(""), row.getCells());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellResult = row.addCell("z");
                    test.assertSame(row, addCellResult);
                    test.assertEqual(Iterable.create("z"), row.getCells());
                });
            });

            runner.testGroup("addCells(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells();
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells("a");
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create("a"), row.getCells());
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells("a", "b", "c");
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create("a", "b", "c"), row.getCells());
                });

                runner.test("with null array", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    test.assertThrows(() -> row.addCells((String[])null),
                        new PreConditionFailure("cells cannot be null."));
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells(new String[0]);
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with one element array", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells(new String[] { "a" });
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create("a"), row.getCells());
                });

                runner.test("with multiple element array", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells(new String[] { "a", "b", "c" });
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create("a", "b", "c"), row.getCells());
                });
            });

            runner.testGroup("addCells(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    test.assertThrows(() -> row.addCells((Iterable<String>)null),
                        new PreConditionFailure("cells cannot be null."));
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells(Iterable.create());
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create(), row.getCells());
                });

                runner.test("with one value", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells(Iterable.create("a"));
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create("a"), row.getCells());
                });

                runner.test("with multiple values", (Test test) ->
                {
                    final CSVRow row = CSVRow.create();
                    final CSVRow addCellsResult = row.addCells(Iterable.create("a", "b", "c"));
                    test.assertSame(row, addCellsResult);
                    test.assertEqual(Iterable.create("a", "b", "c"), row.getCells());
                });
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<CSVRow,String> toStringTest = (CSVRow row, String expected) ->
                {
                    runner.test("with " + row, (Test test) ->
                    {
                        test.assertEqual(expected, row.toString());
                    });
                };

                toStringTest.run(CSVRow.create(), "");
                toStringTest.run(CSVRow.create("a"), "a");
                toStringTest.run(CSVRow.create("a", ""), "a,");
                toStringTest.run(CSVRow.create("a", "b", "c"), "a,b,c");
                toStringTest.run(CSVRow.create("a", "b,c", "d"), "a,\"b,c\",d");
                toStringTest.run(CSVRow.create("a", "b\nc", "d"), "a,\"b\nc\",d");
                toStringTest.run(CSVRow.create("a", "b'c", "d"), "a,b'c,d");
                toStringTest.run(CSVRow.create("a", "b\"c", "d"), "a,b\"c,d");
                toStringTest.run(CSVRow.create("a", "b,\"c\"", "d"), "a,\"b,\\\"c\\\"\",d");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<CSVRow,Object,Boolean> equalsTest = (CSVRow row, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(row, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, row.equals(rhs));
                    });
                };

                equalsTest.run(CSVRow.create(), null, false);
                equalsTest.run(CSVRow.create(), "", false);
                equalsTest.run(CSVRow.create(), CSVRow.create(), true);
                equalsTest.run(CSVRow.create("a"), CSVRow.create(), false);
                equalsTest.run(CSVRow.create(), CSVRow.create("a"), false);
                equalsTest.run(CSVRow.create("a"), CSVRow.create("a"), true);
                equalsTest.run(CSVRow.create("a", "b"), CSVRow.create("a,b"), false);
            });

            runner.testGroup("equals(CSVRow)", () ->
            {
                final Action3<CSVRow,CSVRow,Boolean> equalsTest = (CSVRow row, CSVRow rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(row, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, row.equals(rhs));
                    });
                };

                equalsTest.run(CSVRow.create(), null, false);
                equalsTest.run(CSVRow.create(), CSVRow.create(), true);
                equalsTest.run(CSVRow.create("a"), CSVRow.create(), false);
                equalsTest.run(CSVRow.create(), CSVRow.create("a"), false);
                equalsTest.run(CSVRow.create("a"), CSVRow.create("a"), true);
                equalsTest.run(CSVRow.create("a", "b"), CSVRow.create("a,b"), false);
            });
        });
    }
}
