package dao.customer;

import org.apache.commons.beanutils.BeanUtils;

public class Customer implements java.io.Serializable {

    private static int count = 0;

    private int customerId;
    private String name;
    private String surname;
    private String secondName;
    private String telephone;

    public Customer() {
    }

    public Customer(String name, String surname, String secondName, String telephone) {
        this.name = name;
        this.surname = surname;
        this.secondName = secondName;
        this.telephone = telephone;
    }

    public Customer(int customerId, String name, String surname, String secondName, String telephone) {
        this.customerId = customerId;
        this.name = name;
        this.surname = surname;
        this.secondName = secondName;
        this.telephone = telephone;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (customerId != customer.customerId) return false;
        if (!name.equals(customer.name)) return false;
        if (!surname.equals(customer.surname)) return false;
        if (!secondName.equals(customer.secondName)) return false;
        return telephone.equals(customer.telephone);
    }

    @Override
    public int hashCode() {
        int result = customerId;
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + secondName.hashCode();
        result = 31 * result + telephone.hashCode();
        return result;
    }

    @Override
    public Customer clone() throws CloneNotSupportedException {
        try {
            return (Customer) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    public int getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return surname + ", phone: " + telephone;
    }
}