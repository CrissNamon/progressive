package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.FromParent;
import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredGameScript(ItemSystem.class)
public class InventorySystem extends AbstractGameScript {

    @FromParent
    private ItemSystem itemSystem;

    private final Map<Long, GameItem> items;

    public InventorySystem() {
        this.items = new HashMap<>();
    }

    public <I extends GameItem> I createItem(Class<I> item) {
        Long lastId = items.keySet().stream().max(Long::compareTo).orElse(0L);
        I instance = ComponentCreator.create(item, ++lastId);
        return instance;
    }

    public <I extends GameItem> boolean addItem(I item) {
        return items.putIfAbsent(item.getId(), item) == null;
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

    public ItemSystem getItemSystem() {
        return itemSystem;
    }
}
