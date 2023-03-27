/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author Milos
 */
public class Director {

    public void buildAdminAccount(Builder builder) {
        builder.accountType('A');
    }

    public void buildInstructorAccount(Builder builder) {
        builder.accountType('I');
    }

    public void buildStudentAccount(Builder builder) {
        builder.accountType('S');
    }
}
