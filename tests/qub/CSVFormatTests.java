package qub;

public interface CSVFormatTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CSVFormat.class, () ->
        {
            runner.test("commaSeparated", (Test test) ->
            {
                test.assertEqual(
                    CSVFormat.create()
                        .setCellSeparator(',')
                        .setQuote('\"'),
                    CSVFormat.commaSeparated);
            });

            runner.test("tabSeparated", (Test test) ->
            {
                test.assertEqual(
                    CSVFormat.create()
                        .setCellSeparator('\t')
                        .setQuote('\"'),
                    CSVFormat.tabSeparated);
            });

            runner.test("create()", (Test test) ->
            {
                final CSVFormat format = CSVFormat.create();
                test.assertNotNull(format);
                test.assertEqual(',', format.getCellSeparator());
                test.assertEqual('\"', format.getQuote());
            });

            runner.test("setCellSeparator(char)", (Test test) ->
            {
                final CSVFormat format = CSVFormat.create();
                final CSVFormat setCellSeparatorResult = format.setCellSeparator('|');
                test.assertSame(format, setCellSeparatorResult);
                test.assertEqual('|', format.getCellSeparator());
            });

            runner.test("setQuote(char)", (Test test) ->
            {
                final CSVFormat format = CSVFormat.create();
                final CSVFormat setCellSeparatorResult = format.setQuote('\'');
                test.assertSame(format, setCellSeparatorResult);
                test.assertEqual('\'', format.getQuote());
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<CSVFormat,String> toStringTest = (CSVFormat format, String expected) ->
                {
                    runner.test("with " + format, (Test test) ->
                    {
                        test.assertEqual(expected, format.toString());
                    });
                };

                toStringTest.run(CSVFormat.commaSeparated, "{\"cellSeparator\":\",\",\"quote\":\"\\\"\"}");
                toStringTest.run(CSVFormat.tabSeparated, "{\"cellSeparator\":\"\\t\",\"quote\":\"\\\"\"}");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<CSVFormat,Object,Boolean> equalsTest = (CSVFormat lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(CSVFormat.commaSeparated, null, false);
                equalsTest.run(CSVFormat.commaSeparated, "hello", false);
                equalsTest.run(CSVFormat.commaSeparated, CSVFormat.commaSeparated, true);
                equalsTest.run(CSVFormat.commaSeparated, CSVFormat.create().setCellSeparator('|'), false);
                equalsTest.run(CSVFormat.commaSeparated, CSVFormat.create().setQuote('\''), false);
            });

            runner.testGroup("equals(CSVFormat)", () ->
            {
                final Action3<CSVFormat,CSVFormat,Boolean> equalsTest = (CSVFormat lhs, CSVFormat rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(CSVFormat.commaSeparated, null, false);
                equalsTest.run(CSVFormat.commaSeparated, CSVFormat.commaSeparated, true);
                equalsTest.run(CSVFormat.commaSeparated, CSVFormat.create().setCellSeparator('|'), false);
                equalsTest.run(CSVFormat.commaSeparated, CSVFormat.create().setQuote('\''), false);
            });
        });
    }
}
