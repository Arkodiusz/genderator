package com.app.gender;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.io.IOException;

public class TokenForm extends VerticalLayout {

    private Binder<TokenDto> binder = new Binder<>(TokenDto.class);

    private Service service = Service.getInstance();

    private MainView mainView;

    private Text title = new Text("CREATE NEW TOKEN");

    private TextField name = new TextField("Name");
    private ComboBox<String> genderBox = new ComboBox("Gender");
    private TextField gender = new TextField("");

    private Button save = new Button("Save");

    private Button update = new Button("Update");
    private Button delete = new Button("Delete");

    public TokenForm(MainView mainView) {

        this.mainView = mainView;

        save.addClickListener(event -> {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        update.addClickListener(event -> {
            try {
                update();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delete.addClickListener(event -> delete());

        genderBox.setItems("f", "m");

        add(title, name, genderBox, save, update, delete);

        binder.bindInstanceFields(this);
    }

    private void save() throws IOException {
        gender.setValue(genderBox.getValue());
        service.save(binder.getBean());
        setContent(null, "");
        mainView.refresh();
    }

    private void delete() {
        service.delete(binder.getBean().getId());
        setContent(null, "");
        mainView.refresh();
    }

    private void update() throws IOException {
        gender.setValue(genderBox.getValue());
        service.update(binder.getBean());
        setContent(null, "");
        mainView.refresh();
    }

    public void setContent(TokenDto content, String mode) {
        if (genderBox.getValue() != null) gender.setValue(genderBox.getValue());
        binder.setBean(content);

        if (content != null) {
            switch(mode) {
                case "create":
                    gender.setValue(mainView.isMaleOnGrid() ? "m" : "f");
                    genderBox.setValue(gender.getValue());
                    title.setText("CREATE NEW TOKEN");
                    update.setVisible(false);
                    delete.setVisible(false);
                    save.setVisible(true);
                    setVisible(true);
                    name.focus();
                    return;
                case "update":
                    genderBox.setValue(gender.getValue());
                    title.setText("EDIT TOKEN");
                    save.setVisible(false);
                    update.setVisible(true);
                    delete.setVisible(true);
                    setVisible(true);
                    return;
                default: break;
            }
        }
        setVisible(false);
    }

    public boolean isModeSave() {
        return save.isVisible();
    }
}
