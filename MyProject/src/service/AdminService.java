/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import application.Main;
import facade.AdminFacade;
import gui.AdminGUI;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Milos
 */
public class AdminService {

    private AdminGUI adminGUI;
    private AdminFacade adminFacade;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public AdminService(AdminGUI adminGUI, Socket socket) {
        this.adminGUI = adminGUI;
        this.adminFacade = new AdminFacade();
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        fillStudentCombobox();
        allAdminTableModel();
        adminTableModel();
        instructorTableModel();
        studentTableModel();
        adminGUI.setVisible(true);
    }

    public void initAdmin() {
        adminGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave?\nYou will be logged out!", "Check!", JOptionPane.YES_NO_OPTION);
                if (choice == 1 || choice == -1) {
                    adminGUI.setDefaultCloseOperation(AdminGUI.DO_NOTHING_ON_CLOSE);
                    return;
                }
                CurrentAccount.getInstance(null).logoutAccount();
                close(socket, in, out);
                Main.go();
                adminGUI.dispose();
                adminGUI.setDefaultCloseOperation(AdminGUI.DO_NOTHING_ON_CLOSE);
            }
        });
        adminGUI.getAdd_Admin_Button_().addActionListener(e -> addAdmin());
        adminGUI.getAdd_Instructor_Button_().addActionListener(e -> addInstructor());
        adminGUI.getAdd_Student_Button_().addActionListener(e -> addStudent());
        adminGUI.getEdit_Admin_Button_().addActionListener(e -> editAdmin());
        adminGUI.getDelete_Admin_Button_().addActionListener(e -> deleteAdmin());
        adminGUI.getEdit_Instructor_Button_().addActionListener(e -> editInstructor());
        adminGUI.getDelete_Instructor_Button_().addActionListener(e -> deleteInstructor());
        adminGUI.getEdit_Student_Button_().addActionListener(e -> editStudent());
        adminGUI.getDelete_Student_Button_().addActionListener(e -> deleteStudent());
        adminGUI.getGod_Edit_Admin_Button_().addActionListener(e -> godAdminEdit());
        adminGUI.getGod_Delete_Admin_Button_().addActionListener(e -> godAdminDelete());
    }

    private void addAdmin() {
        String username = adminGUI.getAdmin_Username_Field_().getText();
        String password = adminGUI.getAdmin_Password_Field_().getText();
        String firstName = adminGUI.getAdmin_First_Name_Field_().getText();
        String lastName = adminGUI.getAdmin_Last_Name_Field_().getText();
        String email = adminGUI.getAdmin_Email_Field_().getText();
        String phoneNumer = adminGUI.getAdmin_Phone_Number_Field_().getText();

        if (adminFacade.adminCheck(username, password, firstName, lastName, email, phoneNumer)) {
            JOptionPane.showMessageDialog(null, "New admin was added!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            adminGUI.getAdmin_First_Name_Field_().setText(null);
            adminGUI.getAdmin_Last_Name_Field_().setText(null);
            adminGUI.getAdmin_Phone_Number_Field_().setText(null);
            adminGUI.getAdmin_Email_Field_().setText(null);
            adminGUI.getAdmin_Username_Field_().setText(null);
            adminGUI.getAdmin_Password_Field_().setText(null);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void addInstructor() {
        String email = adminGUI.getInstructor_Email_Field_().getText();
        String password = adminGUI.getInstructor_Password_Field_().getText();
        String firstName = adminGUI.getInstructor_First_Name_Field_().getText();
        String lastName = adminGUI.getInstructor_Last_Name_Field_().getText();
        String initials = adminGUI.getInstructor_Initials_Field_().getText();
        String phoneNumber = adminGUI.getInstructor_Phone_Number_Field_().getText();
        boolean male = adminGUI.getInstructor_Gender_Male_().isSelected();
        boolean female = adminGUI.getInstructor_Gender_Female_().isSelected();
        Date birthdate = adminGUI.getInstructor_Birthdate_DateChooser_().getDate();

        if (adminFacade.instructorCheck(email, password, firstName, lastName, initials, phoneNumber, male, female, birthdate)) {
            JOptionPane.showMessageDialog(null, "New instructor was added!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            adminGUI.getInstructor_Email_Field_().setText(null);
            adminGUI.getInstructor_Password_Field_().setText(null);
            adminGUI.getInstructor_First_Name_Field_().setText(null);
            adminGUI.getInstructor_Last_Name_Field_().setText(null);
            adminGUI.getInstructor_Initials_Field_().setText(null);
            adminGUI.getInstructor_Phone_Number_Field_().setText(null);
            adminGUI.getInstructor_Gender_ButtonGroup_().clearSelection();
            adminGUI.getInstructor_Birthdate_DateChooser_().setCalendar(null);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void addStudent() {
        String email = adminGUI.getStudent_Email_Field_().getText();
        String password = adminGUI.getStudent_Password_Field_().getText();
        String firstName = adminGUI.getStudent_First_Name_Field_().getText();
        String lastName = adminGUI.getStudent_Last_Name_Field_().getText();
        String city = adminGUI.getStudent_City_Field_().getText();
        String street = adminGUI.getStudent_Street_Field_().getText();
        String postalCode = adminGUI.getStudent_Postal_Code_Field_().getText();
        boolean male = adminGUI.getStudent_Gender_Male_().isSelected();
        boolean female = adminGUI.getStudent_Gender_Female_().isSelected();
        Date birthdate = adminGUI.getStudent_Birthdate_DateChooser_().getDate();
        int index = adminGUI.getStudent_Instructor_ComboBox_().getSelectedIndex();

        if (adminFacade.studentCheck(email, password, firstName, lastName, city, street, postalCode, male, female, birthdate, index)) {
            JOptionPane.showMessageDialog(null, "New student was added!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            adminGUI.getStudent_Email_Field_().setText(null);
            adminGUI.getStudent_Password_Field_().setText(null);
            adminGUI.getStudent_First_Name_Field_().setText(null);
            adminGUI.getStudent_Last_Name_Field_().setText(null);
            adminGUI.getStudent_City_Field_().setText(null);
            adminGUI.getStudent_Street_Field_().setText(null);
            adminGUI.getStudent_Postal_Code_Field_().setText(null);
            adminGUI.getStudent_Gender_ButtonGroup_().clearSelection();
            adminGUI.getStudent_Birthdate_DateChooser_().setCalendar(null);
            adminGUI.getStudent_Instructor_ComboBox_().setSelectedIndex(0);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void fillStudentCombobox() {
        new SwingWorker<DefaultComboBoxModel, DefaultComboBoxModel>() {
            @Override
            protected DefaultComboBoxModel doInBackground() throws Exception {
                return adminFacade.setStudentComboboxModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultComboBoxModel model = get();
                    adminGUI.getStudent_Instructor_ComboBox_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void allAdminTableModel() {
        new SwingWorker<DefaultTableModel, DefaultTableModel>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return adminFacade.setAllAdminTableModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel model = get();
                    adminGUI.getGod_Admin_Table_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void adminTableModel() {
        new SwingWorker<DefaultTableModel, DefaultTableModel>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return adminFacade.setAdminTableModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel model = get();
                    adminGUI.getAdmin_Table_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void instructorTableModel() {
        new SwingWorker<DefaultTableModel, DefaultTableModel>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return adminFacade.setInstructorTableModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel model = get();
                    adminGUI.getInstructor_Table_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void studentTableModel() {
        new SwingWorker<DefaultTableModel, DefaultTableModel>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return adminFacade.setStudentTableModel();
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel model = get();
                    adminGUI.getStudent_Table_().setModel(model);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }.execute();
    }

    private void editAdmin() {
        if (adminGUI.getAdmin_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getAdmin_Table_().getSelectedRow();
        int id = Integer.parseInt(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 1).toString());
        String username = adminGUI.getAdmin_Table_().getModel().getValueAt(row, 2).toString();
        String password = adminGUI.getAdmin_Table_().getModel().getValueAt(row, 3).toString();
        String firstName = adminGUI.getAdmin_Table_().getModel().getValueAt(row, 4).toString();
        String lastName = adminGUI.getAdmin_Table_().getModel().getValueAt(row, 5).toString();
        String email = adminGUI.getAdmin_Table_().getModel().getValueAt(row, 6).toString();
        String phoneNumber = adminGUI.getAdmin_Table_().getModel().getValueAt(row, 7).toString();
        if (adminFacade.editAdminCheck(id, username, password, firstName, lastName, email, phoneNumber)) {
            JOptionPane.showMessageDialog(null, "You have edited this admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void deleteAdmin() {
        if (adminGUI.getAdmin_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getAdmin_Table_().getSelectedRow();
        int id = Integer.parseInt(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 1).toString());
        if (adminFacade.deleteAdminCheck(id)) {
            JOptionPane.showMessageDialog(null, "You have deleted this admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void editInstructor() {
        if (adminGUI.getInstructor_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getInstructor_Table_().getSelectedRow();
        int id = Integer.parseInt(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 1).toString());
        String email = adminGUI.getInstructor_Table_().getModel().getValueAt(row, 2).toString();
        String password = adminGUI.getInstructor_Table_().getModel().getValueAt(row, 3).toString();
        String firstName = adminGUI.getInstructor_Table_().getModel().getValueAt(row, 4).toString();
        String lastName = adminGUI.getInstructor_Table_().getModel().getValueAt(row, 5).toString();
        String initials = adminGUI.getInstructor_Table_().getModel().getValueAt(row, 6).toString();
        String phoneNumber = adminGUI.getInstructor_Table_().getModel().getValueAt(row, 7).toString();
        if (adminFacade.editInstructorCheck(id, email, password, firstName, lastName, initials, phoneNumber)) {
            JOptionPane.showMessageDialog(null, "You have edited this instructor!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void deleteInstructor() {
        if (adminGUI.getInstructor_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getInstructor_Table_().getSelectedRow();
        int id = Integer.parseInt(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 1).toString());
        if (adminFacade.deleteInstructorCheck(id)) {
            JOptionPane.showMessageDialog(null, "You have deleted this instructor!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void editStudent() {
        if (adminGUI.getStudent_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getStudent_Table_().getSelectedRow();
        int id = Integer.parseInt(adminGUI.getStudent_Table_().getModel().getValueAt(row, 1).toString());
        String email = adminGUI.getStudent_Table_().getModel().getValueAt(row, 2).toString();
        String password = adminGUI.getStudent_Table_().getModel().getValueAt(row, 3).toString();
        String firstName = adminGUI.getStudent_Table_().getModel().getValueAt(row, 4).toString();
        String lastName = adminGUI.getStudent_Table_().getModel().getValueAt(row, 5).toString();
        String city = adminGUI.getStudent_Table_().getModel().getValueAt(row, 6).toString();
        String street = adminGUI.getStudent_Table_().getModel().getValueAt(row, 7).toString();
        String postalCode = adminGUI.getStudent_Table_().getModel().getValueAt(row, 8).toString();
        String instructorId = adminGUI.getStudent_Table_().getModel().getValueAt(row, 9).toString();
        if (adminFacade.editStudentCheck(id, email, password, firstName, lastName, city, street, postalCode, instructorId)) {
            JOptionPane.showMessageDialog(null, "You have edited this student!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void deleteStudent() {
        if (adminGUI.getStudent_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getStudent_Table_().getSelectedRow();
        int id = Integer.parseInt(adminGUI.getStudent_Table_().getModel().getValueAt(row, 1).toString());
        if (adminFacade.deleteStudentCheck(id)) {
            JOptionPane.showMessageDialog(null, "You have deleted this student!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void godAdminEdit() {
        if (adminGUI.getGod_Admin_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getGod_Admin_Table_().getSelectedRow();
        int adminId = Integer.parseInt(adminGUI.getGod_Admin_Table_().getModel().getValueAt(row, 1).toString());
        String newId = adminGUI.getGod_Admin_Table_().getModel().getValueAt(row, 4).toString();
        if (adminFacade.godAdminEditCheck(adminId, newId)) {
            JOptionPane.showMessageDialog(null, "You have edited this admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private void godAdminDelete() {
        if (adminGUI.getGod_Admin_Table_().getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = adminGUI.getGod_Admin_Table_().getSelectedRow();
        int adminId = Integer.parseInt(adminGUI.getGod_Admin_Table_().getModel().getValueAt(row, 1).toString());
        if (adminFacade.godAdminDeleteCheck(adminId)) {
            JOptionPane.showMessageDialog(null, "You have deleted this admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            try {
                out.writeUTF("Change in database!");
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    public void listenForChanges() {
        new Thread(
                () -> {
                    String change;
                    while (socket.isConnected()) {
                        try {
                            change = in.readUTF();
                            if (change.equals("Change in database!")) {
                                fillStudentCombobox();
                                allAdminTableModel();
                                adminTableModel();
                                instructorTableModel();
                                studentTableModel();
                            }
                        } catch (IOException ex) {
                            close(socket, in, out);
                        }
                    }
                }, "listenThread"
        ).start();
    }

    private void close(Socket socket, DataInputStream in, DataOutputStream out) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
