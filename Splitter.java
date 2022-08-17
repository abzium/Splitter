// Splitter, a to-do list that organizes subtasks under main tasks and 
// indicates progress based on completion of subtasks.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class Splitter extends JFrame {
    // Menu components
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem saveItem, openItem;

    // Buttons
    private JButton btnAdd, btnDelete;

    // List variables
    private DefaultListModel<String> taskListModel;

    // Tree variables
    private JTree taskTree;
    //private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode top;
    private DefaultMutableTreeNode selectedNode = null;

    // Main panels
    private JPanel pnlButtons;

    public Splitter() {
        // Create file menu
        menuBar = createMenu();
        setJMenuBar(menuBar);

        createButtons();

        top = new DefaultMutableTreeNode("Task List");
        //treeModel = new DefaultTreeModel(top);
        taskTree = new JTree(top);
        taskTree.setEditable(true);
        taskTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        taskTree.addTreeSelectionListener(new TaskTreeListener());

        // add panels to content pane
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(3, 3));
        cp.add(pnlButtons, BorderLayout.NORTH);
        cp.add(taskTree, BorderLayout.CENTER);

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

    private class TaskTreeListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            selectedNode = (DefaultMutableTreeNode)
                    taskTree.getLastSelectedPathComponent();
            System.out.println(selectedNode);
        }
        
    }

    private class BtnAddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String taskName = (String)JOptionPane.showInputDialog(pnlButtons, "Enter the name of the task:", "Add task", JOptionPane.PLAIN_MESSAGE);
            DefaultMutableTreeNode newTask = new DefaultMutableTreeNode(taskName);
            
            DefaultMutableTreeNode root = selectedNode;
            DefaultTreeModel model = (DefaultTreeModel)taskTree.getModel();

            model.insertNodeInto(newTask, root, root.getChildCount());
            taskTree.scrollPathToVisible(new TreePath(newTask.getPath()));
        }

    }

    private class BtnDeleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTreeModel model = (DefaultTreeModel)taskTree.getModel();
            // If the root node of the tree is a leaf, the tree is empty
            if (!model.isLeaf(top)) {
                if (!(selectedNode == null)) {
                    model.removeNodeFromParent(selectedNode);
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
                    FileOutputStream fileOut = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(taskTree);
                    out.close();
                    fileOut.close();
                    System.out.println("Saved data to " + file);
                    //FileWriter writer = new FileWriter(file);
                    
                    // Write each task list item on a new line, in plain text
                    //for (int i = 0; i < taskListModel.getSize(); i++) {
                    //    writer.append(taskListModel.getElementAt(i) + "\n");
                    //}
                    //writer.close();
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
                    taskListModel.clear();
                    while (fileScan.hasNextLine()) {
                        // line by line, add each line to the list
                        taskListModel.addElement(fileScan.nextLine());
                    }
                    fileScan.close();
                }
                catch (Exception exception) {
                    System.out.println("An error occurred.");
                    exception.printStackTrace();
                }
            }
            
        }
        
    }

    private JMenuBar createMenu() {
        // Create menu bar 
        JMenuBar menu = new JMenuBar();

        fileMenu = new JMenu("File");

        saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new SaveItemListener());
        fileMenu.add(saveItem);

        openItem = new JMenuItem("Open");
        openItem.addActionListener(new OpenItemListener());
        fileMenu.add(openItem);

        menu.add(fileMenu);

        return menu;
    }

    private void createButtons() {
        // Create button panel
        pnlButtons = new JPanel(new GridLayout(1, 3, 5, 1));

        // Create and add buttons to panel
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(new BtnAddListener());
        pnlButtons.add(btnAdd);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new BtnDeleteListener());
        pnlButtons.add(btnDelete);

    }

}