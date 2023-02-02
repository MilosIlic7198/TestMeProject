/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public class Instructor {

    private String email;
    private String password;

    private int instructorId;
    private String firstName;
    private String lastName;
    private String initials;
    private String birthDate;
    private char gender;
    private String phoneNumber;
    private int adminId;

    public Instructor(int instructorId, String firstName, String lastName, String initials, int adminId) {
        this.instructorId = instructorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.initials = initials;
        this.adminId = adminId;
    }

    public Instructor(String email, String password, String firstName, String lastName, String initials, String birthDate, char gender, String phoneNumber, int adminId) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.initials = initials;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getInitials() {
        return initials;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public char getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getAdminId() {
        return adminId;
    }
}
