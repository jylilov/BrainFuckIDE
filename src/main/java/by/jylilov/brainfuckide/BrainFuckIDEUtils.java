package by.jylilov.brainfuckide;

public class BrainFuckIDEUtils {
    public static String getCharacterInfo(char c) {
        return (int)c + " (" + getCharacterString(c) + ")";
    }

    public static String getCharacterString(char c) {
        return "\"" + c + "\"";
    }

    private BrainFuckIDEUtils() {}
}
