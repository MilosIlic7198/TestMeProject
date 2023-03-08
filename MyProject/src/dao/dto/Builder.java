/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.dto;

/**
 *
 * @author Milos
 */
public interface Builder {

    public Builder accountId(int accountId);

    public Builder usernameEmail(String usernameEmail);

    public Builder password(String password);

    public Builder accountType(char accountType);

    public Builder firstName(String firstName);

    public Builder lastName(String lastName);

    public Builder email(String email);

    public Builder phoneNumber(String phoneNumber);

    public Builder godId(int godId);

    public Builder initials(String initials);

    public Builder birthDate(String birthDate);

    public Builder gender(char gender);

    public Builder adminId(int adminId);

    public Builder city(String city);

    public Builder street(String street);

    public Builder postalCode(int postalCode);

    public Builder instructorId(int instructorId);
}
