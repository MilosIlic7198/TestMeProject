/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.contracts.BaseContract;
import dao.jdbc.DatabaseConnection;
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
public class BaseAccess implements BaseContract {

    private String query;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public Object getUser(String loginDetails, String password) throws SQLException {
        Object obj = null;
        query = "SELECT a.* FROM account a WHERE a.username_email = ? AND a.password = ?;";
        pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        pst.setString(1, loginDetails);
        pst.setString(2, password);
        rs = pst.executeQuery();
        if (rs.next()) {
            switch (rs.getString(4).charAt(0)) {
                case 'A' -> {
                    query = "SELECT a.admin_id, a.first_name, a.last_name, a.god_id FROM admin a WHERE a.admin_id = ?;";
                    pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
                    pst.setInt(1, rs.getInt(1));
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        obj = (Admin) new Admin(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                    }
                }
                case 'I' -> {
                    query = "SELECT i.instructor_id, i.first_name, i.last_name, i.initials, i.admin_id FROM instructor i WHERE i.instructor_id = ?;";
                    pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
                    pst.setInt(1, rs.getInt(1));
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        obj = (Instructor) new Instructor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                    }
                }
                case 'S' -> {
                    query = "SELECT s.student_id, s.first_name, s.last_name, s.instructor_id FROM student s WHERE s.student_id = ?;";
                    pst = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
                    pst.setInt(1, rs.getInt(1));
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        obj = (Student) new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                    }
                }
                default -> {
                    obj = null;
                }
            }
        }
        rs.close();
        pst.close();
        return obj;
    }
}
