package ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import dao.DaoManager;
import ui.customer_ui.CustomerLayout;
import ui.order_ui.OrderLayout;

import javax.servlet.annotation.WebServlet;

@Title("Workshop")
@Theme("valo")
public class WorkshopUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        buildLayout();
    }

    private void buildLayout() {

        try {
            //Create tables, constraint, insert test data, initialize DAO
            DaoManager.getInstance();

            //UI
            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.addComponent(CustomerLayout.getInstance());
            mainLayout.addComponent(OrderLayout.getInstance());
            mainLayout.setSizeFull();

            setContent(mainLayout);
        } catch (Exception e) {
            DaoManager.getInstance().closeConnection();
            e.printStackTrace();
        }
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = WorkshopUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}