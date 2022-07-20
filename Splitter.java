// Splitter, a to-do list that organizes subtasks under main tasks and 
// indicates progress based on completion of subtasks.

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Splitter extends JFrame {
    private JButton btnAdd, btnDelete, btnEdit;
    private JList<String> taskList;
    private int selectedIndex;
    private DefaultListModel<String> taskListModel;
    private JPanel pnlButtons, pnlList;

    public Splitter() {
        // Create panel for buttons on top
        pnlButtons = new JPanel(new GridLayout(1, 3, 5, 1));

        // Create menu bar 
        JMenuBar menuBar = new JMenuBar();

        // Create file menu
        JMenu fileMenu = new JMenu("File");

        // Create 'save' and 'open' menu items
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem openItem = new JMenuItem("Open");

        // Add action listeners
        saveItem.addActionListener(new SaveItemListener());
        openItem.addActionListener(new OpenItemListener());

        // Add menuItems to menu
        fileMenu.add(saveItem);
        fileMenu.add(openItem);

        // add file menu to bar
        menuBar.add(fileMenu);

        // Set menu to frame
        setJMenuBar(menuBar);


        // Add buttons to panel
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(new BtnAddListener());
        pnlButtons.add(btnAdd);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new BtnDeleteListener());
        pnlButtons.add(btnDelete);
        
        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(new BtnEditListener());
        pnlButtons.add(btnEdit);

        // Create panel for list and add list to panel
        taskListModel = new DefaultListModel<>();

        taskList = new JList<>(taskListModel);
        selectedIndex = -1;
        taskList.addListSelectionListener(new TaskListListener());

        pnlList = new JPanel(new GridLayout());
        pnlList.add(taskList);

        // add panels to content pane
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(3, 3));
        cp.add(pnlButtons, BorderLayout.NORTH);
        cp.add(pnlList, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Splitter");
        setSize(800, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Splitter();
            }
        });
    }

    private class TaskListListener implements ListSelectionListener {
        // Handler for when a list item is clicked/selected
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // Called back twice when a list element is selected
            selectedIndex = taskList.getSelectedIndex();
        }
    }

    private class BtnAddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String taskName = (String)JOptionPane.showInputDialog(pnlButtons, "Enter the name of the task:", "Add task", JOptionPane.PLAIN_MESSAGE);
            taskListModel.addElement(taskName);
        }

    }

    private class BtnDeleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!taskListModel.isEmpty()) {
                if (selectedIndex != -1) { 
                    taskListModel.remove(selectedIndex);
                }
            }
        }
    }

    private class BtnEditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (!taskListModel.isEmpty() && selectedIndex != -1) {
                String taskName = (String)JOptionPane.showInputDialog(pnlButtons, "Enter the new name of the task:", "Edit task", JOptionPane.PLAIN_MESSAGE);
                if (!(taskName == null || taskName.isEmpty())) { // if taskName is not null or empty
                    taskListModel.setElementAt(taskName, selectedIndex); 
                }   
            }
        }

    }

    private class SaveItemListener implements ActionListener {
        // Listener for the 'Save' menu item
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create and open a file chooser for saving
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(Splitter.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    // Get the File object from file chooser and write to it
                    File file = fc.getSelectedFile();
                    FileWriter writer = new FileWriter(file);
                    
                    // Write each task list item on a new line, in plain text
                    for (int i = 0; i < taskListModel.getSize(); i++) {
                        writer.append(taskListModel.getElementAt(i) + "\n");
                    }
                    writer.close();
                }
                catch (IOException exception) {
                    System.out.println("An error occurred.");
                    exception.printStackTrace();
                }
                
            }
        }
        
    }

    private class OpenItemListener implements ActionListener {
        // Listener for the 'Open' menu item
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create and open a standard file chooser
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(Splitter.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    Scanner fileScan = new Scanner(file);
                    while (fileScan.hasNextLine()) {
                        taskListModel.addElement(fileScan.nextLine());
                    }
                }
                catch (Exception exception) {
                    System.out.println("An error occurred.");
                    exception.printStackTrace();
                }
            }
            // TODO: Might need a standard file format, as something unexpected
            // happen if a non-text file is opened.
        }
        
    }
}