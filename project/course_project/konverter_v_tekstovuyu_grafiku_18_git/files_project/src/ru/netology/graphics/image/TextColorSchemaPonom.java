package ru.netology.graphics.image;

public class TextColorSchemaPonom implements TextColorSchema{
    @Override
    public char convert(int color) {
        /*
        Стандартные символы 8 шт:

        '#', '$', '@', '%', '*', '+', '-', '''
        '0', '1', '2', '3', '4', '5', '6', '7'

        */
        if (color < 0 || color > 255) {
            throw new IllegalArgumentException(
                    "Допустимые значения для color от 0 до 255. Переданный color " + color
            );
        }

        char[] stanChar = {'#', '$', '@', '%', '*', '+', '-', '\''};

        return stanChar[(int) Math.floor(color / 256. * stanChar.length)];
    }
}
