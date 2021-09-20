package com.app.gender;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    private Grid<String> tokensGrid = new Grid<>();

    public MainView() {
        // Use TextField for standard text input
        TextField textField = new TextField("NAME");

        // Button click listeners can be defined as lambda expressions
        Service service = new Service();

        Button buttonDetect = new Button("DETECT",
                e -> Notification.show(service.detect(textField.getValue())));

        Button buttonMaleTokens = new Button("MALE TOKENS",
                e -> {
                    tokensGrid.asSingleSelect().clear();
                    tokensGrid.setItems(service.listTokens("male"));
                });

        Button buttonFemaleTokens = new Button("FEMALE TOKENS",
                e -> {
                    tokensGrid.asSingleSelect().clear();
                    tokensGrid.setItems(service.listTokens("female"));
                });

        tokensGrid.addColumn(String::valueOf).setHeader("NAME");

//        Button buttonFemaleTokens = new Button("FEMALE TOKENS",
//                e -> Notification.listFemaleTokens("FEMALE");

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        buttonDetect.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        buttonDetect.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, buttonDetect, buttonMaleTokens, buttonFemaleTokens, tokensGrid);
    }
}

