package ceit.aut.ac.ir.gui;

import ceit.aut.ac.ir.model.Note;
import ceit.aut.ac.ir.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class CFrame extends JFrame implements ActionListener {

    private CMainPanel mainPanel;

    private JMenuItem newItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;


    public CFrame(String title) {
        super(title);

        setIconImage(new ImageIcon("icon.png").getImage());

        initMenuBar(); //create menuBar

        initMainPanel(); //create main panel
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu jmenu = new JMenu("File");

        newItem = new JMenuItem("New");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        newItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        jmenu.add(newItem);
        jmenu.add(saveItem);
        jmenu.add(exitItem);

        menuBar.add(jmenu);
        setJMenuBar(menuBar);
    }

    private void initMainPanel() {
        mainPanel = new CMainPanel();
        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newItem) {
            try
            {
                String answer = null;
                answer =  JOptionPane.showInputDialog(null,"Please Enter File Name:","Enter a Name",JOptionPane.QUESTION_MESSAGE);
                if (answer.isEmpty())
                {
                    answer = "unnamed";
                }
                mainPanel.addNewTab(answer);
            }
            catch (Exception ex)
            {
                //Do Nothing
            }
        } else if (e.getSource() == saveItem) {
            mainPanel.saveNote();
        } else if (e.getSource() == exitItem) {
            //TODO: Phase1: check all tabs saved ...
            saveAllTabs(mainPanel);

            System.exit(0);
        } else {
            System.out.println("Nothing detected...");
        }
    }

    public static void saveAllTabs(CMainPanel mainPanel)
    {
        int totalTabs = mainPanel.getTabbedPane().getTabCount();
        for (int i =0 ; i<totalTabs; i++)
        {
            JTextArea textPanel = (JTextArea) mainPanel.getTabbedPane().getComponentAt(i);
            String note = textPanel.getText();
            if (!note.isEmpty())
            {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                FileUtils.serializeData(new Note(mainPanel.getTabbedPane().getTitleAt(mainPanel.getTabbedPane().getSelectedIndex()) , note, Integer.toString(year)));
            }
            System.out.println(mainPanel.getTabbedPane().getTitleAt(i));
        }


    }
}