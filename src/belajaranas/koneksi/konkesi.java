/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajaranas.koneksi;

import java.sql.*;

/**
 *
 * @author user
 */
public class konkesi {
        public static String PathReport=System.getProperty("user.dir") + "/src/global/report/";
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            String dbUrl = "jdbc:mysql://localhost:3306/siakadbsg?user=root&password=";
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection : " + e.getMessage());
            }
        }
        return connection;
    }}
