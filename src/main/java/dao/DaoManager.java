package dao;

import dao.customer.JDBCCustomerDAO;
import dao.order.JDBCOrderDAO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class DaoManager {

    private Connection connection = null;
    private String driver = "org.hsqldb.jdbc.JDBCDriver";
    private String url = "jdbc:hsqldb:mem:testdb";
    private String userName = "SA";
    private String password = "";
    private String tablesSQLStr = "src/main/resources/tables.sql";
    private String constraintSQLStr = "src/main/resources/constraint.sql";
    private String dataSQLStr = "src/main/resources/data.sql";

    private static DaoManager daoManager = null;
    private JDBCCustomerDAO customerDAO;
    private JDBCOrderDAO orderDAO;

    private DaoManager() {
        try {
            Class.forName(driver);
            if (connection == null)
                connection = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        insert(tablesSQLStr);
        insert(constraintSQLStr);
        insert(dataSQLStr);

        customerDAO = new JDBCCustomerDAO(connection);
        orderDAO = new JDBCOrderDAO(connection);
    }

    public static DaoManager getInstance() {
        if (daoManager == null) {
            daoManager = new DaoManager();
        }
        return daoManager;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JDBCCustomerDAO getCustomerDAO() {
        if (daoManager == null) {
            daoManager = new DaoManager();
        }

        return customerDAO;
    }

    public JDBCOrderDAO getOrderDAO() {
        if (daoManager == null) {
            daoManager = new DaoManager();
        }
        return orderDAO;
    }


    private void insert(String path) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                update(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update(String line) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            stmt.executeUpdate(line);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}