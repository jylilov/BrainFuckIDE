package by.jylilov.brainfuckide;

import javax.swing.*;

public class Start {
    public static void main(String [] args) {
        JFrame frame = new JFrame("BrainFuck IDE");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
