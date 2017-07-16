package ui.customer_ui.forms;

import dao.customer.Customer;
import dao.DaoManager;

public class SaveCustomerForm extends CustomerForm {

    protected void propagateToDB(Customer customer) {
        DaoManager.getInstance().getCustomerDAO().insert(customer);
    }
}