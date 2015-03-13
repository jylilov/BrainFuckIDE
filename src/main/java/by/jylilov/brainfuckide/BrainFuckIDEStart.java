package by.jylilov.brainfuckide;

import javax.swing.*;

public class BrainFuckIDEStart {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    public static void main(String [] args) {
        BrainFuckIDEWindow window = new BrainFuckIDEWindow();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
