/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.jdbc.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dao.contracts.AccountContract;
import dao.dto.Account;
import dao.dto.AccountBuilder;
import dao.dto.Director;
import java.sql.Connection;

/**
 *
 * @author Milos
 */
public class AccountAccess implements AccountContract {

    @Override
    public boolean checkAccount(String usernameEmail, String password) {
        String query = "SELECT a.* FROM account a WHERE a.username_email = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, usernameEmail);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(3).equals(password);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public Account getAccount(String usernameEmail) {
        String query = "SELECT a.* FROM account a WHERE a.username_email = ?";
        Account acc = null;
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, usernameEmail);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    acc = getUser(rs.getInt(1), rs.getString(4).charAt(0));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return acc;
    }

    private Account getUser(int id, char type) {
        String getAdminQuery = "SELECT * FROM get_admin a WHERE a.admin_id = ?;";
        String getInstructorQuery = "SELECT * FROM get_instructor i WHERE i.instructor_id = ?;";
        String getStudentQuery = "SELECT * FROM get_student s WHERE s.student_id = ?;";
        try ( Connection con = DatabaseConnection.getConnection();  PreparedStatement getAdmin = con.prepareStatement(getAdminQuery);  PreparedStatement getInstructor = con.prepareStatement(getInstructorQuery);  PreparedStatement getStudent = con.prepareStatement(getStudentQuery)) {
            switch (type) {
                case 'A' -> {
                    getAdmin.setInt(1, id);
                    try ( ResultSet adminRS = getAdmin.executeQuery()) {
                        if (adminRS.next()) {
                            AccountBuilder accountBuilder = new AccountBuilder();
                            Director director = new Director();
                            director.buildAdminAccount(accountBuilder);
                            accountBuilder.accountId(adminRS.getInt(1))
                                    .usernameEmail(adminRS.getString(2))
                                    .firstName(adminRS.getString(3))
                                    .lastName(adminRS.getString(4))
                                    .godId(adminRS.getInt(5));
                            Account account = accountBuilder.build();
                            return account;
                        }
                    }
                }
                case 'I' -> {
                    getInstructor.setInt(1, id);
                    try ( ResultSet instructorRS = getInstructor.executeQuery()) {
                        if (instructorRS.next()) {
                            AccountBuilder accountBuilder = new AccountBuilder();
                            Director director = new Director();
                            director.buildInstructorAccount(accountBuilder);
                            accountBuilder.accountId(instructorRS.getInt(1))
                                    .email(instructorRS.getString(2))
                                    .firstName(instructorRS.getString(3))
                                    .lastName(instructorRS.getString(4))
                                    .initials(instructorRS.getString(5))
                                    .adminId(instructorRS.getInt(6));
                            Account account = accountBuilder.build();
                            return account;
                        }
                    }
                }
                case 'S' -> {
                    getStudent.setInt(1, id);
                    try ( ResultSet studentRS = getStudent.executeQuery()) {
                        if (studentRS.next()) {
                            AccountBuilder accountBuilder = new AccountBuilder();
                            Director director = new Director();
                            director.buildStudentAccount(accountBuilder);
                            accountBuilder.accountId(studentRS.getInt(1))
                                    .email(studentRS.getString(2))
                                    .firstName(studentRS.getString(3))
                                    .lastName(studentRS.getString(4))
                                    .instructorId(studentRS.getInt(5));
                            Account account = accountBuilder.build();
                            return account;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }
}
