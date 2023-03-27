/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author Milos
 */
public class Account implements Serializable {

    private int accountId;
    private String usernameEmail;
    private String password;
    private char accountType;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int godId;
    private String initials;
    private String birthDate;
    private char gender;
    private int adminId;
    private String city;
    private String street;
    private int postalCode;
    private int instructorId;

    public Account(
            int accountId,
            String usernameEmail,
            String password,
            char accountType,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            int godId,
            String initials,
            String birthDate,
            char gender,
            int adminId,
            String city,
            String street,
            int postalCode,
            int instructorId) {
        this.accountId = accountId;
        this.usernameEmail = usernameEmail;
        this.password = password;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.godId = godId;
        this.initials = initials;
        this.birthDate = birthDate;
        this.gender = gender;
        this.adminId = adminId;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.instructorId = instructorId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsernameEmail() {
        return usernameEmail;
    }

    public String getPassword() {
        return password;
    }

    public char getAccountType() {
        return accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getGodId() {
        return godId;
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

    public int getAdminId() {
        return adminId;
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

    public int getInstructorId() {
        return instructorId;
    }
}
