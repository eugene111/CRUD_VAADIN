package ui.order_ui.forms;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import dao.DaoManager;
import dao.customer.Customer;
import dao.order.Order;
import ui.order_ui.OrderLayout;
import ui.validators.DoubleValidator;

public class EditOrderForm extends OrderForm {
    protected void propagateToDB(Order order) {
        DaoManager.getInstance().getOrderDAO().update(order);
    }

    public void save(Button.ClickEvent event) {
        try {
            if (validateFields()) {
                // Propagate CustomerId and customerSurname to order
                //Customer customer = (Customer) customerBox.getValue();
                //order.setCustomerId(customer.getCustomerId());
                //order.setCustomerSurname(customer.getSurname());

                // Propagate STATUS to order
                //order.setStatus((String) statusBox.getValue());

                // Commit the fields from UI to DAO
                formFieldBindings.commit();

                // Propagate to DB
                propagateToDB(order);

                String msg = String.format("Saved '%s %s'.", order.getDescription(),
                        order.getCompletionDate());
                Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
                OrderLayout.getInstance().refreshOrders();
                setVisible(false);
            }
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }

        customerBox.clear();
        statusBox.clear();
    }


    protected boolean validateFields() {
        description.addValidator(new NullValidator("Specify Description", false));
        //customerBox.addValidator(new NullValidator("Specify Customer", false));
        orderDate.addValidator(new NullValidator("Specify Order Date", false));
        completionDate.addValidator(new NullValidator("Specify Completion Date", false));
        price.addValidator(new NullValidator("Specify Price", false));
        price.addValidator(new DoubleValidator());
        //statusBox.addValidator(new NullValidator("Specify Status", false));

        return true;
    }
}
