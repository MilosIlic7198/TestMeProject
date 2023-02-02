/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public class Admin {

    private int adminId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int godId;

    public Admin(int adminId, String firstName, String lastName, int godId) {
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.godId = godId;
    }

    public Admin(String firstName, String lastName, String email, String phoneNumber, int godId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.godId = godId;
    }

    public int getAdminId() {
        return adminId;
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
}
