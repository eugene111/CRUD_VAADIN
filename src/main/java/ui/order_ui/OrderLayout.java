package ui.order_ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import dao.DaoManager;
import dao.order.Order;
import ui.order_ui.forms.EditOrderForm;
import ui.order_ui.forms.SaveOrderForm;

public class OrderLayout extends VerticalLayout {

    private Label label;
    private TextField orderFilter;
    private Grid orderList;
    private Button newOrder;
    private Button deleteOrder;
    private Button editOrder;
    private SaveOrderForm saveOrderForm;
    private EditOrderForm editOrderForm;

    private static OrderLayout instance;

    public static OrderLayout getInstance() {

        if (instance == null) {
            instance = new OrderLayout();
        }

        return instance;
    }

    private OrderLayout() {
        configureComponents();
        buildLayout();
    }


    private void configureComponents() {

        label = new Label("Orders");
        orderFilter = new TextField();
        orderList = new Grid();
        newOrder = new Button("New Order");
        deleteOrder = new Button("Delete Order");
        editOrder = new Button("Edit Order");
        saveOrderForm = new SaveOrderForm();
        editOrderForm = new EditOrderForm();

        newOrder.addClickListener(e -> {
                    saveOrderForm.edit(new Order());
                    refreshOrders();
                }
        );
        editOrder.addClickListener(e -> {

                    Order selectedOrder = (Order) orderList.getSelectedRow();
                    editOrderForm.edit(selectedOrder);

                    refreshOrders();
                }
        );

        deleteOrder.addClickListener(e -> {

                    Order selectedOrder = (Order) orderList.getSelectedRow();

                    if (selectedOrder != null) {
                        DaoManager.getInstance().getOrderDAO().delete(selectedOrder);

                        String msg = String.format("Deleted '%s %s'.", selectedOrder.getDescription(),
                                selectedOrder.getCompletionDate());

                        Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
                        refreshOrders();
                    }
                }
        );

        orderFilter.setInputPrompt("Filter orders by description or customer surname...");
        orderFilter.addTextChangeListener(e -> refreshOrders());
        orderFilter.addValueChangeListener(e -> refreshOrders());
        orderFilter.addBlurListener(e -> refreshOrders());


        orderList
                .setContainerDataSource(new BeanItemContainer<>(Order.class));
        orderList.setColumnOrder("description", "customerSurname", "orderDate", "completionDate", "status", "price");
        orderList.removeColumn("customerId");
        orderList.removeColumn("orderId");
        orderList.setSelectionMode(Grid.SelectionMode.SINGLE);
        /*orderList.addSelectionListener( e -> {});*/
    }

    private void buildLayout() {

        HorizontalLayout buttonsLayout = new HorizontalLayout(newOrder, editOrder, deleteOrder);

        this.addComponent(label);
        this.addComponent(orderFilter);
        this.addComponent(orderList);
        this.addComponent(buttonsLayout);
        this.addComponent(saveOrderForm);
        this.addComponent(editOrderForm);

        label.setWidth("100%");
        orderList.setWidth("100%");
        orderFilter.setWidth("100%");

        refreshOrders();
    }

    public void refreshOrders() {
        String stringFilter = orderFilter.getValue();
        orderList.setContainerDataSource(new BeanItemContainer<>(
                Order.class, DaoManager.getInstance().getOrderDAO().getAll(stringFilter)));

    }

    public Grid getOrderList() {
        return orderList;
    }
}