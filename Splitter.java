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

    public Splitter() {
        // Create panel for buttons on top
        JPanel pnlButtons = new JPanel(new GridLayout(1, 3, 5, 1));

        // Add buttons to panel
        btnAdd = new JButton("Add");
        pnlButtons.add(btnAdd);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new BtnDeleteListener());
        pnlButtons.add(btnDelete);
        
        btnEdit = new JButton("Edit");
        pnlButtons.add(btnEdit);

        // Create panel for list and add list to panel
        JPanel pnlList = new JPanel(new GridLayout());
        String[] data = {"one", "two", "three", "four"};
        taskList = new JList<>(data);
        taskList.addListSelectionListener(new TaskListListener());
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
            System.out.println(selectedIndex); //DEBUG
        }
    }

    private class BtnDeleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Delete " + selectedIndex + "!"); // DEBUG
            
        }
        
    }
}