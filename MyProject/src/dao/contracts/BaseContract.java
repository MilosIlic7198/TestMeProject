/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.contracts;

import java.sql.SQLException;

/**
 *
 * @author Milos
 */
public interface BaseContract<Object> {

    Object getUser(String loginDetails, String password) throws SQLException;
}
