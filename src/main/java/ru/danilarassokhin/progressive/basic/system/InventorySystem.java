package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.RequiredScript;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.util.GameComponentInstantiator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredScript(ItemSystem.class)
public class InventorySystem extends AbstractGameScript {

    private final Map<Long, GameItem> items;

    public InventorySystem() {
        this.items = new HashMap<>();
    }

    public <I extends GameItem> I createItem(Class<I> item) {
        Long lastId = items.keySet().stream().max(Long::compareTo).orElse(0L);
        I instance = GameComponentInstantiator.instantiate(item, ++lastId);
        return instance;
    }

    public <I extends GameItem> boolean addItem(I item) {
        if(items.putIfAbsent(item.getId(), item) == null) {
            return true;
        }else {
            System.out.println("ITEM WITH ID " + item.getId() + " ALREADY EXISTS! SKIPPING...");
            return false;
        }
    }
    
    public boolean hasItem(Long id) {
        return items.containsKey(id);
    }

    public void removeItem(Long id) {
        items.remove(id);
    }

    public Set<GameItem> getItems() {
        return new HashSet<>(items.values());
    }
    
    public void addItem(Long id, float add) {
        GameItem item = items.getOrDefault(id, null);
        if(item != null) {
            item.setAmount(
                    item.getAmount() + add
            );
        }
    }
}
