/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.jdbc.DatabaseConnection;
import dao.contracts.AdminContract;
import dao.dto.Admin;
import dao.dto.Instructor;
import dao.dto.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Milos
 */
public class AdminAccess implements AdminContract {

    private String query;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public void addAdmin(Admin a) throws SQLException {
    }

    @Override
    public void addInstructor(Instructor i) throws SQLException {
        //TODO: Add account info for instructor!
        query = "INSERT INTO account(username_email, password, account_type) VALUES(?, ?, ?);";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setString(1, i.getEmail());
        pst.setString(2, i.getPassword());
        pst.setString(3, "I");
        if (pst.executeUpdate() > 0) {
            query = "INSERT INTO instructor (instructor_id, first_name, last_name, initials, birthdate, gender, phone_number, admin_id) VALUES(LAST_INSERT_ID(), ?, ?, ?, ?, ?, ?, ?);";
            pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            pst.setString(1, i.getFirstName());
            pst.setString(2, i.getLastName());
            pst.setString(3, i.getInitials());
            pst.setString(4, i.getBirthDate());
            pst.setString(5, String.valueOf(i.getGender()));
            pst.setString(6, i.getPhoneNumber());
            pst.setInt(7, i.getAdminId());
            pst.executeUpdate();
        }
        pst.close();
    }

    @Override
    public void addStudent(Student s) throws SQLException {
    }
}
