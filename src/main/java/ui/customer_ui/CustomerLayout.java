package ui.customer_ui;

import com.vaadin.ui.*;
import com.vaadin.data.util.BeanItemContainer;
import dao.customer.Customer;
import dao.DaoManager;
import ui.customer_ui.forms.EditCustomerForm;
import ui.customer_ui.forms.SaveCustomerForm;

import java.sql.SQLException;

public class CustomerLayout extends VerticalLayout{

    private Label label = new Label("Customers");
    private TextField customerFilter = new TextField();
    private Grid customerList = new Grid();
    private Button newCustomer = new Button("New Customer");
    private Button deleteCustomer = new Button("Delete Customer");
    private Button editCustomer = new Button("Edit Customer");
    private SaveCustomerForm customerForm;
    private EditCustomerForm editCustomerForm;


    private static CustomerLayout instance = null;

    private CustomerLayout() {

        customerFilter = new TextField();
        customerList = new Grid();
        newCustomer = new Button("New Customer");
        customerForm = new SaveCustomerForm();
        editCustomerForm = new EditCustomerForm();

        configureComponents();
        buildLayout();
    }
    public static CustomerLayout getInstance() {
        if(instance == null) {
            instance = new CustomerLayout();
        }
        return instance;
    }

    private void configureComponents() {

        newCustomer.addClickListener(e -> {
                    customerForm.edit(new Customer());
                    refreshCustomers();
                }
        );

        editCustomer.addClickListener(e -> {
                    Customer selectedCustomer = (Customer) customerList.getSelectedRow();
                    editCustomerForm.edit(selectedCustomer);
                    refreshCustomers();
                }
        );

        deleteCustomer.addClickListener(e -> {

                    Customer selectedCustomer = (Customer) customerList.getSelectedRow();

                    if(selectedCustomer != null) {

                        try {
                            DaoManager.getInstance().getCustomerDAO().delete(selectedCustomer);

                            String msg = String.format("Deleted '%s %s'.", selectedCustomer.getName(),
                                    selectedCustomer.getSecondName());

                            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
                        } catch (SQLException e1) {
                            String msg = "Impossible to delete the customer while his order exists. Error message: " + e1.getMessage();

                            Notification.show(msg, Notification.Type.ERROR_MESSAGE);
                            e1.printStackTrace();
                        }
                        refreshCustomers();
                    }
                }
        );

        customerFilter.setInputPrompt("Filter customers by surname or telephone...");
        customerFilter.addTextChangeListener(e -> refreshCustomers());
        customerFilter.addValueChangeListener(e -> refreshCustomers());
        customerFilter.addBlurListener(e -> refreshCustomers());

        customerList
                .setContainerDataSource(new BeanItemContainer<>(Customer.class));
        customerList.setColumnOrder("name", "surname", "telephone");
        customerList.removeColumn("secondName");
        customerList.removeColumn("customerId");
        customerList.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void buildLayout() {

        HorizontalLayout buttonsLayout = new HorizontalLayout(newCustomer, editCustomer, deleteCustomer);

        this.addComponent(label);
        this.addComponent(customerFilter);
        this.addComponent(customerList);
        this.addComponent(buttonsLayout);
        this.addComponent(customerForm);
        this.addComponent(editCustomerForm);
        label.setWidth("100%");
        customerList.setWidth("100%");
        customerFilter.setWidth("100%");

        refreshCustomers();
    }

    public void refreshCustomers() {
        String stringFilter = customerFilter.getValue();
        customerList.setContainerDataSource(new BeanItemContainer<>(
                Customer.class, DaoManager.getInstance().getCustomerDAO().getAll(stringFilter)));
    }

    public Grid getCustomerList() {
        return customerList;
    }
}