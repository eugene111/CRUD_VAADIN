package ui.customer_ui.forms;

import dao.customer.Customer;
import dao.DaoManager;

public class EditCustomerForm extends CustomerForm {

    protected void propagateToDB(Customer customer) {
        DaoManager.getInstance().getCustomerDAO().update(customer);
    }
}