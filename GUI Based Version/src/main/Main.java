package main;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch the GUI application
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}