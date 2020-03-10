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

        CSVFormat.addProperty(builder, "cellSeparator", Characters.toString(this.cellSeparator));
        CSVFormat.addProperty(builder, "quote", Characters.toString(this.quote));

        builder.add('}');

        return builder.toString(true);
    }

    private static void addProperty(CharacterList list, String propertyName, String propertyValue)
    {
        PreCondition.assertNotNull(list, "list");
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValue, "propertyValue");

        if (!Strings.isNullOrEmpty(propertyValue))
        {
            if (!list.endsWith('{'))
            {
                list.add(',');
            }
            list.addAll(Strings.escapeAndQuote(propertyName));
            list.add(':');
            list.addAll(Objects.toString(Strings.escapeAndQuote(propertyValue)));
        }
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
