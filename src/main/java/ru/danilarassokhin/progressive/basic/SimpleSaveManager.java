package ru.danilarassokhin.progressive.basic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.danilarassokhin.progressive.StorySaveManager;

public class SimpleSaveManager implements StorySaveManager<SimpleStory, String> {

    private static SimpleSaveManager INSTANCE;
    private Gson gson;

    private SimpleSaveManager() {
        gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    }

    public static SimpleSaveManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SimpleSaveManager();
        }
        return INSTANCE;
    }

    @Override
    public String save() {
        return gson.toJson(SimpleStory.getInstance());
    }

    @Override
    public SimpleStory load(String save) {
        return gson.fromJson(save, SimpleStory.class);
    }

    /**
     * Writes data from {@coded saved} to current Story instance
     * @param saved Saved story
     */
    public void toInstance(SimpleStory saved) {
        for(SimpleStoryNode find : SimpleStory.getInstance().getStoryNodes().values()) {
            if(find.getId().equals(saved.getCurrentNode().getId())) {
                SimpleStory.getInstance().setNext(find);
                break;
            }
        }
        for(SimpleStoryCharacter character : saved.getStoryCharacters().values()) {
            SimpleStoryCharacter exists = SimpleStory.getInstance().getCharacterById(character.getId());
            if(exists != null) {
                exists.setHealth(character.getHealth());
                if(character.getQuests() != null) {
                    for (SimpleStoryQuest q : character.getQuests()) {
                        SimpleStoryQuest qExists = SimpleStory.getInstance().getQuestById(q.getId());
                        if (qExists != null) {
                            exists.addQuest(q);
                        }
                    }
                }
                for(SimpleStoryItem item : character.getInventory().getItems()) {
                    SimpleStoryItem iExists = SimpleStory.getInstance().getItemById(item.getId());
                    if(iExists != null) {
                        exists.addItem(item);
                    }
                }
            }
        }
    }
}
