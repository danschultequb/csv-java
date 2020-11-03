package qub;

/**
 * A collection of functions for parsing CSV documents.
 */
public interface CSV
{
    static Result<CSVDocument> parse(File file)
    {
        PreCondition.assertNotNull(file, "file");

        return Result.createUsing(
            () -> ByteReadStream.buffer(file.getContentsReadStream().await()),
            (ByteReadStream byteReadStream) -> CSV.parse(byteReadStream).await());
    }

    static Result<CSVDocument> parse(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotDisposed(byteReadStream, "byteReadStream");

        return CSV.parse((CharacterReadStream)CharacterReadStream.create(byteReadStream));
    }

    static Result<CSVDocument> parse(CharacterReadStream characterReadStream)
    {
        PreCondition.assertNotNull(characterReadStream, "characterReadStream");

        return CSV.parse(CharacterReadStream.iterate(characterReadStream));
    }

    static Result<CSVDocument> parse(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return CSV.parse(Strings.iterable(text));
    }

    static Result<CSVDocument> parse(Iterable<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return CSV.parse(characters.iterate());
    }

    static Result<CSVDocument> parse(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return Result.create(() ->
        {
            characters.start();

            final CSVDocument result = CSVDocument.create();

            CSVRow currentRow = CSVRow.create();
            final CharacterList currentCell = CharacterList.create();
            boolean currentCellIsQuoted = false;
            boolean previousCharacterWasCarriageReturn = false;
            while (characters.hasCurrent())
            {
                final char currentCharacter = characters.takeCurrent();
                if (currentCharacter == '\"')
                {
                    if (!currentCell.any())
                    {
                        currentCellIsQuoted = true;
                    }
                    else
                    {
                        currentCellIsQuoted = false;
                    }
                    previousCharacterWasCarriageReturn = false;
                }
                else if (currentCharacter == '\r')
                {
                    if (currentCellIsQuoted || previousCharacterWasCarriageReturn)
                    {
                        currentCell.add(currentCharacter);
                    }
                    previousCharacterWasCarriageReturn = true;
                }
                else if (currentCharacter == '\n')
                {
                    if (currentCellIsQuoted)
                    {
                        currentCell.add(currentCharacter);
                    }
                    else
                    {
                        if (currentCell.any() || currentRow.getCellCount() > 0)
                        {
                            currentRow.addCell(currentCell.toString(true));
                            currentCell.clear();
                        }
                        result.addRow(currentRow);
                        currentRow = CSVRow.create();
                    }
                    previousCharacterWasCarriageReturn = false;
                }
                else if (currentCharacter == ',')
                {
                    if (currentCellIsQuoted)
                    {
                        currentCell.add(currentCharacter);
                    }
                    else
                    {
                        currentRow.addCell(currentCell.toString(true));
                        currentCell.clear();
                    }
                    previousCharacterWasCarriageReturn = false;
                }
                else
                {
                    currentCell.add(currentCharacter);
                    previousCharacterWasCarriageReturn = false;
                }
            }

            if (currentCellIsQuoted)
            {
                throw new ParseException("Missing closing double-quote ('\"').");
            }

            if (previousCharacterWasCarriageReturn)
            {
                currentCell.add('\r');
            }

            if (currentCell.any() || currentRow.getCellCount() > 0)
            {
                currentRow.addCell(currentCell.toString(true));
                currentCell.clear();
            }

            if (currentRow.getCellCount() > 0)
            {
                result.addRow(currentRow);
                currentRow = null;
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }
}
