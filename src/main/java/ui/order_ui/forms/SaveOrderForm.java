package ui.order_ui.forms;

import dao.DaoManager;
import dao.order.Order;

public class SaveOrderForm extends OrderForm {
    protected void propagateToDB(Order order) {
        DaoManager.getInstance().getOrderDAO().insert(order);
    }
}