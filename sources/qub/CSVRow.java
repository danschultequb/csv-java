package qub;

/**
 * A row within a CSV document.
 */
public class CSVRow
{
    private final List<String> cells;

    private CSVRow(List<String> cells)
    {
        this.cells = cells;
    }

    public static CSVRow create(String... cells)
    {
        PreCondition.assertNotNull(cells, "cells");

        return new CSVRow(List.create(cells));
    }

    public static CSVRow create(Iterable<String> cells)
    {
        PreCondition.assertNotNull(cells, "cells");

        return new CSVRow(List.create(cells));
    }

    /**
     * Get the number of cells that exist in this row.
     * @return The number of cells that exist in this row.
     */
    public int getCellCount()
    {
        return this.cells.getCount();
    }

    /**
     * Get the cell in this row at the provided columnIndex, or null if the provided columnIndex is
     * greater than or equal to the number of cells in this row.
     * @param columnIndex The index of the column to get the cell from.
     * @return The cell in this row at the provided columnIndex, or null if the provided columnIndex
     * is greater than or equal to the number of cells in this row.
     */
    public String getCell(int columnIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(columnIndex, 0, "columnIndex");

        return columnIndex < this.getCellCount()
            ? this.cells.get(columnIndex)
            : null;
    }

    public CSVRow addCell(String cell)
    {
        PreCondition.assertNotNull(cell, "cell");

        this.cells.add(cell);

        return this;
    }

    public CSVRow addCells(String... cells)
    {
        PreCondition.assertNotNull(cells, "cells");

        for (final String cell : cells)
        {
            this.addCell(cell);
        }
        return this;
    }

    public CSVRow addCells(Iterable<String> cells)
    {
        PreCondition.assertNotNull(cells, "cells");

        for (final String cell : cells)
        {
            this.addCell(cell);
        }
        return this;
    }

    public Indexable<String> getCells()
    {
        return this.cells;
    }

    @Override
    public String toString()
    {
        return this.toString(CSVFormat.commaSeparated);
    }

    public String toString(CSVFormat format)
    {
        PreCondition.assertNotNull(format, "format");

        final InMemoryCharacterStream stream = InMemoryCharacterStream.create();
        this.toString(stream, format).await();
        return stream.getText().await();
    }

    public Result<Integer> toString(CharacterWriteStream writeStream, CSVFormat format)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotDisposed(writeStream, "writeStream");
        PreCondition.assertNotNull(format, "format");

        return Result.create(() ->
        {
            int result = 0;

            final char cellSeparator = format.getCellSeparator();
            final char quote = format.getQuote();
            boolean isFirstCell = true;
            for (final String cell : this.cells)
            {
                if (isFirstCell)
                {
                    isFirstCell = false;
                }
                else
                {
                    result += writeStream.write(cellSeparator).await();
                }

                boolean needsQuotes = false;
                boolean containsQuote = false;
                for (final char c : Strings.iterate(cell))
                {
                    if (c == cellSeparator || c == '\n')
                    {
                        needsQuotes = true;
                    }
                    if (c == quote)
                    {
                        containsQuote = true;
                    }

                    if (needsQuotes && containsQuote)
                    {
                        break;
                    }
                }

                if (!needsQuotes)
                {
                    if (!Strings.isNullOrEmpty(cell))
                    {
                        result += writeStream.write(cell).await();
                    }
                }
                else
                {
                    result += writeStream.write(quote).await();

                    for (final char c : Strings.iterate(cell))
                    {
                        if (c == quote)
                        {
                            result += writeStream.write('\\').await();
                        }
                        result += writeStream.write(c).await();
                    }

                    result += writeStream.write(quote).await();
                }
            }

            return result;
        });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CSVRow && this.equals((CSVRow)rhs);
    }

    public boolean equals(CSVRow rhs)
    {
        return rhs != null &&
            this.cells.equals(rhs.cells);
    }
}
