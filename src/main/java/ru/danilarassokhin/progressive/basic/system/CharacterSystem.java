package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.Parentwired;
import ru.danilarassokhin.progressive.annotation.RequiredScript;
import ru.danilarassokhin.progressive.basic.component.GameQuest;
import ru.danilarassokhin.progressive.lambda.GameActionObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredScript({InventorySystem.class, LocationSystem.class, QuestSystem.class})
public class CharacterSystem extends AbstractGameScript {

    @Parentwired
    private InventorySystem inventorySystem;

    @Parentwired
    private LocationSystem locationSystem;

    @Parentwired
    private QuestSystem questSystem;

    private String name;
    private float health;
    private transient final Set<? extends GameQuest> quests;
    private transient final Map<String, GameActionObject> actions;

    public CharacterSystem() {
        this.quests = new HashSet<>();
        this.actions = new HashMap<>();
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void addHealth(float toAdd) {
        this.health += toAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
