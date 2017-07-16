package ui.customer_ui.forms;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import dao.customer.Customer;
import ui.customer_ui.CustomerLayout;
import ui.validators.NumberValidator;

public abstract class CustomerForm extends FormLayout {

    protected Button save;
    protected Button cancel;

    protected TextField name;
    protected TextField secondName;
    protected TextField surname;
    protected TextField telephone;
    protected Customer customer;

    protected BeanFieldGroup<Customer> formFieldBindings;

    public CustomerForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        save = new Button("Save", this::save);
        cancel = new Button("Cancel", this::cancel);

        name = new TextField("Name");
        name.setNullRepresentation("");

        secondName = new TextField("Second Name");
        secondName.setNullRepresentation("");

        surname = new TextField("Surname");
        surname.setNullRepresentation("");

        telephone = new TextField("Phone");
        telephone.setNullRepresentation("");

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(name, secondName, surname, telephone, actions);
    }


    public void save(Button.ClickEvent event) {
        try {
            //validateFields phone number
            validateFields();

            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Save DAO
            propagateToDB(customer);

            String msg = String.format("Saved '%s %s'.", customer.getName(),
                    customer.getSecondName());
            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            CustomerLayout.getInstance().refreshCustomers();
            setVisible(false);
        } catch (FieldGroup.CommitException e) {
        }
    }

    private void validateFields() {
        name.addValidator(new NullValidator("Specify Name", false));
        secondName.addValidator(new NullValidator("Specify Second Name", false));
        surname.addValidator(new NullValidator("Specify Surname", false));
        telephone.addValidator(new NullValidator("Specify Phone", false));
        telephone.addValidator(new NumberValidator());
    }


    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION);
        CustomerLayout.getInstance().getCustomerList().select(null);
        setVisible(false);
    }

    public void edit(Customer customer) {

        this.customer = customer;
        if (customer != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(customer,
                    this);
            name.focus();
            CustomerLayout.getInstance().refreshCustomers();
        }
        setVisible(customer != null);
    }

    protected void propagateToDB(Customer customer) {
    }
}
