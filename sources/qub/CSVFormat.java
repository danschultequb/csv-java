package qub;

public class CSVFormat
{
    public static final CSVFormat commaSeparated = CSVFormat.create();

    public static final CSVFormat tabSeparated = CSVFormat.create()
        .setCellSeparator('\t');

    private char cellSeparator;
    private char quote;

    private CSVFormat()
    {
        this.cellSeparator = ',';
        this.quote = '\"';
    }

    public static CSVFormat create()
    {
        return new CSVFormat();
    }

    public char getCellSeparator()
    {
        return this.cellSeparator;
    }

    public CSVFormat setCellSeparator(char cellSeparator)
    {
        this.cellSeparator = cellSeparator;
        return this;
    }

    public char getQuote()
    {
        return this.quote;
    }

    public CSVFormat setQuote(char quote)
    {
        this.quote = quote;
        return this;
    }

    @Override
    public String toString()
    {
        final CharacterList builder = CharacterList.create();
        builder.add('{');

        builder.addAll(Strings.quote("cellSeparator"));
        builder.add(':');
        builder.addAll(Strings.escapeAndQuote(this.cellSeparator));

        builder.add(',');

        builder.addAll(Strings.quote("quote"));
        builder.add(':');
        builder.addAll(Strings.escapeAndQuote(this.quote));

        builder.add('}');

        return builder.toString(true);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CSVFormat && this.equals((CSVFormat)rhs);
    }

    public boolean equals(CSVFormat rhs)
    {
        return rhs != null &&
            this.cellSeparator == rhs.cellSeparator &&
            this.quote == rhs.quote;
    }
}
