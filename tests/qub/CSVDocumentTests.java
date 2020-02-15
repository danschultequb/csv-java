package qub;

public interface CSVDocumentTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CSVDocument.class, () ->
        {
            runner.testGroup("create(CSVRow...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create();
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(), document.getRows());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(CSVRow.create("a"));
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(CSVRow.create("a")), document.getRows());
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(CSVRow.create("a"), CSVRow.create("b", "c"));
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(CSVRow.create("a"), CSVRow.create("b", "c")), document.getRows());
                });

                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> CSVDocument.create((CSVRow[])null),
                        new PreConditionFailure("rows cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(new CSVRow[0]);
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(), document.getRows());
                });

                runner.test("with one-element array", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(new CSVRow[] { CSVRow.create() });
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(CSVRow.create()), document.getRows());
                });

                runner.test("with multiple-element array", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(new CSVRow[] { CSVRow.create(), CSVRow.create("a"), CSVRow.create("b") });
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(CSVRow.create(), CSVRow.create("a"), CSVRow.create("b")), document.getRows());
                });
            });

            runner.testGroup("create(Iterable<CSVRow>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CSVDocument.create((Iterable<CSVRow>)null),
                        new PreConditionFailure("rows cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(Iterable.create());
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(), document.getRows());
                });

                runner.test("with one value", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(Iterable.create(CSVRow.create()));
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(CSVRow.create()), document.getRows());
                });

                runner.test("with multiple values", (Test test) ->
                {
                    final CSVDocument document = CSVDocument.create(Iterable.create(CSVRow.create(), CSVRow.create("a"), CSVRow.create("b")));
                    test.assertNotNull(document, "document");
                    test.assertEqual(Iterable.create(CSVRow.create(), CSVRow.create("a"), CSVRow.create("b")), document.getRows());
                });
            });

            runner.testGroup("getRowCount()", () ->
            {
                final Action2<CSVDocument,Integer> getRowCountTest = (CSVDocument document, Integer expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(document.toString()), (Test test) ->
                    {
                        test.assertEqual(expected, document.getRowCount());
                    });
                };

                getRowCountTest.run(CSVDocument.create(), 0);
                getRowCountTest.run(CSVDocument.create().addRows(CSVRow.create("a")), 1);
                getRowCountTest.run(CSVDocument.create().addRows(CSVRow.create("a"), CSVRow.create("b")), 2);
                getRowCountTest.run(CSVDocument.create().addRows(CSVRow.create("a"), CSVRow.create("b"), CSVRow.create("b")), 3);
            });

            runner.testGroup("getRow(int)", () ->
            {
                final Action3<CSVDocument,Integer,Throwable> getRowErrorTest = (CSVDocument document, Integer rowIndex, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(document), rowIndex), (Test test) ->
                    {
                        test.assertThrows(() -> document.getRow(rowIndex), expected);
                    });
                };

                getRowErrorTest.run(CSVDocument.create(), -1, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getRowErrorTest.run(CSVDocument.create(), 0, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getRowErrorTest.run(CSVDocument.create(), 1, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                getRowErrorTest.run(CSVDocument.create(CSVRow.create()), -1, new PreConditionFailure("rowIndex (-1) must be equal to 0."));
                getRowErrorTest.run(CSVDocument.create(CSVRow.create()), 1, new PreConditionFailure("rowIndex (1) must be equal to 0."));

                getRowErrorTest.run(CSVDocument.create(CSVRow.create(), CSVRow.create()), -1, new PreConditionFailure("rowIndex (-1) must be between 0 and 1."));
                getRowErrorTest.run(CSVDocument.create(CSVRow.create(), CSVRow.create()), 2, new PreConditionFailure("rowIndex (2) must be between 0 and 1."));

                final Action3<CSVDocument,Integer,CSVRow> getRowTest = (CSVDocument document, Integer rowIndex, CSVRow expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(document), rowIndex), (Test test) ->
                    {
                        test.assertEqual(expected, document.getRow(rowIndex));
                    });
                };

                getRowTest.run(CSVDocument.create(CSVRow.create()), 0, CSVRow.create());
                getRowTest.run(CSVDocument.create(CSVRow.create("a"), CSVRow.create("b")), 0, CSVRow.create("a"));
                getRowTest.run(CSVDocument.create(CSVRow.create("a"), CSVRow.create("b")), 1, CSVRow.create("b"));
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<CSVDocument,String> toStringTest = (CSVDocument document, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(document), (Test test) ->
                    {
                        test.assertEqual(expected, document.toString());
                    });
                };

                toStringTest.run(CSVDocument.create(), "");
                toStringTest.run(CSVDocument.create(CSVRow.create()), "\n");
                toStringTest.run(CSVDocument.create(CSVRow.create("a", "b")), "a,b\n");
                toStringTest.run(CSVDocument.create(CSVRow.create("a", "b"), CSVRow.create("c")), "a,b\nc\n");
                toStringTest.run(CSVDocument.create(CSVRow.create("a", "b"), CSVRow.create(), CSVRow.create("c")), "a,b\n\nc\n");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<CSVDocument,Object,Boolean> equalsTest = (CSVDocument document, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(document), Strings.escapeAndQuote(rhs)), (Test test) ->
                    {
                        test.assertEqual(expected, document.equals(rhs));
                    });
                };

                equalsTest.run(CSVDocument.create(), null, false);
                equalsTest.run(CSVDocument.create(), "", false);
                equalsTest.run(CSVDocument.create(), CSVDocument.create(), true);
                equalsTest.run(CSVDocument.create(CSVRow.create()), CSVDocument.create(), false);
                equalsTest.run(CSVDocument.create(), CSVDocument.create(CSVRow.create()), false);
                equalsTest.run(CSVDocument.create(CSVRow.create()), CSVDocument.create(CSVRow.create()), true);
            });

            runner.testGroup("equals(CSVDocument)", () ->
            {
                final Action3<CSVDocument,CSVDocument,Boolean> equalsTest = (CSVDocument document, CSVDocument rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(document), Strings.escapeAndQuote(rhs)), (Test test) ->
                    {
                        test.assertEqual(expected, document.equals(rhs));
                    });
                };

                equalsTest.run(CSVDocument.create(), null, false);
                equalsTest.run(CSVDocument.create(), CSVDocument.create(), true);
                equalsTest.run(CSVDocument.create(CSVRow.create()), CSVDocument.create(), false);
                equalsTest.run(CSVDocument.create(), CSVDocument.create(CSVRow.create()), false);
                equalsTest.run(CSVDocument.create(CSVRow.create()), CSVDocument.create(CSVRow.create()), true);
            });
        });
    }
}
