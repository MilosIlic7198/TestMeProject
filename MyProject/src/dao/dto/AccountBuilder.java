/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public class AccountBuilder implements Builder {

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

    @Override
    public Builder accountId(int accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public Builder usernameEmail(String usernameEmail) {
        this.usernameEmail = usernameEmail;
        return this;
    }

    @Override
    public Builder password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public Builder accountType(char accountType) {
        this.accountType = accountType;
        return this;
    }

    @Override
    public Builder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Override
    public Builder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public Builder email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public Builder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public Builder godId(int godId) {
        this.godId = godId;
        return this;
    }

    @Override
    public Builder initials(String initials) {
        this.initials = initials;
        return this;
    }

    @Override
    public Builder birthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    @Override
    public Builder gender(char gender) {
        this.gender = gender;
        return this;
    }

    @Override
    public Builder adminId(int adminId) {
        this.adminId = adminId;
        return this;
    }

    @Override
    public Builder city(String city) {
        this.city = city;
        return this;
    }

    @Override
    public Builder street(String street) {
        this.street = street;
        return this;
    }

    @Override
    public Builder postalCode(int postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    @Override
    public Builder instructorId(int instructorId) {
        this.instructorId = instructorId;
        return this;
    }

    public Account build() {
        return new Account(
                accountId,
                usernameEmail,
                password,
                accountType,
                firstName,
                lastName,
                email,
                phoneNumber,
                godId,
                initials,
                birthDate,
                gender,
                adminId,
                city,
                street,
                postalCode,
                instructorId);
    }
}
