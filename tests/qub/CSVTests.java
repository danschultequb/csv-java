package qub;

public interface CSVTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CSV.class, () ->
        {
            runner.testGroup("parse(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CSV.parse((File)null),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.getFile("/file.csv").await();

                    test.assertThrows(() -> CSV.parse(file).await(),
                        new FileNotFoundException("/file.csv"));
                });

                runner.test("with empty file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.createFile("/file.csv").await();

                    test.assertEqual(
                        CSVDocument.create(),
                        CSV.parse(file).await());
                });

                runner.test("with non-empty file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.createFile("/file.csv").await();
                    file.setContentsAsString("a,b,c\nd,e,f\ng,h,i\n").await();

                    test.assertEqual(
                        CSVDocument.create(
                            CSVRow.create("a", "b", "c"),
                            CSVRow.create("d", "e", "f"),
                            CSVRow.create("g", "h", "i")),
                        CSV.parse(file).await());
                });
            });

            runner.testGroup("parse(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CSV.parse((ByteReadStream)null),
                        new PreConditionFailure("byteReadStream cannot be null."));
                });
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> CSV.parse(text).await(), expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseErrorTest.run("\"hello", new ParseException("Missing closing double-quote ('\"')."));

                final Action2<String,CSVDocument> parseTest = (String text, CSVDocument expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final CSVDocument document = CSV.parse(text).await();
                        test.assertEqual(expected, document);
                    });
                };

                parseTest.run(
                    "",
                    CSVDocument.create());
                parseTest.run(
                    "   ",
                    CSVDocument.create(
                        CSVRow.create("   ")));
                parseTest.run(
                    "\n",
                    CSVDocument.create(
                        CSVRow.create()));
                parseTest.run(
                    "\r",
                    CSVDocument.create(
                        CSVRow.create("\r")));
                parseTest.run(
                    "\r\n",
                    CSVDocument.create(
                        CSVRow.create()));
                parseTest.run(
                    "a",
                    CSVDocument.create(
                        CSVRow.create("a")));
                parseTest.run(
                    "a,",
                    CSVDocument.create(
                        CSVRow.create("a", "")));
                parseTest.run(
                    "a,b",
                    CSVDocument.create(
                        CSVRow.create("a", "b")));
                parseTest.run(
                    " a , b ",
                    CSVDocument.create(
                        CSVRow.create(" a ", " b ")));
                parseTest.run(
                    "a,b\n",
                    CSVDocument.create(
                        CSVRow.create("a", "b")));
                parseTest.run(
                    "a,b\nc",
                    CSVDocument.create(
                        CSVRow.create("a", "b"),
                        CSVRow.create("c")));
                parseTest.run(
                    "a,b,\nc\nd e",
                    CSVDocument.create(
                        CSVRow.create("a", "b", ""),
                        CSVRow.create("c"),
                        CSVRow.create("d e")));
                parseTest.run(
                    "a,b,\r\nc\nd e",
                    CSVDocument.create(
                        CSVRow.create("a", "b", ""),
                        CSVRow.create("c"),
                        CSVRow.create("d e")));
                parseTest.run(
                    "\"a\"",
                    CSVDocument.create(
                        CSVRow.create("a")));
                parseTest.run(
                    "\"a,b\"",
                    CSVDocument.create(
                        CSVRow.create("a,b")));
                parseTest.run(
                    "\"a\nb,\"",
                    CSVDocument.create(
                        CSVRow.create("a\nb,")));
                parseTest.run(
                    "\"a'b\"",
                    CSVDocument.create(
                        CSVRow.create("a'b")));
                parseTest.run(
                    "'a'",
                    CSVDocument.create(
                        CSVRow.create("'a'")));
                parseTest.run(
                    "'a,b'",
                    CSVDocument.create(
                        CSVRow.create("'a", "b'")));
            });
        });
    }
}
