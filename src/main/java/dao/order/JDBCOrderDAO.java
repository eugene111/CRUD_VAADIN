package dao.order;

import dao.customer.Customer;
import dao.customer.JDBCCustomerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCOrderDAO {

    private Connection connection = null;

    public JDBCOrderDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Order order) {

        System.out.println(order.getCompletionDate().toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders " +
                    "(description, customer_id, customer_surname, order_date, completion_date, price, status) " +
                    "VALUES (" +
                    " '" + order.getDescription() +
                    "', '" + ((Integer) order.getCustomerId()) +
                    "', '" + (order.getCustomerSurname()) +
                    "', '" + java.sql.Date.valueOf(order.getOrderDate().toString()) +
                    "', '" + java.sql.Date.valueOf(order.getCompletionDate().toString()) +
                    "', '" + ((Double) order.getPrice()) +
                    "', '" + order.getStatus() +
                    "')");

            preparedStatement.executeUpdate();
            preparedStatement.close();
            //Set orderId
            order.setOrderId(getLastId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getLastId() {
        int id = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT TOP 1 Id FROM Orders ORDER BY Id DESC");

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

    public void update(Order order) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE orders SET " +
                    "description = '" + order.getDescription() + "', " +
                    "customer_id = '" + order.getCustomerId() + "', " +
                    "customer_surname = '" + order.getCustomerSurname() + "', " +
                    "order_date = '" + order.getOrderDate() + "', " +
                    "completion_date = '" + order.getCompletionDate() + "', " +
                    "price = '" + order.getPrice() + "', " +
                    "status = '" + order.getStatus() + "' " +
                    "where id = " + order.getOrderId() + ";"
            );

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Order order) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from orders " +
                    "where id = " + order.getOrderId() + ";"
            );

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public List<Order> getAll(String stringFilter) {
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM orders");

            Order Order = null;
            while (resultSet.next()) {
                Order = new Order();
                Order.setOrderId(Integer.parseInt(resultSet.getString("id")));
                Order.setDescription(resultSet.getString("description"));
                Order.setCustomerId(resultSet.getInt("customer_id"));
                Order.setCustomerSurname(resultSet.getString("customer_surname"));
                Order.setOrderDate(resultSet.getDate("order_date"));
                Order.setCompletionDate(resultSet.getDate("completion_date"));
                Order.setPrice(resultSet.getDouble("price"));
                Order.setStatus(resultSet.getString("status"));

                orders.add(Order);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filterout(orders, stringFilter);
    }

    private List<Order> filterout(List<Order> orders, String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (Order order : orders) {


            if (order.getDescription().toLowerCase().contains(stringFilter.toLowerCase())) {
                arrayList.add(order);
            }

            if (order.getCustomerSurname().toLowerCase().contains(stringFilter.toLowerCase())) {
                arrayList.add(order);
            }

            /*            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || order.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(order.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(JDBCOrderDAO.class.getName()).log(
                        Level.SEVERE, null, ex);
            }*/
        }
        Collections.sort(arrayList, new Comparator<Order>() {

            @Override
            public int compare(Order o1, Order o2) {
                return (int) (o1.getCustomerId() - o2.getCustomerId());
            }
        });
        return arrayList;
    }
}
