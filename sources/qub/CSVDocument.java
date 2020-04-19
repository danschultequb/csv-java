package qub;

public class CSVDocument
{
    private final List<CSVRow> rows;

    private CSVDocument()
    {
        this.rows = List.create();
    }

    public static CSVDocument create(CSVRow... rows)
    {
        PreCondition.assertNotNull(rows, "rows");

        return new CSVDocument()
            .addRows(rows);
    }

    public static CSVDocument create(Iterable<CSVRow> rows)
    {
        PreCondition.assertNotNull(rows, "rows");

        return new CSVDocument()
            .addRows(rows);
    }

    public int getRowCount()
    {
        return this.rows.getCount();
    }

    public Indexable<CSVRow> getRows()
    {
        return this.rows;
    }

    public CSVRow getRow(int rowIndex)
    {
        PreCondition.assertIndexAccess(rowIndex, this.getRowCount(), "rowIndex");

        return this.rows.get(rowIndex);
    }

    public CSVDocument addRow(CSVRow row)
    {
        PreCondition.assertNotNull(row, "row");

        this.rows.add(row);

        return this;
    }

    public CSVDocument addRows(CSVRow... rows)
    {
        PreCondition.assertNotNull(rows, "rows");

        for (final CSVRow row : rows)
        {
            this.addRow(row);
        }

        return this;
    }

    public CSVDocument addRows(Iterable<CSVRow> rows)
    {
        PreCondition.assertNotNull(rows, "rows");

        for (final CSVRow row : rows)
        {
            this.addRow(row);
        }

        return this;
    }

    @Override
    public String toString()
    {
        return this.toString(CSVFormat.commaSeparated);
    }

    public String toString(CSVFormat format)
    {
        PreCondition.assertNotNull(format, "format");

        final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();
        this.toString(writeStream, format).await();
        return writeStream.getText().await();
    }

    public Result<Integer> toString(CharacterWriteStream writeStream, CSVFormat format)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotDisposed(writeStream, "writeStream");
        PreCondition.assertNotNull(format, "format");

        return Result.create(() ->
        {
            int result = 0;

            for (final CSVRow row : this.rows)
            {
                result += row.toString(writeStream, format).await();
                result += writeStream.writeLine().await();
            }

            return result;
        });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CSVDocument && this.equals((CSVDocument)rhs);
    }

    public boolean equals(CSVDocument rhs)
    {
        return rhs != null &&
            this.rows.equals(rhs.rows);
    }
}
