package ceit.aut.ac.ir.gui;

import ceit.aut.ac.ir.model.Note;
import ceit.aut.ac.ir.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CMainPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private JList<File> directoryList;

    public CMainPanel() {

        setLayout(new BorderLayout());

        initDirectoryList(); // add JList to main Panel

        initTabbedPane(); // add TabbedPane to main panel

        addNewTab("New Tab"); // open new empty tab when user open the application
    }

    private void initTabbedPane() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initDirectoryList()
    {
        File[] files = FileUtils.getFilesInDirectory();
        directoryList = new JList<>(files);

        directoryList.setBackground(new Color(211, 211, 211));
        directoryList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        directoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        directoryList.setVisibleRowCount(-1);
        directoryList.setMinimumSize(new Dimension(130, 100));
        directoryList.setMaximumSize(new Dimension(130, 100));
        directoryList.setFixedCellWidth(130);
        directoryList.setCellRenderer(new MyCellRenderer());
        directoryList.addMouseListener(new MyMouseAdapter());

        add(new JScrollPane(directoryList), BorderLayout.WEST);
    }

    public void addNewTab(String answer) {
        JTextArea textPanel = createTextPanel();
        textPanel.setText("Write Something here...");
        tabbedPane.addTab(answer, textPanel);
    }

    public JList<File> getDirectoryList() {
        return directoryList;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public void setDirectoryList(JList<File> directoryList) {
        this.directoryList = directoryList;
    }

    public void openExistingNote(String title, String content)
    {
        JTextArea existPanel = createTextPanel();
        int tabIndex = tabbedPane.getTabCount() + 1;
        tabbedPane.addTab( title+" ", existPanel);
        tabbedPane.setSelectedIndex(tabIndex - 1);
        existPanel.setText(content);
    }

    public void saveNote() {
        JTextArea textPanel = (JTextArea) tabbedPane.getSelectedComponent();
        String note = textPanel.getText();
        if (!note.isEmpty())
        {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            FileUtils.serializeData(new Note(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) , note, Integer.toString(year)));
        }
        updateListGUI();
    }

    private JTextArea createTextPanel()
    {
        JTextArea textPanel = new JTextArea();
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return textPanel;
    }

    private void updateListGUI() {
        File[] newFiles = FileUtils.getFilesInDirectory();
        directoryList.setListData(newFiles);
    }


    private class MyMouseAdapter extends MouseAdapter {
        private final ArrayList<Integer> clickedIndexes = new ArrayList<>();

        @Override
        public void mouseClicked(MouseEvent eve) {
            // Double-click detected

            if (eve.getClickCount() == 2) {
                int index = directoryList.locationToIndex(eve.getPoint());
                boolean checkExist = false;
                for (Integer inde: clickedIndexes)
                {
                    if (inde == index)
                    {
                        checkExist = true;
                    }
                }

                if (!checkExist)
                {
                    //TODO: Phase1: Click on file is handled... Just load content into JTextArea
                    clickedIndexes.add(index);
                    File curr[] = FileUtils.getFilesInDirectory();
                    Note content = FileUtils.deserializeData(curr[index]);
                    openExistingNote(content.getTitle(),content.getContent());
                }
            }
        }
    }


    private class MyCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {
            if (object instanceof File) {
                File file = (File) object;
                Note read = FileUtils.deserializeData(file);

                setText(read.getTitle()+"("+read.getDate()+")");
//                setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                setEnabled(list.isEnabled());
            }
            return this;
        }
    }
}

