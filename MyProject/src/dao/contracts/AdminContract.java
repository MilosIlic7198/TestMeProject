/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import dao.dto.Account;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public interface AdminContract {

    boolean checkUsername(String username);

    boolean checkForInstructorID(int instructorId);

    boolean checkForAdminID(int adminId);

    boolean addAccount(Account account);

    boolean editAccount(Account account);

    boolean godAdminEditAdmin(int adminId, int newAdminId);

    boolean deleteAdmin(int adminId, int IdOfAdminToDelete, String choice);

    boolean deleteInstructor(int instructorId);

    boolean deleteStudent(int studentId);

    ArrayList<Account> getAdminInstructors(int adminId);

    ArrayList<Account> getAllAdmins();

    ArrayList<Account> getAdminAccounts(int adminId);

    ArrayList<Account> getInstructorAccounts(int adminId);

    ArrayList<Account> getStudentAccounts(int adminId);
}
