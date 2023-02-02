/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import dao.dto.Admin;
import dao.dto.Instructor;
import dao.dto.Student;
import java.sql.SQLException;

/**
 *
 * @author Milos
 */
public interface AdminContract {

    void addAdmin(Admin a) throws SQLException;

    void addInstructor(Instructor i) throws SQLException;

    void addStudent(Student s) throws SQLException;
}
