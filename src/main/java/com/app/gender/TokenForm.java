package com.app.gender;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.io.IOException;

        import com.vaadin.flow.component.Text;
        import com.vaadin.flow.component.button.Button;
        import com.vaadin.flow.component.orderedlayout.VerticalLayout;
        import com.vaadin.flow.component.textfield.TextField;
        import com.vaadin.flow.data.binder.Binder;

        import java.io.IOException;

public class TokenForm extends VerticalLayout {

    private Binder<TokenDto> binder = new Binder<>(TokenDto.class);

    private Service service = Service.getInstance();

    private MainView mainView;

    private Text title = new Text("CREATE NEW ROCK");

    private TextField name = new TextField("Name");
    private TextField region = new TextField("Region");

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

        delete.addClickListener(event -> {
            delete();
        });

        add(title, name, region, save, update, delete);

        binder.bindInstanceFields(this);
    }

    private void save() throws IOException {
        service.save(binder.getBean());
        setContent(null, "");
    }

    private void delete() {
        service.delete(binder.getBean().getId());
        setContent(null, "");
    }

    private void update() throws IOException {
        service.update(binder.getBean());
        setContent(null, "");
    }

    public void setContent(TokenDto content, String mode) {
        binder.setBean(content);

        if (content != null) {
            switch(mode) {
                case "create":
                    title.setText("CREATE NEW TOKEN");
                    update.setVisible(false);
                    delete.setVisible(false);
                    save.setVisible(true);
                    setVisible(true);
                    name.focus();
                    return;
                case "update":
                    title.setText("EDIT TOKEN");
                    save.setVisible(false);
                    update.setVisible(true);
                    delete.setVisible(true);
                    setVisible(true);
                    return;
                default: break;
            }
        }
        //setVisible(false);
    }
}
