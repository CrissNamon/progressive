package ru.danilarassokhin.progressive.basic.component;

import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.component.GameComponent;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

@GameBean(name = "gameItem", policy = GameBeanCreationPolicy.OBJECT)
public class GameItem implements GameComponent {

    private String name;
    private float amount;
    private Long id;

    @Autofill(value = "globalIdGenerator")
    private GlobalIdGenerator idGenerator;

    public GameItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Autofill
    public void setId() {
        this.id = idGenerator.next();
    }

    @Override
    public Long getId() {
        return id;
    }
}
