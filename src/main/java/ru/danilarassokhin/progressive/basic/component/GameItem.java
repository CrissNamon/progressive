package ru.danilarassokhin.progressive.basic.component;

public class GameItem extends AbstractGameComponent{

    private String name;
    private float amount;

    public GameItem(Long id) {
        super(id);
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

}
