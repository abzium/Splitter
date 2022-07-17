// Splitter, a to-do list that organizes subtasks under main tasks and 
// indicates progress based on completion of subtasks.

import java.awt.*;
import java.awt.event.*;
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

        // Create 'save' and 'load' menu items
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");

        // Add action listeners
        saveItem.addActionListener(new SaveItemListener());
        loadItem.addActionListener(new LoadItemListener());

        // Add menuItems to menu
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

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

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Saving...");
            // Open file chooser for saving
            //Create a file chooser
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(Splitter.this);
        }
        
    }

    private class LoadItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Loading...");
            
        }
        
    }
}