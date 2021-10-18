package com.codebind;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WordCounter {
    private JPanel panelMain;
    private JLabel typeTextHereLabel;
    private JTextArea typeTextHereTextArea;
    private JButton countWordsButton;
    private JLabel count;
    private JTextField totalCountOutput;
    private JButton clearButton;
    private JLabel Title;
    private JButton exitButton;

    public WordCounter() {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // status 0 means no active process "process finished with exit code 0" (exit)
                System.exit(0);
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sets text field to empty/blank -> ""
                typeTextHereTextArea.setText("");
                totalCountOutput.setText("");
            }
        });
        countWordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String words = typeTextHereTextArea.getText();
                int count = 1;
                for (int i = 0; i < words.length(); i++) {
                    if (words.charAt(i) == ' ') {
                        count++;
                    }
                }
                totalCountOutput.setText(" " + count);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("WordCounter");
        frame.setContentPane(new WordCounter().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
