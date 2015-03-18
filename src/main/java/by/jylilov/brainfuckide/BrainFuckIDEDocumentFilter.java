package by.jylilov.brainfuckide;

import javax.swing.text.*;
import java.awt.*;

public class BrainFuckIDEDocumentFilter extends DocumentFilter {

    private static final int DEFAULT_FONT_SIZE = 14;
    
    private int highlightCharacter = -1;

    private final MutableAttributeSet codeAttributes;
    private final MutableAttributeSet bracketAttributes;
    private final MutableAttributeSet commentAttributes;
    private final MutableAttributeSet highlightAttributes;

    public BrainFuckIDEDocumentFilter() {
        codeAttributes = createCodeAttributes();
        bracketAttributes = createBracketAttributes();
        commentAttributes = createCommentAttributes();
        highlightAttributes = createHighlightCharacterAttributes();
    }

    public void setHighlightCharacter(int highlightCharacter) {
        this.highlightCharacter = highlightCharacter;
    }

    private MutableAttributeSet createHighlightCharacterAttributes() {
        MutableAttributeSet MutableAttributeSet = createCodeAttributes();
        StyleConstants.setBackground(MutableAttributeSet, Color.RED);        
        return MutableAttributeSet;
    }

    private MutableAttributeSet createCodeAttributes() {
        SimpleAttributeSet MutableAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(MutableAttributeSet, Font.MONOSPACED);
        StyleConstants.setFontSize(MutableAttributeSet, DEFAULT_FONT_SIZE);
        return MutableAttributeSet;
    }

    private MutableAttributeSet createBracketAttributes() {
        SimpleAttributeSet MutableAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(MutableAttributeSet, Font.MONOSPACED);
        StyleConstants.setFontSize(MutableAttributeSet, DEFAULT_FONT_SIZE);
        StyleConstants.setForeground(MutableAttributeSet, Color.BLUE);
        StyleConstants.setBold(MutableAttributeSet, true);
        return MutableAttributeSet;
    }

    private MutableAttributeSet createCommentAttributes() {
        SimpleAttributeSet MutableAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(MutableAttributeSet, Font.MONOSPACED);
        StyleConstants.setFontSize(MutableAttributeSet, DEFAULT_FONT_SIZE);
        StyleConstants.setItalic(MutableAttributeSet, true);
        StyleConstants.setForeground(MutableAttributeSet, Color.GRAY);
        return MutableAttributeSet;
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
        MutableAttributeSet MutableAttributeSet;
        String text = character.equals('\t') ? "  " : character.toString();
        if ("+-><.,".contains(text)) {
            MutableAttributeSet = codeAttributes;
        } else if ("[]".contains(text)){
            MutableAttributeSet = bracketAttributes;
        } else if (offset == highlightCharacter) {
            MutableAttributeSet = createHighlightCharacterAttributes();
        } else {
            MutableAttributeSet = commentAttributes;
        }

        super.insertString(fb, offset, text, MutableAttributeSet);
    }
}