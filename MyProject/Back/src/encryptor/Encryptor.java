/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package encryptor;

/**
 *
 * @author Milos
 */
public class Encryptor {

    private final int key = 7;

    public String getEncryptedString(String pwd) {
        return encrypt(pwd);
    }

    public String getDecryptedString(String pwd) {
        return decrypt(pwd);
    }

    private String encrypt(String pwd) {
        String pass = new String();
        char[] chars = pwd.toCharArray();
        for (char c : chars) {
            c += key;
            pass += c;
        }
        return pass;
    }

    private String decrypt(String pwd) {
        String pass = new String();
        char[] chars = pwd.toCharArray();
        for (char c : chars) {
            c -= key;
            pass += c;
        }
        return pass;
    }
}
