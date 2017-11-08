package com.Sortex.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CapturePane extends JPanel implements Consumer {

    private JTextArea output;

    public CapturePane() {
        setLayout(new BorderLayout());
        output = new JTextArea();
        output.setFont(new Font("Arial", Font.BOLD, 12));
        output.setForeground(new Color(7,163,145));
        add(new JScrollPane(output));
    }

    @Override
    public void appendText(final String text) {
        if (EventQueue.isDispatchThread()) {
            output.append(text);
            
            output.setCaretPosition(output.getText().length());
        } else {

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    appendText(text);
                }
            });

        }
    }      
    
    
}



 