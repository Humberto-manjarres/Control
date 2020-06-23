/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Humberto Manjarres
 */
public class Conexion implements Serializable{
    public Connection conectar() throws ClassNotFoundException, SQLException{
        Connection connection = null;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/control?useSSL=true", "root", "12345678");
        return connection;
    }
}
