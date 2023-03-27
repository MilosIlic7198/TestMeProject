/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jdbc.DatabaseConnection;
import dao.contracts.AdminContract;
import dto.Account;
import dto.AccountBuilder;
import dto.Director;
import encryptor.Encryptor;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class AdminAccess implements AdminContract {

    private final Encryptor encryptor = new Encryptor();

    @Override
    public boolean checkUsername(String username) {
        String query = "SELECT a.* FROM account a WHERE a.username_email = ?";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, username);
            try ( ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while checking username for admin!");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkForInstructorID(int instructorId) {
        String query = "SELECT i.instructor_id FROM instructor i WHERE i.instructor_id = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, instructorId);
            try ( ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while checking instructor id for admin!");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkForAdminID(int adminId) {
        String query = "SELECT a.admin_id FROM admin a WHERE a.admin_id = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, adminId);
            try ( ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while checking admin id for admin!");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addAccount(Account account) {
        try ( Connection con = DatabaseConnection.getConnection()) {
            String addAccountQuery = "INSERT INTO account (username_email, password, account_type) VALUES(?, ?, ?);";
            String addAdminQuery = "INSERT INTO admin (admin_id, first_name, last_name, email, phone_number, god_id) VALUES(?, ?, ?, ?, ?, ?);";
            String addInstructorQuery = "INSERT INTO instructor (instructor_id, first_name, last_name, initials, birthdate, gender, phone_number, admin_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
            String addStudentQuery = "INSERT INTO student (student_id, first_name, last_name, city, street, postal_code, birthdate, gender, instructor_id, admin_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            con.setAutoCommit(false);
            try ( PreparedStatement addAccount = con.prepareStatement(addAccountQuery, Statement.RETURN_GENERATED_KEYS);  PreparedStatement addAdmin = con.prepareStatement(addAdminQuery);  PreparedStatement addInstructor = con.prepareStatement(addInstructorQuery);  PreparedStatement addStudent = con.prepareStatement(addStudentQuery)) {
                String pwd = encryptor.getEncryptedString(account.getPassword());
                switch (account.getAccountType()) {
                    case 'A' -> {
                        addAccount.setString(1, account.getUsernameEmail());
                        addAccount.setString(2, pwd);
                        addAccount.setString(3, String.valueOf(account.getAccountType()));
                        addAccount.executeUpdate();
                        try ( ResultSet rs = addAccount.getGeneratedKeys()) {
                            rs.next();
                            addAdmin.setInt(1, rs.getInt(1));
                            addAdmin.setString(2, account.getFirstName());
                            addAdmin.setString(3, account.getLastName());
                            addAdmin.setString(4, account.getEmail());
                            addAdmin.setString(5, account.getPhoneNumber());
                            addAdmin.setInt(6, account.getGodId());
                            addAdmin.executeUpdate();
                        }
                        con.commit();
                    }
                    case 'I' -> {
                        addAccount.setString(1, account.getUsernameEmail());
                        addAccount.setString(2, pwd);
                        addAccount.setString(3, String.valueOf(account.getAccountType()));
                        addAccount.executeUpdate();
                        try ( ResultSet rs = addAccount.getGeneratedKeys()) {
                            rs.next();
                            addInstructor.setInt(1, rs.getInt(1));
                            addInstructor.setString(2, account.getFirstName());
                            addInstructor.setString(3, account.getLastName());
                            addInstructor.setString(4, account.getInitials());
                            addInstructor.setString(5, account.getBirthDate());
                            addInstructor.setString(6, String.valueOf(account.getGender()));
                            addInstructor.setString(7, account.getPhoneNumber());
                            addInstructor.setInt(8, account.getAdminId());
                            addInstructor.executeUpdate();
                        }
                        con.commit();
                    }
                    case 'S' -> {
                        addAccount.setString(1, account.getUsernameEmail());
                        addAccount.setString(2, pwd);
                        addAccount.setString(3, String.valueOf(account.getAccountType()));
                        addAccount.executeUpdate();
                        try ( ResultSet rs = addAccount.getGeneratedKeys()) {
                            rs.next();
                            addStudent.setInt(1, rs.getInt(1));
                            addStudent.setString(2, account.getFirstName());
                            addStudent.setString(3, account.getLastName());
                            addStudent.setString(4, account.getCity());
                            addStudent.setString(5, account.getStreet());
                            addStudent.setInt(6, account.getPostalCode());
                            addStudent.setString(7, account.getBirthDate());
                            addStudent.setString(8, String.valueOf(account.getGender()));
                            addStudent.setInt(9, account.getInstructorId());
                            addStudent.setInt(10, account.getAdminId());
                            addStudent.executeUpdate();
                        }
                        con.commit();
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Insert is being rolled back!");
                ex.printStackTrace();
                con.rollback();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while adding account as admin!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean editAccount(Account account) {
        try ( Connection con = DatabaseConnection.getConnection()) {
            String editAccountQuery = "UPDATE account  SET username_email = ?, password = ? WHERE account_id = ?;";
            String editAdminQuery = "UPDATE admin SET first_name = ?, last_name = ?, email = ?, phone_number = ? WHERE admin_id = ?;";
            String editInstructorQuery = "UPDATE instructor SET first_name = ?, last_name = ?, initials = ?, phone_number = ? WHERE instructor_id = ?;";
            String editStudentQuery = "UPDATE student SET first_name = ?, last_name = ?, city = ?, street = ?, postal_code = ?, instructor_id = ? WHERE student_id = ?;";
            con.setAutoCommit(false);
            try ( PreparedStatement editAccount = con.prepareStatement(editAccountQuery);  PreparedStatement editAdmin = con.prepareStatement(editAdminQuery);  PreparedStatement editInstructor = con.prepareStatement(editInstructorQuery);  PreparedStatement editStudent = con.prepareStatement(editStudentQuery)) {
                String pwd = encryptor.getEncryptedString(account.getPassword());
                switch (account.getAccountType()) {
                    case 'A' -> {
                        editAccount.setString(1, account.getUsernameEmail());
                        editAccount.setString(2, pwd);
                        editAccount.setInt(3, account.getAccountId());
                        editAccount.executeUpdate();
                        editAdmin.setString(1, account.getFirstName());
                        editAdmin.setString(2, account.getLastName());
                        editAdmin.setString(3, account.getEmail());
                        editAdmin.setString(4, account.getPhoneNumber());
                        editAdmin.setInt(5, account.getAccountId());
                        editAdmin.executeUpdate();
                        con.commit();
                    }
                    case 'I' -> {
                        editAccount.setString(1, account.getUsernameEmail());
                        editAccount.setString(2, pwd);
                        editAccount.setInt(3, account.getAccountId());
                        editAccount.executeUpdate();
                        editInstructor.setString(1, account.getFirstName());
                        editInstructor.setString(2, account.getLastName());
                        editInstructor.setString(3, account.getInitials());
                        editInstructor.setString(4, account.getPhoneNumber());
                        editInstructor.setInt(5, account.getAccountId());
                        editInstructor.executeUpdate();
                        con.commit();
                    }
                    case 'S' -> {
                        editAccount.setString(1, account.getUsernameEmail());
                        editAccount.setString(2, pwd);
                        editAccount.setInt(3, account.getAccountId());
                        editAccount.executeUpdate();
                        editStudent.setString(1, account.getFirstName());
                        editStudent.setString(2, account.getLastName());
                        editStudent.setString(3, account.getCity());
                        editStudent.setString(4, account.getStreet());
                        editStudent.setInt(5, account.getPostalCode());
                        editStudent.setInt(6, account.getInstructorId());
                        editStudent.setInt(7, account.getAccountId());
                        editStudent.executeUpdate();
                        con.commit();
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Edit is being rolled back!");
                ex.printStackTrace();
                con.rollback();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while editing account as admin!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean godAdminEditAdmin(int adminId, int newAdminId) {
        String editAdminToNewAdmin = "UPDATE admin SET god_id = ? WHERE admin_id = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(editAdminToNewAdmin)) {
            pst.setInt(1, newAdminId);
            pst.setInt(2, adminId);
            return pst.executeUpdate() >= 1;
        } catch (SQLException ex) {
            System.out.println("An error occurred while editing admin as god admin!");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAdmin(int adminId, int IdOfAdminToDelete, String choice) {
        if (choice.equals("SOFT")) {
            try ( Connection con = DatabaseConnection.getConnection()) {
                String editStudentQuery = "UPDATE student s SET s.admin_id = ? WHERE s.admin_id = ?;";
                String editInstructorQuery = "UPDATE instructor i SET i.admin_id = ? WHERE i.admin_id = ?";
                String deleteAdminQuery = "DELETE FROM account WHERE account_id = ?;";
                con.setAutoCommit(false);
                try ( PreparedStatement editStudent = con.prepareStatement(editStudentQuery);  PreparedStatement editInstructor = con.prepareStatement(editInstructorQuery);  PreparedStatement deleteAdmin = con.prepareStatement(deleteAdminQuery)) {
                    editStudent.setInt(1, adminId);
                    editStudent.setInt(2, IdOfAdminToDelete);
                    editStudent.executeUpdate();
                    editInstructor.setInt(1, adminId);
                    editInstructor.setInt(2, IdOfAdminToDelete);
                    editInstructor.executeUpdate();
                    deleteAdmin.setInt(1, IdOfAdminToDelete);
                    deleteAdmin.executeUpdate();
                    con.commit();
                } catch (SQLException ex) {
                    System.out.println("Delete is being rolled back!");
                    ex.printStackTrace();
                    con.rollback();
                    return false;
                }
            } catch (SQLException ex) {
                System.out.println("An error occurred while soft deleting admin!");
                ex.printStackTrace();
                return false;
            }
        }
        if (choice.equals("FORCE")) {
            try ( Connection con = DatabaseConnection.getConnection()) {
                String deleteStudentQuery = "DELETE a FROM account a JOIN student s ON a.account_id = s.student_id WHERE s.admin_id = ?;";
                String deleteInstructorQuery = "DELETE a FROM account a JOIN instructor i ON a.account_id = i.instructor_id WHERE i.admin_id = ?";
                String deleteAdminQuery = "DELETE FROM account WHERE account_id = ?;";
                con.setAutoCommit(false);
                try ( PreparedStatement deleteStudent = con.prepareStatement(deleteStudentQuery);  PreparedStatement deleteInstructor = con.prepareStatement(deleteInstructorQuery);  PreparedStatement deleteAdmin = con.prepareStatement(deleteAdminQuery)) {
                    deleteStudent.setInt(1, IdOfAdminToDelete);
                    deleteStudent.executeUpdate();
                    deleteInstructor.setInt(1, IdOfAdminToDelete);
                    deleteInstructor.executeUpdate();
                    deleteAdmin.setInt(1, IdOfAdminToDelete);
                    deleteAdmin.executeUpdate();
                    con.commit();
                } catch (SQLException ex) {
                    System.out.println("Delete is being rolled back!");
                    ex.printStackTrace();
                    con.rollback();
                    return false;
                }
            } catch (SQLException ex) {
                System.out.println("An error occurred while force deleting admin!");
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteInstructor(int instructorId) {
        try ( Connection con = DatabaseConnection.getConnection()) {
            String deleteStudentQuery = "DELETE a FROM account a JOIN student s ON a.account_id = s.student_id WHERE s.instructor_id = ?;";
            String deleteInstructorQuery = "DELETE FROM account WHERE account_id = ?;";
            con.setAutoCommit(false);
            try ( PreparedStatement deleteStudent = con.prepareStatement(deleteStudentQuery);  PreparedStatement deleteInstructor = con.prepareStatement(deleteInstructorQuery)) {
                deleteStudent.setInt(1, instructorId);
                deleteStudent.executeUpdate();
                deleteInstructor.setInt(1, instructorId);
                deleteInstructor.executeUpdate();
                con.commit();
            } catch (SQLException ex) {
                System.out.println("Delete is being rolled back!");
                ex.printStackTrace();
                con.rollback();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while deleting instructor!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteStudent(int studentId) {
        String deleteStudentQuery = "DELETE FROM account WHERE account_id = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement deleteStudent = con.prepareStatement(deleteStudentQuery)) {
            deleteStudent.setInt(1, studentId);
            return deleteStudent.executeUpdate() >= 1;
        } catch (SQLException ex) {
            System.out.println("An error occurred while deleting student!");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Account> getAdminInstructors(int adminId) {
        String adminInstructorsQuery = "SELECT i.instructor_id, i.first_name, i.last_name FROM instructor i WHERE admin_id = ?;";
        ArrayList<Account> iList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement adminInstructors = con.prepareStatement(adminInstructorsQuery)) {
            adminInstructors.setInt(1, adminId);
            try ( ResultSet rs = adminInstructors.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    AccountBuilder accountBuilder = new AccountBuilder();
                    Account ins;
                    while (rs.next()) {
                        accountBuilder.accountId(rs.getInt(1))
                                .firstName(rs.getString(2))
                                .lastName(rs.getString(3));
                        ins = accountBuilder.build();
                        iList.add(ins);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while getting admin instructors!");
            ex.printStackTrace();
        }
        return iList;
    }

    @Override
    public ArrayList<Account> getAllAdmins() {
        String allAdminsQuery = "SELECT * FROM admin a WHERE a.admin_id != 1;";
        ArrayList<Account> aList = new ArrayList<>();
        try ( Connection con = DatabaseConnection.getConnection();  Statement cst = con.createStatement()) {
            try ( ResultSet rs = cst.executeQuery(allAdminsQuery)) {
                if (rs.isBeforeFirst()) {
                    AccountBuilder accountBuilder = new AccountBuilder();
                    Account adm;
                    while (rs.next()) {
                        accountBuilder.accountId(rs.getInt(1))
                                .firstName(rs.getString(2))
                                .lastName(rs.getString(3))
                                .godId(rs.getInt(6));
                        adm = accountBuilder.build();
                        aList.add(adm);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while getting all admins!");
            ex.printStackTrace();
        }
        return aList;
    }

    @Override
    public ArrayList<Account> getAdminAccounts(int adminId) {
        String getAdminQuery = "SELECT * FROM admin_admin a WHERE a.god_id = ?;";
        ArrayList<Account> aList = new ArrayList<>();
        Director director = new Director();
        AccountBuilder accountBuilder = new AccountBuilder();
        Account account;
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(getAdminQuery)) {
            pst.setInt(1, adminId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        director.buildAdminAccount(accountBuilder);
                        accountBuilder.accountId(rs.getInt(1))
                                .usernameEmail(rs.getString(2))
                                .password(encryptor.getDecryptedString(rs.getString(3)))
                                .firstName(rs.getString(4))
                                .lastName(rs.getString(5))
                                .email(rs.getString(6))
                                .phoneNumber(rs.getString(7));
                        account = accountBuilder.build();
                        aList.add(account);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while getting admin accounts!");
            ex.printStackTrace();
        }
        return aList;
    }

    @Override
    public ArrayList<Account> getInstructorAccounts(int adminId) {
        String getInstructorQuery = "SELECT * FROM admin_instructor a WHERE a.admin_id = ?;";
        ArrayList<Account> aList = new ArrayList<>();
        Director director = new Director();
        AccountBuilder accountBuilder = new AccountBuilder();
        Account account;
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(getInstructorQuery)) {
            pst.setInt(1, adminId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        director.buildInstructorAccount(accountBuilder);
                        accountBuilder.accountId(rs.getInt(1))
                                .usernameEmail(rs.getString(2))
                                .password(encryptor.getDecryptedString(rs.getString(3)))
                                .firstName(rs.getString(4))
                                .lastName(rs.getString(5))
                                .initials(rs.getString(6))
                                .phoneNumber(rs.getString(7));
                        account = accountBuilder.build();
                        aList.add(account);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while getting instructor accounts!");
            ex.printStackTrace();
        }
        return aList;
    }

    @Override
    public ArrayList<Account> getStudentAccounts(int adminId) {
        String getStudentQuery = "SELECT * FROM admin_student a WHERE a.admin_id = ?;";
        ArrayList<Account> aList = new ArrayList<>();
        Director director = new Director();
        AccountBuilder accountBuilder = new AccountBuilder();
        Account account;
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(getStudentQuery)) {
            pst.setInt(1, adminId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        director.buildStudentAccount(accountBuilder);
                        accountBuilder.accountId(rs.getInt(1))
                                .usernameEmail(rs.getString(2))
                                .password(encryptor.getDecryptedString(rs.getString(3)))
                                .firstName(rs.getString(4))
                                .lastName(rs.getString(5))
                                .city(rs.getString(6))
                                .street(rs.getString(7))
                                .postalCode(rs.getInt(8))
                                .instructorId(rs.getInt(9));
                        account = accountBuilder.build();
                        aList.add(account);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while getting student accounts!");
            ex.printStackTrace();
        }
        return aList;
    }
}
