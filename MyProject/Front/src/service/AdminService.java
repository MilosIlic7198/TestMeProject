/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import application.Main;
import dto.Account;
import facade.ValidatorFacade;
import gui.AdminGUI;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Milos
 */
public class AdminService {

    private AdminGUI adminGUI;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ValidatorFacade validator;
    private ArrayList<Account> studentComboboxList;

    public AdminService(AdminGUI adminGUI, Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.adminGUI = adminGUI;
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.validator = new ValidatorFacade();
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
                close(socket, in, out);
                Main.go();
                adminGUI.dispose();
                adminGUI.setDefaultCloseOperation(AdminGUI.DO_NOTHING_ON_CLOSE);
            }
        });
        adminGUI.getAdd_Admin_Button_().addActionListener(e -> addAdminCheck());
        adminGUI.getAdd_Instructor_Button_().addActionListener(e -> addInstructorCheck());
        adminGUI.getAdd_Student_Button_().addActionListener(e -> addStudentCheck());
        adminGUI.getEdit_Admin_Button_().addActionListener(e -> editAdmin());
        adminGUI.getDelete_Admin_Button_().addActionListener(e -> deleteAdmin());
        adminGUI.getEdit_Instructor_Button_().addActionListener(e -> editInstructor());
        adminGUI.getDelete_Instructor_Button_().addActionListener(e -> deleteInstructor());
        adminGUI.getEdit_Student_Button_().addActionListener(e -> editStudent());
        adminGUI.getDelete_Student_Button_().addActionListener(e -> deleteStudent());
        adminGUI.getGod_Edit_Admin_Button_().addActionListener(e -> godAdminEdit());
        adminGUI.getGod_Delete_Admin_Button_().addActionListener(e -> godAdminDelete());
    }

    private void addAdminCheck() {
        try {
            if (adminGUI.getAdmin_First_Name_Field_().getText().trim().isEmpty()
                    || adminGUI.getAdmin_Last_Name_Field_().getText().trim().isEmpty()
                    || adminGUI.getAdmin_Phone_Number_Field_().getText().trim().isEmpty()
                    || adminGUI.getAdmin_Email_Field_().getText().trim().isEmpty()
                    || adminGUI.getAdmin_Username_Field_().getText().trim().isEmpty()
                    || adminGUI.getAdmin_Password_Field_().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (validator.adminValidation(adminGUI.getAdmin_Username_Field_().getText(), adminGUI.getAdmin_Password_Field_().getText(), adminGUI.getAdmin_Email_Field_().getText(), adminGUI.getAdmin_Phone_Number_Field_().getText())) {
                out.writeObject("Check username!");
                out.writeObject(adminGUI.getAdmin_Username_Field_().getText());
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while checking username!");
            ex.printStackTrace();
        }
    }

    private void checkUsername(boolean check) {
        try {
            if (!check) {
                out.writeObject("Add admin!");
                out.writeObject(adminGUI.getAdmin_Username_Field_().getText());
                out.writeObject(adminGUI.getAdmin_Password_Field_().getText());
                out.writeObject(adminGUI.getAdmin_First_Name_Field_().getText());
                out.writeObject(adminGUI.getAdmin_Last_Name_Field_().getText());
                out.writeObject(adminGUI.getAdmin_Email_Field_().getText());
                out.writeObject(adminGUI.getAdmin_Phone_Number_Field_().getText());
            } else {
                JOptionPane.showMessageDialog(null, "Account with this username already exists!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while adding admin account!");
            ex.printStackTrace();
        }
    }

    private void finishAddingAdmin(boolean check) {
        try {
            if (check) {
                adminGUI.getAdmin_First_Name_Field_().setText(null);
                adminGUI.getAdmin_Last_Name_Field_().setText(null);
                adminGUI.getAdmin_Phone_Number_Field_().setText(null);
                adminGUI.getAdmin_Email_Field_().setText(null);
                adminGUI.getAdmin_Username_Field_().setText(null);
                adminGUI.getAdmin_Password_Field_().setText(null);
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "New admin was added!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while finishing adding admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing admin account!");
            ex.printStackTrace();
        }
    }

    private void addInstructorCheck() {
        try {
            if (adminGUI.getInstructor_First_Name_Field_().getText().trim().isEmpty()
                    || adminGUI.getInstructor_Last_Name_Field_().getText().trim().isEmpty()
                    || adminGUI.getInstructor_Initials_Field_().getText().trim().isEmpty()
                    || adminGUI.getInstructor_Phone_Number_Field_().getText().trim().isEmpty()
                    || (adminGUI.getInstructor_Gender_Male_().isSelected() == false && adminGUI.getInstructor_Gender_Female_().isSelected() == false)
                    || adminGUI.getInstructor_Birthdate_DateChooser_().getDate() == null
                    || adminGUI.getInstructor_Email_Field_().getText().trim().isEmpty()
                    || adminGUI.getInstructor_Password_Field_().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (validator.instructorValidation(adminGUI.getInstructor_Email_Field_().getText(), adminGUI.getInstructor_Password_Field_().getText(), adminGUI.getInstructor_Initials_Field_().getText(), adminGUI.getInstructor_Phone_Number_Field_().getText())) {
                char gender;
                if (adminGUI.getInstructor_Gender_Male_().isSelected()) {
                    gender = 'M';
                } else {
                    gender = 'F';
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(adminGUI.getInstructor_Birthdate_DateChooser_().getDate());
                out.writeObject("Add instructor!");
                out.writeObject(adminGUI.getInstructor_Email_Field_().getText());
                out.writeObject(adminGUI.getInstructor_Password_Field_().getText());
                out.writeObject(adminGUI.getInstructor_First_Name_Field_().getText());
                out.writeObject(adminGUI.getInstructor_Last_Name_Field_().getText());
                out.writeObject(adminGUI.getInstructor_Initials_Field_().getText());
                out.writeObject(date);
                out.writeObject(gender);
                out.writeObject(adminGUI.getInstructor_Phone_Number_Field_().getText());
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while adding instructor account!");
            ex.printStackTrace();
        }
    }

    private void finishAddingInstructor(boolean check) {
        try {
            if (check) {
                adminGUI.getInstructor_Email_Field_().setText(null);
                adminGUI.getInstructor_Password_Field_().setText(null);
                adminGUI.getInstructor_First_Name_Field_().setText(null);
                adminGUI.getInstructor_Last_Name_Field_().setText(null);
                adminGUI.getInstructor_Initials_Field_().setText(null);
                adminGUI.getInstructor_Phone_Number_Field_().setText(null);
                adminGUI.getInstructor_Gender_ButtonGroup_().clearSelection();
                adminGUI.getInstructor_Birthdate_DateChooser_().setCalendar(null);
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "New instructor was added!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while finishing adding instructor!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing instructor account!");
            ex.printStackTrace();
        }
    }

    private void addStudentCheck() {
        try {
            if (adminGUI.getStudent_First_Name_Field_().getText().trim().isEmpty()
                    || adminGUI.getStudent_Last_Name_Field_().getText().trim().isEmpty()
                    || adminGUI.getStudent_City_Field_().getText().trim().isEmpty()
                    || adminGUI.getStudent_Street_Field_().getText().trim().isEmpty()
                    || adminGUI.getStudent_Postal_Code_Field_().getText().trim().isEmpty()
                    || (adminGUI.getStudent_Gender_Male_().isSelected() == false && adminGUI.getStudent_Gender_Female_().isSelected() == false)
                    || adminGUI.getStudent_Birthdate_DateChooser_().getDate() == null
                    || adminGUI.getStudent_Email_Field_().getText().trim().isEmpty()
                    || adminGUI.getStudent_Password_Field_().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (validator.studentValidation(adminGUI.getStudent_Email_Field_().getText(), adminGUI.getStudent_Password_Field_().getText(), adminGUI.getStudent_Postal_Code_Field_().getText())) {
                int index = adminGUI.getStudent_Instructor_ComboBox_().getSelectedIndex();
                char gender;
                if (adminGUI.getStudent_Gender_Male_().isSelected()) {
                    gender = 'M';
                } else {
                    gender = 'F';
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(adminGUI.getStudent_Birthdate_DateChooser_().getDate());
                out.writeObject("Add student!");
                out.writeObject(adminGUI.getStudent_Email_Field_().getText());
                out.writeObject(adminGUI.getStudent_Password_Field_().getText());
                out.writeObject(adminGUI.getStudent_First_Name_Field_().getText());
                out.writeObject(adminGUI.getStudent_Last_Name_Field_().getText());
                out.writeObject(adminGUI.getStudent_City_Field_().getText());
                out.writeObject(adminGUI.getStudent_Street_Field_().getText());
                out.writeObject(Integer.valueOf(adminGUI.getStudent_Postal_Code_Field_().getText()));
                out.writeObject(date);
                out.writeObject(gender);
                out.writeObject(studentComboboxList.get(index).getAccountId());
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while adding student account!");
            ex.printStackTrace();
        }
    }

    private void finishAddingStudent(boolean check) {
        try {
            if (check) {
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
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "New student was added!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while finishing adding student!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing student account!");
            ex.printStackTrace();
        }
    }

    private void fillStudentCombobox(ArrayList<Account> studentComboboxList) {
        new SwingWorker<DefaultComboBoxModel, Void>() {
            @Override
            protected DefaultComboBoxModel doInBackground() throws Exception {
                DefaultComboBoxModel refreshModel = new DefaultComboBoxModel();
                for (Account acc : studentComboboxList) {
                    refreshModel.addElement(acc.getFirstName() + " " + acc.getLastName());
                }
                return refreshModel;
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

    private void allAdminTableModel(ArrayList<Account> allAdminList) {
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel refreshModel = (DefaultTableModel) adminGUI.getGod_Admin_Table_().getModel();
                refreshModel.setRowCount(0);
                Object[] row = new Object[6];
                int i = 0;
                for (Account acc : allAdminList) {
                    row[0] = ++i;
                    row[1] = acc.getAccountId();
                    row[2] = acc.getFirstName();
                    row[3] = acc.getLastName();
                    if (acc.getGodId() == 0) {
                        row[4] = "No Admin!";
                    } else {
                        row[4] = acc.getGodId();
                    }
                    refreshModel.addRow(row);
                }
                return refreshModel;
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel refreshModel = get();
                    refreshModel.fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("An error occurred while refreshing admin table!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void adminTableModel(ArrayList<Account> adminAdminsList) {
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel refreshModel = (DefaultTableModel) adminGUI.getAdmin_Table_().getModel();
                refreshModel.setRowCount(0);
                Object[] row = new Object[8];
                int j = 0;
                for (Account acc : adminAdminsList) {
                    row[0] = ++j;
                    row[1] = acc.getAccountId();
                    row[2] = acc.getUsernameEmail();
                    row[3] = acc.getPassword();
                    row[4] = acc.getFirstName();
                    row[5] = acc.getLastName();
                    row[6] = acc.getEmail();
                    row[7] = acc.getPhoneNumber();
                    refreshModel.addRow(row);
                }
                return refreshModel;
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel refreshModel = get();
                    refreshModel.fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("An error occurred while refreshing admin table!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void instructorTableModel(ArrayList<Account> adminInstructorsList) {
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel refreshModel = (DefaultTableModel) adminGUI.getInstructor_Table_().getModel();
                refreshModel.setRowCount(0);
                Object[] row = new Object[8];
                int j = 0;
                for (Account acc : adminInstructorsList) {
                    row[0] = ++j;
                    row[1] = acc.getAccountId();
                    row[2] = acc.getUsernameEmail();
                    row[3] = acc.getPassword();
                    row[4] = acc.getFirstName();
                    row[5] = acc.getLastName();
                    row[6] = acc.getInitials();
                    row[7] = acc.getPhoneNumber();
                    refreshModel.addRow(row);
                }
                return refreshModel;
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel refreshModel = get();
                    refreshModel.fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("An error occurred while refreshing instructor table!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void studentTableModel(ArrayList<Account> adminStudentsList) {
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel refreshModel = (DefaultTableModel) adminGUI.getStudent_Table_().getModel();
                refreshModel.setRowCount(0);
                Object[] row = new Object[10];
                int j = 0;
                for (Account acc : adminStudentsList) {
                    row[0] = ++j;
                    row[1] = acc.getAccountId();
                    row[2] = acc.getUsernameEmail();
                    row[3] = acc.getPassword();
                    row[4] = acc.getFirstName();
                    row[5] = acc.getLastName();
                    row[6] = acc.getCity();
                    row[7] = acc.getStreet();
                    row[8] = acc.getPostalCode();
                    row[9] = acc.getInstructorId();
                    refreshModel.addRow(row);
                }
                return refreshModel;
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel refreshModel = get();
                    refreshModel.fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("An error occurred while refreshing student table!");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void editAdmin() {
        try {
            if (adminGUI.getAdmin_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int row = adminGUI.getAdmin_Table_().getSelectedRow();
            if (adminGUI.getAdmin_Table_().getModel().getValueAt(row, 2).toString().trim().isEmpty()
                    || adminGUI.getAdmin_Table_().getModel().getValueAt(row, 3).toString().trim().isEmpty()
                    || adminGUI.getAdmin_Table_().getModel().getValueAt(row, 4).toString().trim().isEmpty()
                    || adminGUI.getAdmin_Table_().getModel().getValueAt(row, 5).toString().trim().isEmpty()
                    || adminGUI.getAdmin_Table_().getModel().getValueAt(row, 6).toString().trim().isEmpty()
                    || adminGUI.getAdmin_Table_().getModel().getValueAt(row, 7).toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (validator.adminValidation(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 2).toString(),
                    adminGUI.getAdmin_Table_().getModel().getValueAt(row, 3).toString(),
                    adminGUI.getAdmin_Table_().getModel().getValueAt(row, 6).toString(),
                    adminGUI.getAdmin_Table_().getModel().getValueAt(row, 7).toString())) {
                out.writeObject("Check edited username!");
                out.writeObject(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 2).toString());
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while editing admin!");
            ex.printStackTrace();
        }
    }

    private void editUsernameCheck(boolean check) {
        try {
            if (check) {
                int row = adminGUI.getAdmin_Table_().getSelectedRow();
                int id = Integer.parseInt(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 1).toString());
                out.writeObject("Edit admin!");
                out.writeObject(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 2).toString());
                out.writeObject(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 3).toString());
                out.writeObject(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 4).toString());
                out.writeObject(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 5).toString());
                out.writeObject(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 6).toString());
                out.writeObject(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 7).toString());
                out.writeObject(id);
            } else {
                JOptionPane.showMessageDialog(null, "Account with this username already exists!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while checking new username for admin!");
            ex.printStackTrace();
        }
    }

    private void finishEditingAdmin(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have edited this admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while finishing editing admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing editing admin!");
            ex.printStackTrace();
        }
    }

    private void deleteAdmin() {
        try {
            if (adminGUI.getAdmin_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int choice;
            choice = JOptionPane.showConfirmDialog(null, """
                                                     Are you sure you want to delete this admin account?
                                                     You will be presented with a option of FORCE and SOFT delete if you want to continue.
                                                     With FORCE delete you will delete all instructors and students that this admin created!
                                                     It will also delete created test and submitted tests from instuctors and students!
                                                     With SOFT delete you will inherit all instructor and student that this admin created!
                                                     In both cases, created admins will go to god admin to be managed!
                                                     Do you want to continue?""", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return;
            }
            String[] delete = {"SOFT", "FORCE"};
            JComboBox cb = new JComboBox(delete);
            String choice2;
            int option = JOptionPane.showConfirmDialog(null, cb, "Select delete option!", JOptionPane.DEFAULT_OPTION);
            if (option == 0) {
                choice2 = cb.getSelectedItem().toString();
            } else {
                return;
            }
            int row = adminGUI.getAdmin_Table_().getSelectedRow();
            int id = Integer.parseInt(adminGUI.getAdmin_Table_().getModel().getValueAt(row, 1).toString());
            out.writeObject("Delete admin!");
            out.writeObject(id);
            out.writeObject(choice2);
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to delete admin account!");
            ex.printStackTrace();
        }
    }

    private void finishDeletingAdmin(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have deleted this admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while deleting this admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while deleting admin account!");
            ex.printStackTrace();
        }
    }

    private void editInstructor() {
        try {
            if (adminGUI.getInstructor_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int row = adminGUI.getInstructor_Table_().getSelectedRow();
            if (adminGUI.getInstructor_Table_().getModel().getValueAt(row, 2).toString().trim().isEmpty()
                    || adminGUI.getInstructor_Table_().getModel().getValueAt(row, 3).toString().trim().isEmpty()
                    || adminGUI.getInstructor_Table_().getModel().getValueAt(row, 4).toString().trim().isEmpty()
                    || adminGUI.getInstructor_Table_().getModel().getValueAt(row, 5).toString().trim().isEmpty()
                    || adminGUI.getInstructor_Table_().getModel().getValueAt(row, 6).toString().trim().isEmpty()
                    || adminGUI.getInstructor_Table_().getModel().getValueAt(row, 7).toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (validator.instructorValidation(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 2).toString(),
                    adminGUI.getInstructor_Table_().getModel().getValueAt(row, 3).toString(),
                    adminGUI.getInstructor_Table_().getModel().getValueAt(row, 6).toString(),
                    adminGUI.getInstructor_Table_().getModel().getValueAt(row, 7).toString())) {
                int id = Integer.parseInt(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 1).toString());
                out.writeObject("Edit instructor!");
                out.writeObject(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 2).toString());
                out.writeObject(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 3).toString());
                out.writeObject(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 4).toString());
                out.writeObject(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 5).toString());
                out.writeObject(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 6).toString());
                out.writeObject(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 7).toString());
                out.writeObject(id);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while editing instructor!");
            ex.printStackTrace();
        }
    }

    private void finishEditingInstructor(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have edited this instructor!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while finishing editing instructor!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing editing instructor!");
            ex.printStackTrace();
        }
    }

    private void deleteInstructor() {
        try {
            if (adminGUI.getInstructor_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int choice;
            choice = JOptionPane.showConfirmDialog(null, """
                                                     Are you sure you want to delete this instructor account?
                                                     If this instructor has created tests and assigned students it will delete them too!
                                                     If you don't want to do that go and change all student instructor id to some other instructor!
                                                     Do you want to continue?""", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return;
            }
            int row = adminGUI.getInstructor_Table_().getSelectedRow();
            int id = Integer.parseInt(adminGUI.getInstructor_Table_().getModel().getValueAt(row, 1).toString());
            out.writeObject("Delete instructor!");
            out.writeObject(id);
        } catch (IOException ex) {
            System.out.println("An error occurred while deleting instructor account!");
            ex.printStackTrace();
        }
    }

    private void finishDeletingInstructor(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have deleted this instructor!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while trying to deleted this instructor!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing deletion of instructor account!");
            ex.printStackTrace();
        }
    }

    private void editStudent() {
        try {
            if (adminGUI.getStudent_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int row = adminGUI.getStudent_Table_().getSelectedRow();
            if (adminGUI.getStudent_Table_().getModel().getValueAt(row, 2).toString().trim().isEmpty()
                    || adminGUI.getStudent_Table_().getModel().getValueAt(row, 3).toString().trim().isEmpty()
                    || adminGUI.getStudent_Table_().getModel().getValueAt(row, 4).toString().trim().isEmpty()
                    || adminGUI.getStudent_Table_().getModel().getValueAt(row, 5).toString().trim().isEmpty()
                    || adminGUI.getStudent_Table_().getModel().getValueAt(row, 6).toString().trim().isEmpty()
                    || adminGUI.getStudent_Table_().getModel().getValueAt(row, 7).toString().trim().isEmpty()
                    || adminGUI.getStudent_Table_().getModel().getValueAt(row, 8).toString().trim().isEmpty()
                    || adminGUI.getStudent_Table_().getModel().getValueAt(row, 9).toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure all fields are filled in correctly!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (validator.studentValidation(adminGUI.getStudent_Table_().getModel().getValueAt(row, 2).toString(),
                    adminGUI.getStudent_Table_().getModel().getValueAt(row, 3).toString(),
                    adminGUI.getStudent_Table_().getModel().getValueAt(row, 8).toString(),
                    adminGUI.getStudent_Table_().getModel().getValueAt(row, 9).toString())) {
                int instructorId = Integer.parseInt(adminGUI.getStudent_Table_().getModel().getValueAt(row, 9).toString());
                out.writeObject("Check instructor id!");
                out.writeObject(instructorId);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while editing student!");
            ex.printStackTrace();
        }
    }

    private void editInstructorIdCheck(boolean check) {
        try {
            if (check) {
                int row = adminGUI.getStudent_Table_().getSelectedRow();
                int id = Integer.parseInt(adminGUI.getStudent_Table_().getModel().getValueAt(row, 1).toString());
                int instructorId = Integer.parseInt(adminGUI.getStudent_Table_().getModel().getValueAt(row, 9).toString());
                int postalCode = Integer.parseInt(adminGUI.getStudent_Table_().getModel().getValueAt(row, 8).toString());
                out.writeObject("Edit student!");
                out.writeObject(id);
                out.writeObject(adminGUI.getStudent_Table_().getModel().getValueAt(row, 2).toString());
                out.writeObject(adminGUI.getStudent_Table_().getModel().getValueAt(row, 3).toString());
                out.writeObject(adminGUI.getStudent_Table_().getModel().getValueAt(row, 4).toString());
                out.writeObject(adminGUI.getStudent_Table_().getModel().getValueAt(row, 5).toString());
                out.writeObject(adminGUI.getStudent_Table_().getModel().getValueAt(row, 6).toString());
                out.writeObject(adminGUI.getStudent_Table_().getModel().getValueAt(row, 7).toString());
                out.writeObject(postalCode);
                out.writeObject(instructorId);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while checking instructor id!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while editing student!");
            ex.printStackTrace();
        }
    }

    private void finishEditingStudent(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have edited this student!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while finishing editing student!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing editing student!");
            ex.printStackTrace();
        }
    }

    private void deleteStudent() {
        try {
            if (adminGUI.getStudent_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int choice;
            choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this student account?", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return;
            }
            int row = adminGUI.getStudent_Table_().getSelectedRow();
            int id = Integer.parseInt(adminGUI.getStudent_Table_().getModel().getValueAt(row, 1).toString());
            out.writeObject("Delete student!");
            out.writeObject(id);
        } catch (IOException ex) {
            System.out.println("An error occurred while deleting student account!");
            ex.printStackTrace();
        }
    }

    private void finishDeletingStudent(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have deleted this student!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while trying to delete this student!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while finishing deletion of student account!");
            ex.printStackTrace();
        }
    }

    private void godAdminEdit() {
        try {
            if (adminGUI.getGod_Admin_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int row = adminGUI.getGod_Admin_Table_().getSelectedRow();
            String newId = adminGUI.getGod_Admin_Table_().getModel().getValueAt(row, 4).toString();
            if (validator.adminValidation(newId)) {
                int newAdminId = Integer.parseInt(newId);
                out.writeObject("Check admin id!");
                out.writeObject(newAdminId);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while checking admin id!");
            ex.printStackTrace();
        }
    }

    private void chechAdminId(boolean check) {
        try {
            if (check) {
                int row = adminGUI.getGod_Admin_Table_().getSelectedRow();
                int adminId = Integer.parseInt(adminGUI.getGod_Admin_Table_().getModel().getValueAt(row, 1).toString());
                int parsedNewId = Integer.parseInt(adminGUI.getGod_Admin_Table_().getModel().getValueAt(row, 4).toString());
                out.writeObject("Edit admin as god admin!");
                out.writeObject(adminId);
                out.writeObject(parsedNewId);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while checking admin id!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to edit admin as god admin!");
            ex.printStackTrace();
        }
    }

    private void finishEditingAsGodAdmin(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have edited this admin as god admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while editing this admin as god admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to refresh edit as god admin!");
            ex.printStackTrace();
        }
    }

    private void godAdminDelete() {
        try {
            if (adminGUI.getGod_Admin_Table_().getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You must have a selected row first!", "Info!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int choice;
            choice = JOptionPane.showConfirmDialog(null, """
                                                     Are you sure you want to delete this admin account?
                                                     You will be presented with a option of FORCE and SOFT delete if you want to continue.
                                                     With FORCE delete you will delete all instructors and students that this admin created!
                                                     It will also delete created test and submitted tests from instuctors and students!
                                                     With SOFT delete you will inherit all instructor and student that this admin created!
                                                     In both cases, created admins will go to god admin to be managed!
                                                     Do you want to continue?""", "Check!", JOptionPane.YES_NO_OPTION);
            if (choice == 1 || choice == -1) {
                return;
            }
            String[] delete = {"SOFT", "FORCE"};
            JComboBox cb = new JComboBox(delete);
            String choice2;
            int option = JOptionPane.showConfirmDialog(null, cb, "Select delete option!", JOptionPane.DEFAULT_OPTION);
            if (option == 0) {
                choice2 = cb.getSelectedItem().toString();
            } else {
                return;
            }
            int row = adminGUI.getGod_Admin_Table_().getSelectedRow();
            int Id = Integer.parseInt(adminGUI.getGod_Admin_Table_().getModel().getValueAt(row, 1).toString());
            out.writeObject("Delete admin as a god admin!");
            out.writeObject(Id);
            out.writeObject(choice2);
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to delete admin account as god admin!");
            ex.printStackTrace();
        }
    }

    private void finishDeletingAsGodAdmin(boolean check) {
        try {
            if (check) {
                out.writeObject("Change in database!");
                JOptionPane.showMessageDialog(null, "You have deleted this admin as god admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong while deleting this admin as god admin!", "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to refresh delete admin as god admin!");
            ex.printStackTrace();
        }
    }

    public void listen() {
        new Thread(
                () -> {
                    String operation;
                    while (socket.isConnected()) {
                        try {
                            operation = (String) in.readObject();
                            if (operation.equals("Add admin!")) {
                                boolean check = (boolean) in.readObject();
                                finishAddingAdmin(check);
                            }
                            if (operation.equals("Add instructor!")) {
                                boolean check = (boolean) in.readObject();
                                finishAddingInstructor(check);
                            }
                            if (operation.equals("Add student!")) {
                                boolean check = (boolean) in.readObject();
                                finishAddingStudent(check);
                            }
                            if (operation.equals("Check username!")) {
                                boolean check = (boolean) in.readObject();
                                checkUsername(check);
                            }
                            if (operation.equals("Edit admin!")) {
                                boolean check = (boolean) in.readObject();
                                finishEditingAdmin(check);
                            }
                            if (operation.equals("Edit instructor!")) {
                                boolean check = (boolean) in.readObject();
                                finishEditingInstructor(check);
                            }
                            if (operation.equals("Edit student!")) {
                                boolean check = (boolean) in.readObject();
                                finishEditingStudent(check);
                            }
                            if (operation.equals("Edit admin as god admin!")) {
                                boolean check = (boolean) in.readObject();
                                finishEditingAsGodAdmin(check);
                            }
                            if (operation.equals("Check admin id!")) {
                                boolean check = (boolean) in.readObject();
                                chechAdminId(check);
                            }
                            if (operation.equals("Check instructor id!")) {
                                boolean check = (boolean) in.readObject();
                                editInstructorIdCheck(check);
                            }
                            if (operation.equals("Check edited username!")) {
                                boolean check = (boolean) in.readObject();
                                editUsernameCheck(check);
                            }
                            if (operation.equals("Delete admin!")) {
                                boolean check = (boolean) in.readObject();
                                finishDeletingAdmin(check);
                            }
                            if (operation.equals("Delete instructor!")) {
                                boolean check = (boolean) in.readObject();
                                finishDeletingInstructor(check);
                            }
                            if (operation.equals("Delete student!")) {
                                boolean check = (boolean) in.readObject();
                                finishDeletingStudent(check);
                            }
                            if (operation.equals("Delete admin as a god admin!")) {
                                boolean check = (boolean) in.readObject();
                                finishDeletingAsGodAdmin(check);
                            }
                            if (operation.equals("Change in database!")) {
                                studentComboboxList = (ArrayList<Account>) in.readObject();
                                fillStudentCombobox(studentComboboxList);
                                ArrayList<Account> allAdminList = (ArrayList<Account>) in.readObject();
                                allAdminTableModel(allAdminList);
                                ArrayList<Account> adminAdminsList = (ArrayList<Account>) in.readObject();
                                adminTableModel(adminAdminsList);
                                ArrayList<Account> adminInstructorsList = (ArrayList<Account>) in.readObject();
                                instructorTableModel(adminInstructorsList);
                                ArrayList<Account> adminStudentsList = (ArrayList<Account>) in.readObject();
                                studentTableModel(adminStudentsList);
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            close(socket, in, out);
                            break;
                        }
                    }
                }, "listenThread"
        ).start();
    }

    private void close(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
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
            System.out.println("An error occurred while closing socket and streams on a client side!");
            ex.printStackTrace();
        }
    }
}
