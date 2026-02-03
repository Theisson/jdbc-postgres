package app;

import database.SchemaManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.Database;

public class Program {
    public static void main(String[] args) throws SQLException {
        try {
            SchemaManager.createTables();
            SchemaManager.seedData();

            Connection conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tb_products");
            while(rs.next()) {
                System.out.println(rs.getLong("id") + " - " + rs.getString("name") + " - " + rs.getBigDecimal("price"));
            }
        }
        finally {
            Database.closeConnection();
        }
    }
}