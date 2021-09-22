package com.app.gender;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;


@Route("")
@PWA(name = "GenderDetector GUI", shortName = "Genderator")
@CssImport("./styles/shared-styles.css")
//@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    public MainView() {

        Service service = new Service();

        VerticalLayout top = new VerticalLayout();

        Text title = new Text("GENDER DETECTOR");
        Text version = new Text("V0.1");

        top.setAlignItems(Alignment.CENTER);
        top.add(title, version);


        HorizontalLayout mid = new HorizontalLayout();

        TextField nameTextField = new TextField();
        nameTextField.setPlaceholder("NAME");
        Text response = new Text("");

        Button detectButton = new Button("DETECT",
                e -> response.setText(service.detect(nameTextField.getValue())));
        detectButton.addClickShortcut(Key.ENTER);

        mid.setAlignItems(Alignment.CENTER);
        mid.add(nameTextField, detectButton, response);


        VerticalLayout bottom = new VerticalLayout();
        HorizontalLayout bottom_top= new HorizontalLayout();
        VerticalLayout bottom_bottom= new VerticalLayout();

        TokenForm tokenForm = new TokenForm(this);

        Grid<TokenDto> tokensGrid = new Grid<>();
        tokensGrid.addColumn(TokenDto::getId).setHeader("ID");
        tokensGrid.addColumn(TokenDto::getName).setHeader("NAME");
        tokensGrid.addColumn(TokenDto::getGender).setHeader("GENDER");

        tokensGrid.asSingleSelect().addValueChangeListener(e -> tokenForm.setContent(tokensGrid.asSingleSelect().getValue(), "update"));

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
        Button buttonAddToken = new Button("ADD TOKEN",
                e -> {
            tokensGrid.asSingleSelect().clear();
            tokenForm.setContent(new TokenDto(), "create");
        });


        bottom_top.add(buttonMaleTokens, buttonFemaleTokens, buttonAddToken);
        bottom_bottom.add(tokensGrid, tokenForm);
        bottom.add(bottom_top, bottom_bottom);

        add(top, mid, bottom);
    }
}

