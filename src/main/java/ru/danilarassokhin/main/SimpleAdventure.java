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
        SimpleStoryLocation startLocation = simpleStory.addStoryLocation(1L);
        startLocation.setName("Castle");
        //Add location for your enemies
        SimpleStoryLocation basement = simpleStory.addStoryLocation(2L);
        basement.setName("Basement");
        basement.setEntryRestriction(() -> mainCharacter.getHealth() > 80);
    }

    public void addItems() {
        //Add some items, why not?
        SimpleStoryItem sword = simpleStory.addStoryItem(1L);
        sword.setName("Sword");
        sword.setAmount(1);
    }

    public void addQuests() {
        //May be some quest? Hero need to do hero things!
        SimpleStoryQuest enterBasement = simpleStory.addStoryQuest(1L);
        enterBasement.setName("Enter basement");
        enterBasement.setUnique(true);
        enterBasement.setCompleteCondition(
                        //When this quest will be completed?
                        () -> mainCharacter.getLocation().getId().equals(2L)
                );
        enterBasement.setOnComplete(
                        //What if quest will be completed?
                        (q) -> System.out.println("QUEST COMPLETED: " + q.getName())
                );

    }

    public void addCharacters() {
        //And your hero...
        mainCharacter = simpleStory.addStoryCharacter(1L).setHealth(80)
                .setName("Main Character")
                .setLocation(
                    simpleStory.getLocationById(1L)
                );
        //give him a goal...
        mainCharacter.addQuest(
                simpleStory.getQuestById(1L)
        );
        //and an item to achieve it
        mainCharacter.addItem(
                simpleStory.getItemById(1L)
        );
        //hero can punch zombies with actions
        //and action can have some parameters
        mainCharacter.<SimpleStoryCharacter>addAction("Punch", (e) -> e.addHealth(-10));
        mainCharacter.addAction("Open", (o) -> {});
        //Add some enemies
        enemyCharacter = simpleStory.addStoryCharacter(2L).setHealth(10).setLocation(
                simpleStory.getLocationById(2L)
        );
    }

    public void addNodes() {
        //And now scenery...
        //Greet your hero
        SimpleStoryNode greeting = simpleStory.addStoryNode(1L).setContent("Hi, stranger! What do you want?");
        startNode = greeting;
        SimpleStoryNode lowHp = simpleStory.addStoryNode(2L).setContent("Seem like you are injured. Need a help?");
        SimpleStoryNode metZombie = simpleStory.addStoryNode(3L).setContent("You see the zombie! What do you want to do?");
        SimpleStoryNode punchZombieResult = simpleStory.addStoryNode(4L).setContent("I killed the zombie!");

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
                    //You can pass params to action...
                    mainCharacter.action("Punch", enemyCharacter);
                    //... and make action without params
                    mainCharacter.action("Open");
                    System.out.println("ZOMBIE HAVE: " + enemyCharacter.getHealth() + "HP");
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
