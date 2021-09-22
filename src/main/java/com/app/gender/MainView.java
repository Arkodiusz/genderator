package com.app.gender;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route("")
@PWA(name = "GenderDetector GUI", shortName = "Genderator")
public class MainView extends VerticalLayout {

    private Grid<TokenDto> gridTokens = new Grid<>();
    private boolean maleOnGrid;
    private Service service = new Service();
    TokenForm formToken = new TokenForm(this);

    public MainView() {
        VerticalLayout layTop = new VerticalLayout();
        Text title = new Text("GENDER DETECTOR");
        Text version = new Text("\nv0.2");
        layTop.add(title, version);

        HorizontalLayout layMid = new HorizontalLayout();
        TextField tfName = new TextField();
        tfName.setPlaceholder("NAME");
        Text response = new Text("");
        RadioButtonGroup<String> sMethod =
                new RadioButtonGroup<>();
        sMethod.setItems("compare first token", "compare all tokens");
        sMethod.setValue("compare first token");
        sMethod.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        Button buttonDetect = new Button("DETECT",
                e -> {
            int variant = 1;
            if (!sMethod.isEmpty()) {
                if (sMethod.getValue().equals("compare all tokens")) {
                    variant = 2;
                }
            }
            ResponseDto responseDto = service.detect(tfName.getValue(), variant);
            response.setText(responseDto.getDesignation()
                    + " m:" + responseDto.getMale() + " f:" + responseDto.getFemale()
                    + " i:" + responseDto.getInconclusive() + " %:" + responseDto.getPercentage());
        });
        layMid.add(tfName, sMethod, buttonDetect, response);

        VerticalLayout layBottom = new VerticalLayout();
        HorizontalLayout layBottomTop= new HorizontalLayout();
        VerticalLayout layBottomBottom= new VerticalLayout();

        formToken.setVisible(false);

        gridTokens.addColumn(TokenDto::getId).setHeader("ID");
        gridTokens.addColumn(TokenDto::getName).setHeader("NAME");
        gridTokens.addColumn(TokenDto::getGender).setHeader("GENDER");
        gridTokens.asSingleSelect().addValueChangeListener(e -> formToken.setContent(gridTokens.asSingleSelect().getValue(), "update"));
        Button buttonMaleTokens = new Button("MALE TOKENS",
                e -> showMaleTokens());
        Button buttonFemaleTokens = new Button("FEMALE TOKENS",
                e -> showFemaleTokens());
        Button buttonAddToken = new Button("ADD TOKEN",
                e -> {
            if (formToken.isVisible() && formToken.isModeSave()) {
                gridTokens.asSingleSelect().clear();
                formToken.setContent(null, "");
            } else {
                gridTokens.asSingleSelect().clear();
                formToken.setContent(new TokenDto(), "create");
            }
        });
        layBottomTop.add(buttonMaleTokens, buttonFemaleTokens, buttonAddToken);
        layBottomBottom.add(gridTokens, formToken);
        layBottom.add(layBottomTop, layBottomBottom);

        add(layTop, layMid, layBottom);
    }

    private void setMaleOnGrid(boolean bool) {
        maleOnGrid = bool;
    }

    public boolean isMaleOnGrid() {
        return maleOnGrid;
    }

    public void refresh() {
        if (isMaleOnGrid()) {
            showMaleTokens();
        } else {
            showFemaleTokens();
        }
    }

    private void showMaleTokens() {
        gridTokens.asSingleSelect().clear();
        gridTokens.setItems(service.listTokens("male"));
        formToken.setContent(null, "");
        setMaleOnGrid(true);
    }
    private void showFemaleTokens() {
        gridTokens.asSingleSelect().clear();
        gridTokens.setItems(service.listTokens("female"));
        formToken.setContent(null, "");
        setMaleOnGrid(false);
    }

}
