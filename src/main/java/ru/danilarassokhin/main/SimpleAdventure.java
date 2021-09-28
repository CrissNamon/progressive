package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.data.StoryState;
import ru.danilarassokhin.progressive.basic.*;

import java.util.Scanner;

public class SimpleAdventure {

    private SimpleStory simpleStory;
    private SimpleStoryStateManager simpleStoryStateManager;
    private SimpleSaveManager simpleSaveManager;

    private SimpleStoryNode startNode;
    private SimpleStoryCharacter mainCharacter;
    private SimpleStoryCharacter enemyCharacter;

    public SimpleAdventure() {
        //Story begins with... a story actually
        simpleStory = SimpleStory.getInstance();
        //and with a state management
        simpleStoryStateManager = SimpleStoryStateManager.getInstance();
        //wanna save your game?
        simpleSaveManager = SimpleSaveManager.getInstance();
    }

    public void addStates() {
        //And states can do some actions on change
        simpleStoryStateManager.<SimpleStory>addAction(StoryState.INIT, (story) -> {
            //You can do some stuff after story initialization
            //It will be called one time on Story instance creation
        });
        //Actions have parameters with some cool stuff!
        simpleStoryStateManager.addAction(StoryState.NODE_TRANSITION_START, (oldNode) -> System.out.println("Node transition start"));
        //You can specify action param type...
        simpleStoryStateManager.<SimpleStoryNode>addAction(StoryState.NODE_TRANSITION_END, (newNode) -> System.out.println("Node transition end"));
        simpleStoryStateManager.addAction(StoryState.LOCATION_MOVE_START, (oldLocation) -> System.out.println("START MOVE TO LOCATION"));
        simpleStoryStateManager.addAction(StoryState.LOCATION_MOVE_COMPLETE, (newLocation) -> {
            //...or get an Object by default
            //You can try-with-resource (yup, some types are AutoClosable)
            try(SimpleStoryLocation l = (SimpleStoryLocation) newLocation) {
                System.out.println("MOVE TO LOCATION " + l.getName() + " complete");
            }catch (Exception e) {
                System.out.println("NOT A LOCATION!");
            }
            //...or you can use THE OBJECT CASTER
            SimpleObjectCaster simpleObjectCaster = new SimpleObjectCaster();
            //cast will return casted object and make action, you had specified!
            SimpleStoryLocation castedLocation = simpleObjectCaster.cast(newLocation, SimpleStoryLocation.class, (l) -> System.out.println("MOVE TO LOCATION " + l.getName() + " complete"));
        });
    }


    public void addLocations() {
        //Add start location for your hero with builder
        SimpleStoryLocation startLocation = new SimpleLocationBuilder(1L).setName("Castle").build();
        //All components must be registered in story!
        simpleStory.addStoryLocation(startLocation);
        //Add location for your enemies
        SimpleStoryLocation basement = new SimpleLocationBuilder(2L)
                .setName("Basement")
                //You can add entry restrictions to your location
                .setEntryRestriction(() -> simpleStory.getCharacterById(1L).getHealth() > 80)
                .build();
        simpleStory.addStoryLocation(basement);
    }

    public void addItems() {
        //Add some items, why not?
        SimpleStoryItem sword = new SimpleItemBuilder(1L).setName("Sword").setStartCount(1).build();
        simpleStory.addStoryItem(sword);
    }

    public void addQuests() {
        //May be some quest? Hero need to do hero things!
        SimpleStoryQuest enterBasement = new SimpleQuestBuilder(1L).setName("Enter basement").setUnique(true)
                .setCompleteCondition(
                        //When this quest will be completed?
                        () -> mainCharacter.getLocation().getId().equals(2L)
                )
                .setOnComplete(
                        //What if quest will be completed?
                        (q) -> System.out.println("QUEST COMPLETED: " + q.getName())
                ).build();
        simpleStory.addStoryQuest(enterBasement);
    }

    public void addCharacters() {
        //And your hero...
        mainCharacter = new SimpleCharacterBuilder(1L).setHealth(80).setName("Otis")
                //set your hero to start location...
                .setLocation(simpleStory.getLocationById(1L))
                .build();
        simpleStory.addStoryCharacter(mainCharacter);
        //give him a goal...
        mainCharacter.addQuest(
                simpleStory.getQuestById(1L)
        );
        //and an item to achieve it
        mainCharacter.addItem(
                simpleStory.getItemById(1L)
        );
        //hero can punch zombies
        mainCharacter.addAction("Punch", () -> enemyCharacter.addHealth(-10));
        //Add some enemies
        enemyCharacter = new SimpleCharacterBuilder(2L).setHealth(10).setName("Zombie")
                .setLocation(simpleStory.getLocationById(2L))
                .build();
        simpleStory.addStoryCharacter(enemyCharacter);
    }

    public void addNodes() {
        //And now scenery...
        //Greet your hero
        SimpleStoryNode greeting = new SimpleNodeBuilder(1L, "Hi, stranger! What do you want?").build();
        simpleStory.addStoryNode(greeting);
        startNode = greeting;
        SimpleStoryNode lowHp = new SimpleNodeBuilder(2L, "Seem like you are injured. Need a help?").build();
        simpleStory.addStoryNode(lowHp);
        SimpleStoryNode metZombie = new SimpleNodeBuilder(3L, "You see the zombie! What do you want to do?").build();
        simpleStory.addStoryNode(metZombie);
        SimpleStoryNode punchZombieResult = new SimpleNodeBuilder(4L, "I killed the zombie!").build();
        simpleStory.addStoryNode(punchZombieResult);

        //You can add answers to your scenery nodes
        SimpleStoryNodeAnswer greetingAnswer1 = new SimpleStoryNodeAnswer("Go to basement",
                //What if answer... answered?
                () -> simpleStory.getCharacterById(1L).setLocation(
                        simpleStory.getLocationById(2L),
                        //DON'T FORGET TO SET NEXT SCENERY NODE!
                        //of course if you don't want to play current node again
                        () -> simpleStory.setNext(metZombie),
                        () -> {
                            System.out.println("You have " + mainCharacter.getHealth() + "HP. Not enough for basement");
                            simpleStory.setNext(lowHp);
                        }
                )
        );
        //Don't forget to connect nodes with answers! It's important
        greeting.addAnswer(greetingAnswer1);

        SimpleStoryNodeAnswer lowHpAnswerYes = new SimpleStoryNodeAnswer("Yeah",
                () -> {
                    mainCharacter.addHealth(20);
                    System.out.println("Done! You now have "
                            + simpleStory.getCharacterById(1L).getHealth() + "HP");
                    simpleStory.setNext(greeting);
                });
        SimpleStoryNodeAnswer lowHpAnswerNo = new SimpleStoryNodeAnswer("No",
                () -> {
                    System.out.println("As you wish...");
                    simpleStory.setNext(greeting);
                }
        );
        lowHp.addAnswer(lowHpAnswerYes, lowHpAnswerNo);

        SimpleStoryNodeAnswer metZombiePunch = new SimpleStoryNodeAnswer("Punch zombie",
                () -> {
                    mainCharacter.action("Punch");
                    simpleStory.setNext(punchZombieResult);
                });
        metZombie.addAnswer(metZombiePunch);
    }

    public void startStory() {
        Scanner sc = new Scanner(System.in);

        addStates();
        addLocations();
        addItems();
        addQuests();
        addCharacters();
        addNodes();

        //AND NOW THE STORY BEGINS
        //Your story should always begin with begin! Even before loading saved game
        SimpleStoryNode current = simpleStory.begin(startNode);
        String save = "";
        //You can load your game
        if(save.length() > 0) {
            simpleSaveManager.toInstance(
                    simpleSaveManager.load(save)
            );
            current = simpleStory.getCurrentNode();
        }
        while(current != null) {
            System.out.println(current.getContent());
            if(!current.hasAnswers()) {
                break;
            }
            System.out.println();
            System.out.println("Choose your answer: ");
            for(int i = 0; i < current.getAnswers().size(); i++) {
                System.out.println((i+1) + ". " +
                        current.answer(i).getContent()
                );
            }
            int ans = sc.nextInt();
            //Get node answer
            SimpleStoryNodeAnswer answer = current.answer(ans - 1);
            //Confirm the answer
            answer.onAnswer();
            //Update the current node. You awesome! :)
            current = simpleStory.next();
        }
    }

}