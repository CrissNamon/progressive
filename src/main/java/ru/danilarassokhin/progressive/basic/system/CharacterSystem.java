package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.annotation.FromParent;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.basic.component.GameQuest;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.lambda.GameActionObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredGameScript(value = {EchoSystem.class})
@IsGameScript
public class CharacterSystem implements GameScript {

    @FromParent
    private EchoSystem echoSystem;

    private GameObject parent;

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

    @Override
    public GameObject gameObject() {
        return parent;
    }

    @Override
    public void setGameObject(GameObject parent) {
        this.parent = parent;
    }

    private void start() {
        echoSystem.say("Hello");
        System.out.println(BasicDIContainer.getInstance().getBean("gameItem", GameItem.class).getId());
        System.out.println(BasicDIContainer.getInstance().getBean("gameItem", GameItem.class).getId());
    }

    private void update() {

    }
}
