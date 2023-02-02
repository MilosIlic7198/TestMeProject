/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public class Student {

    private int studentId;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private int postalCode;
    private String birthDate;
    private char gender;
    private int instructorId;
    private int adminId;

    public Student(int studentId, String firstName, String lastName, int instructorId) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.instructorId = instructorId;
    }

    public Student(int studentId, String firstName, String lastName, String city, String street, int postalCode, String birthDate, char gender) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public Student(String firstName, String lastName, String city, String street, int postalCode, String birthDate, char gender, int instructorId, int adminId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.birthDate = birthDate;
        this.gender = gender;
        this.instructorId = instructorId;
        this.adminId = adminId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public char getGender() {
        return gender;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public int getAdminId() {
        return adminId;
    }
}
