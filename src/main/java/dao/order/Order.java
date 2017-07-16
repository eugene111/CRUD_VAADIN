package dao.order;

import dao.customer.Customer;
import org.apache.commons.beanutils.BeanUtils;

import java.time.LocalDate;
import java.sql.Date;

public class Order {

    private int orderId;
    private String description;
    private int customerId;
    private String customerSurname;
    private Date orderDate;
    private Date completionDate;
    private Double price;
    private String status;

    public Order(String description, Customer customer, Date orderDate, Date completionDate, Double cost, String status) {
        this.description = description;
        this.customerId = customer.getCustomerId();
        this.orderDate = orderDate;
        this.completionDate = completionDate;
        this.price = cost;
        this.status = status;
    }

    public Order() {

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (customerId != order.customerId) return false;
        if (!description.equals(order.description)) return false;
        if (!orderDate.equals(order.orderDate)) return false;
        if (!completionDate.equals(order.completionDate)) return false;
        if (!price.equals(order.price)) return false;
        return status.equals(order.status);
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + description.hashCode();
        result = 31 * result + customerId;
        result = 31 * result + orderDate.hashCode();
        result = 31 * result + completionDate.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", description='" + description + '\'' +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", completionDate=" + completionDate +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public Order clone() throws CloneNotSupportedException {
        try {
            return (Order) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }
}