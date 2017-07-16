package ui.order_ui.forms;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import dao.DaoManager;
import dao.customer.Customer;
import dao.order.Order;
import ui.order_ui.OrderLayout;
import ui.validators.DoubleValidator;

import java.util.ArrayList;
import java.util.List;


public abstract class OrderForm extends FormLayout {

    protected Button save;
    protected Button cancel;

    protected TextField description;

    protected DateField orderDate;
    protected DateField completionDate;
    protected TextField price;

    protected ComboBox customerBox;
    protected ComboBox statusBox;

    protected Order order;

    protected BeanFieldGroup<Order> formFieldBindings;

    public OrderForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        save = new Button("Save", this::save);
        cancel = new Button("Cancel", this::cancel);

        description = new TextField("Description");
        description.setInputPrompt("Description");
        description.setNullRepresentation("");

        orderDate = new DateField("Order Date");
        orderDate.setDateFormat("yyyy-MM-dd");

        completionDate = new DateField("Completion Date");
        completionDate.setDateFormat("yyyy-MM-dd");


        price = new TextField("Price");
        price.setInputPrompt("Price...");
        price.setNullRepresentation("");

        customerBox = new ComboBox();
        configureCustomerBox();

        statusBox = new ComboBox();
        configureStatusBox();

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {

        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(description, customerBox, orderDate, completionDate, price, statusBox, actions);
    }

    private void configureCustomerBox() {

        customerBox.setTextInputAllowed(true);
        customerBox.setInputPrompt("Input Surname...");
        customerBox.setNullSelectionAllowed(false);

        customerBox.addFocusListener(e -> {
            customerBox.removeAllItems();
            List<Customer> customers = DaoManager.getInstance().getCustomerDAO().getAll("");
            customerBox.addItems(customers);
        });

        customerBox.addBlurListener(e -> {
            Customer customer = (Customer) customerBox.getValue();

            if (customer != null) {
                order.setCustomerId(customer.getCustomerId());
                order.setCustomerSurname(customer.getSurname());
            }
        });

        customerBox.addValueChangeListener(e -> {
            Customer customer = (Customer) customerBox.getValue();

            if (customer != null) {
                order.setCustomerId(customer.getCustomerId());
                order.setCustomerSurname(customer.getSurname());
            }
        });

    }

    private void configureStatusBox() {

        statusBox.setTextInputAllowed(true);
        statusBox.setInputPrompt("Input Status...");
        statusBox.setNullSelectionAllowed(false);

        List<String> statuses = new ArrayList<String>();
        statuses.add("Planned");
        statuses.add("Completed");
        statuses.add("Accepted by Customer");
        statusBox.addItems(statuses);
        statusBox.setInputPrompt("Status...");


        statusBox.addBlurListener(e -> {
            String status = (String) statusBox.getValue();
            order.setStatus(status);
        });

        statusBox.addValueChangeListener(e -> {
            String status = (String) statusBox.getValue();

            if (status != null) {
                order.setStatus(status);
            }
        });


    }


    protected void save(Button.ClickEvent event) {
        try {
            if (validateFields()) {
                // Propagate CustomerId and customerSurname to order
                Customer customer = (Customer) customerBox.getValue();
                order.setCustomerId(customer.getCustomerId());
                order.setCustomerSurname(customer.getSurname());

                // Propagate STATUS to order
                order.setStatus((String) statusBox.getValue());

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
        customerBox.addValidator(new NullValidator("Specify Customer", false));
        orderDate.addValidator(new NullValidator("Specify Order Date", false));
        completionDate.addValidator(new NullValidator("Specify Completion Date", false));
        price.addValidator(new NullValidator("Specify Price", false));
        price.addValidator(new DoubleValidator());
        statusBox.addValidator(new NullValidator("Specify Status", false));

        if (description.getValue() == null |
                customerBox.getValue() == null |
                orderDate.getValue() == null |
                completionDate.getValue() == null |
                description.getValue() == null |
                statusBox.getValue() == null
                ) {
            String msg = String.format("Specify all attributes", customerBox.getValue());
            Notification.show(msg, Notification.Type.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    protected void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION);
        OrderLayout.getInstance().getOrderList().select(null);
        setVisible(false);
    }

    public void edit(Order order) {
        this.order = order;
        if (order != null) {
            // Bind the properties of the order POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(order,
                    this);
            description.focus();
        }
        setVisible(order != null);
    }

    protected void propagateToDB(Order order) {
    }
}