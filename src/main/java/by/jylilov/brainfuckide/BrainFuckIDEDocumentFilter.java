package by.jylilov.brainfuckide;

import javax.swing.text.*;
import java.awt.*;

public class BrainFuckIDEDocumentFilter extends DocumentFilter {

    private static final int DEFAULT_FONT_SIZE = 14;

    private final AttributeSet codeAttributes;
    private final AttributeSet bracketAttributes;
    private final AttributeSet commentAttributes;

    public BrainFuckIDEDocumentFilter() {
        codeAttributes = createCodeAttributes();
        bracketAttributes = createBracketAttributes();
        commentAttributes = createCommentAttributes();
    }

    private AttributeSet createCodeAttributes() {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributeSet, Font.MONOSPACED);
        StyleConstants.setFontSize(attributeSet, DEFAULT_FONT_SIZE);
        return attributeSet;
    }

    private AttributeSet createBracketAttributes() {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributeSet, Font.MONOSPACED);
        StyleConstants.setFontSize(attributeSet, DEFAULT_FONT_SIZE);
        StyleConstants.setForeground(attributeSet, Color.BLUE);
        StyleConstants.setBold(attributeSet, true);
        return attributeSet;
    }

    private AttributeSet createCommentAttributes() {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributeSet, Font.MONOSPACED);
        StyleConstants.setFontSize(attributeSet, DEFAULT_FONT_SIZE);
        StyleConstants.setItalic(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.GRAY);
        return attributeSet;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributeSet) throws BadLocationException {
        processText(fb, offset, text);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributeSet) throws BadLocationException {
        this.remove(fb, offset, length);
        processText(fb, offset, text);
    }

    private void processText(FilterBypass fb, int offset, String text) throws BadLocationException {
        for (int i = text.length() - 1; i >= 0; --i) {
            Character character = text.charAt(i);
            processCharacter(fb, offset, character);
        }
    }

    private void processCharacter(FilterBypass fb, int offset, Character character) throws BadLocationException {
        AttributeSet attributeSet;
        String text = character.equals('\t') ? "  " : character.toString();
        if ("+-><.,".contains(text)) {
            attributeSet = codeAttributes;
        } else if ("[]".contains(text)){
            attributeSet = bracketAttributes;
        } else {
            attributeSet = commentAttributes;
        }
        super.insertString(fb, offset, text, attributeSet);
    }
}