package dao.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCCustomerDAO {

    private Connection connection = null;

    public JDBCCustomerDAO(Connection connection) {
        this.connection = connection;
    }


    public void insert(Customer customer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customers " +
                    "(name, second_name, surname, telephone) " +
                    "VALUES (" +
                    " '" + customer.getName() +
                    "', '" + customer.getSecondName() +
                    "', '" + customer.getSurname() +
                    "', '" + customer.getTelephone() +
                    "')");

            preparedStatement.executeUpdate();
            preparedStatement.close();

            //Set customerId
            customer.setCustomerId(getLastId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getLastId() {
        int id = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customers ORDER BY ID DESC LIMIT 1");

            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public void update(Customer customer) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customers SET " +
                    "name = '" + customer.getName() + "', " +
                    "second_name = '" + customer.getSecondName() + "', " +
                    "surname = '" + customer.getSurname() + "', " +
                    "telephone = '" + customer.getTelephone() + "' " +
                    "where id = " + customer.getCustomerId() + ";"
            );

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Customer customer) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("delete from customers " +
                "where id = " + customer.getCustomerId() + ";"
        );

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<Customer> getAll(String stringFilter) {
        List<Customer> customers = new ArrayList<Customer>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customers");

            Customer Customer = null;
            while (resultSet.next()) {
                Customer = new Customer();
                Customer.setCustomerId(Integer.parseInt(resultSet.getString("id")));
                Customer.setName(resultSet.getString("name"));
                Customer.setSecondName(resultSet.getString("second_name"));
                Customer.setSurname(resultSet.getString("surname"));
                Customer.setTelephone(resultSet.getString("telephone"));

                customers.add(Customer);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filterout(customers, stringFilter);
    }

    private List<Customer> filterout(List<Customer> customers, String stringFilter) {
        List<Customer> arrayList = new ArrayList<Customer>();
        for (Customer customer : customers) {

            if (customer.getSurname().toLowerCase().contains(stringFilter.toLowerCase()) |
                    customer.getTelephone().toLowerCase().contains(stringFilter.toLowerCase())) {
                arrayList.add(customer);
            }


        }
        Collections.sort(arrayList, new Comparator<Customer>() {

            @Override
            public int compare(Customer o1, Customer o2) {
                return (int) (o1.getCustomerId() - o2.getCustomerId());
            }
        });
        return arrayList;
    }
}