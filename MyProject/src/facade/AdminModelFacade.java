/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import dao.AdminAccess;
import dao.contracts.AdminContract;
import dao.dto.Account;
import encryptor.Encryptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import service.CurrentAccount;

/**
 *
 * @author Milos
 */
public class AdminModelFacade {

    private Account admin;
    private AdminContract adminDAO;
    private Encryptor encryptor;
    private List<Account> studentComboboxList;
    private List<Account> allAdminList;
    private List<Account> adminAdminsList;
    private List<Account> adminInstructorsList;
    private List<Account> adminStudentsList;

    public AdminModelFacade() {
        this.admin = CurrentAccount.getInstance(null).getAccount();
        this.adminDAO = new AdminAccess();
        this.encryptor = new Encryptor();
        this.studentComboboxList = new ArrayList<>();
        this.allAdminList = new ArrayList<>();
        this.adminAdminsList = new ArrayList<>();
        this.adminInstructorsList = new ArrayList<>();
        this.adminStudentsList = new ArrayList<>();
    }

    public int getIdFromComboboxList(int index) {
        return studentComboboxList.get(index).getAccountId();
    }

    public DefaultComboBoxModel getStudentComboboxModel() {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        populateStudentComboboxList();
        populateComboboxModel(m);
        return m;
    }

    private void populateStudentComboboxList() {
        studentComboboxList = adminDAO.getAdminInstructors(admin.getAccountId());
    }

    private void populateComboboxModel(DefaultComboBoxModel m) {
        for (Account i : studentComboboxList) {
            m.addElement(i.getFirstName() + " " + i.getLastName());
        }
    }

    public DefaultTableModel getAllAdminTableModel() {
        DefaultTableModel allAdminModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return switch (column) {
                    case 0, 1, 2, 3 ->
                        false;
                    default ->
                        true;
                };
            }
        };
        allAdminModel.addColumn("No.");
        allAdminModel.addColumn("Admin ID");
        allAdminModel.addColumn("First Name");
        allAdminModel.addColumn("Last Name");
        allAdminModel.addColumn("God ID");
        populateAllAdminList();
        populateAllAdminTableModel(allAdminModel);
        return allAdminModel;
    }

    public DefaultTableModel getAdminTableModel() {
        DefaultTableModel adminModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return switch (column) {
                    case 0, 1 ->
                        false;
                    default ->
                        true;
                };
            }
        };
        adminModel.addColumn("No.");
        adminModel.addColumn("Admin ID");
        adminModel.addColumn("Username");
        adminModel.addColumn("Password");
        adminModel.addColumn("First Name");
        adminModel.addColumn("Last Name");
        adminModel.addColumn("Email");
        adminModel.addColumn("Phone Number");
        populateAdminAdminsList();
        populateAdminTableModel(adminModel);
        return adminModel;
    }

    public DefaultTableModel getInstructorTableModel() {
        DefaultTableModel instructorModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return switch (column) {
                    case 0, 1 ->
                        false;
                    default ->
                        true;
                };
            }
        };
        instructorModel.addColumn("No.");
        instructorModel.addColumn("Instructor ID");
        instructorModel.addColumn("Email");
        instructorModel.addColumn("Password");
        instructorModel.addColumn("First Name");
        instructorModel.addColumn("Last Name");
        instructorModel.addColumn("Initials");
        instructorModel.addColumn("Phone Number");
        populateAdminInstructorsList();
        populateInstructorTableModel(instructorModel);
        return instructorModel;
    }

    public DefaultTableModel getStudentTableModel() {
        DefaultTableModel studentModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return switch (column) {
                    case 0, 1 ->
                        false;
                    default ->
                        true;
                };
            }
        };
        studentModel.addColumn("No.");
        studentModel.addColumn("Student ID");
        studentModel.addColumn("Email");
        studentModel.addColumn("Password");
        studentModel.addColumn("First Name");
        studentModel.addColumn("Last Name");
        studentModel.addColumn("City");
        studentModel.addColumn("Street");
        studentModel.addColumn("Postal Code");
        studentModel.addColumn("Instructor ID");
        populateAdminStudentsList();
        populateStudentTableModel(studentModel);
        return studentModel;
    }

    private void populateAllAdminList() {
        allAdminList = adminDAO.getAllAdmins();
    }

    private void populateAdminAdminsList() {
        adminAdminsList = adminDAO.getAdminAccounts(admin.getAccountId());
    }

    private void populateAdminInstructorsList() {
        adminInstructorsList = adminDAO.getInstructorAccounts(admin.getAccountId());
    }

    private void populateAdminStudentsList() {
        adminStudentsList = adminDAO.getStudentAccounts(admin.getAccountId());
    }

    private void populateAllAdminTableModel(DefaultTableModel model) {
        Object[] aRow = new Object[5];
        int j = 0;
        for (Account acc : allAdminList) {
            aRow[0] = ++j;
            aRow[1] = acc.getAccountId();
            aRow[2] = acc.getFirstName();
            aRow[3] = acc.getLastName();
            if (acc.getGodId() == 0) {
                aRow[4] = "No Admin!";
            } else {
                aRow[4] = acc.getGodId();
            }
            model.addRow(aRow);
        }
    }

    private void populateAdminTableModel(DefaultTableModel model) {
        Object[] aRow = new Object[8];
        String pwd;
        int j = 0;
        for (Account acc : adminAdminsList) {
            aRow[0] = ++j;
            aRow[1] = acc.getAccountId();
            aRow[2] = acc.getUsernameEmail();
            pwd = encryptor.getDecryptedString(acc.getPassword());
            aRow[3] = pwd;
            aRow[4] = acc.getFirstName();
            aRow[5] = acc.getLastName();
            aRow[6] = acc.getEmail();
            aRow[7] = acc.getPhoneNumber();
            model.addRow(aRow);
        }
    }

    private void populateInstructorTableModel(DefaultTableModel model) {
        Object[] iRow = new Object[8];
        String pwd;
        int j = 0;
        for (Account acc : adminInstructorsList) {
            iRow[0] = ++j;
            iRow[1] = acc.getAccountId();
            iRow[2] = acc.getUsernameEmail();
            pwd = encryptor.getDecryptedString(acc.getPassword());
            iRow[3] = pwd;
            iRow[4] = acc.getFirstName();
            iRow[5] = acc.getLastName();
            iRow[6] = acc.getInitials();
            iRow[7] = acc.getPhoneNumber();
            model.addRow(iRow);
        }
    }

    private void populateStudentTableModel(DefaultTableModel model) {
        Object[] sRow = new Object[10];
        String pwd;
        int j = 0;
        for (Account acc : adminStudentsList) {
            sRow[0] = ++j;
            sRow[1] = acc.getAccountId();
            sRow[2] = acc.getUsernameEmail();
            pwd = encryptor.getDecryptedString(acc.getPassword());
            sRow[3] = pwd;
            sRow[4] = acc.getFirstName();
            sRow[5] = acc.getLastName();
            sRow[6] = acc.getCity();
            sRow[7] = acc.getStreet();
            sRow[8] = acc.getPostalCode();
            sRow[9] = acc.getInstructorId();
            model.addRow(sRow);
        }
    }
}
