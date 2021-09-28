package ru.danilarassokhin;

import ru.danilarassokhin.tengine.StoryState;
import ru.danilarassokhin.tengine.basic.*;

import java.util.Scanner;

/**
 * Simple story example
 */
public class SimpleAdventure {

    /**
     * Your story
     */
    private final SimpleStory simpleStory;
    /**
     * State manager for story
     */
    private final SimpleStoryStateManager simpleStoryStateManager;

    /**
     * Location to start from
     */
    private SimpleStoryLocation startLocation;
    /**
     * Basement location
     */
    private SimpleStoryLocation basementLocation;

    /**
     * Main game character
     */
    private SimpleStoryCharacter mainCharacter;
    /**
     * Enemy character
     */
    private SimpleStoryCharacter enemyCharacter;

    /**
     * Sword item for character
     */
    private SimpleStoryItem sword;

    /**
     * Simple quest for character
     */
    private SimpleStoryQuest simpleStoryQuest;

    /**
     * Node to start with
     */
    private SimpleStoryNode startNode;

    /**
     * Scanner to get answers
     */
    private Scanner sc;

    /**
     * Create story
     */
    public SimpleAdventure() {
        //Your adventure begins with story
        simpleStory = SimpleStory.getInstance();
        //But story must have states
        simpleStoryStateManager = SimpleStoryStateManager.getInstance();
        sc = new Scanner(System.in);
    }

    /**
     * Add actions for game states
     */
    public void addStateActions() {
        //And states can do some actions on change
        simpleStoryStateManager.addAction(StoryState.NODE_TRANSITION_START, () -> System.out.println("Node transition start"));
        simpleStoryStateManager.addAction(StoryState.NODE_TRANSITION_END, () -> System.out.println("Node transition end"));
        simpleStoryStateManager.addAction(StoryState.LOCATION_MOVE_START, () -> System.out.println("START MOVE TO LOCATION"));
        simpleStoryStateManager.addAction(StoryState.LOCATION_MOVE_COMPLETE, () -> System.out.println("MOVE TO LOCATION COMPLETE"));
    }

    /**
     * Add locations
     */
    public void addLocations() {
        //Create location for your game
        startLocation = simpleStory.addStoryLocation(1L, "Castle");
    }

    /**
     * Add characters
     */
    public void addCharacters() {
        //Don't forget to add character with start health and location
        mainCharacter = simpleStory.addStoryCharacter(1L, "MainCharacter",
                70, startLocation);

        //Allow your character to fight...
        mainCharacter.addAction("Punch", () -> enemyCharacter.addHealth(-10));
        //...and give it an enemy
        enemyCharacter = simpleStory.addStoryCharacter(2L, "EnemyCharacter", 70, startLocation);
    }

    /**
     * Add items
     */
    public void addItems() {
        //Give a sword to your character
        sword = simpleStory.addStoryItem(1L, "Sword", 1);
    }

    /**
     * Add items
     */
    public void addQuests() {
        //May be some quests?
        simpleStoryQuest = simpleStory.addStoryQuest(1L, "First quest",
                //Quest must have complete condition
                () -> mainCharacter.getLocation().equals(basementLocation),
                //What will happen if quest completed?
                (q) -> System.out.println("Completed quest: " + q.getName())

        );
    }

    /**
     * Add game flow nodes
     */
    public void addNodes() {
        //Every event in story is a node. So, create one!
        startNode = simpleStory.addStoryNode(1L, "Where you wanna go?");
        //May be some more...
        SimpleStoryNode q3 = simpleStory.addStoryNode(2L, mainCharacter.getName() + ", wanna health buff?");
        SimpleStoryNode q4 = simpleStory.addStoryNode(3L, "Well, ok. That's the end");
        SimpleStoryNode fightEnemy = simpleStory.addStoryNode(4L, "Wanna punch the enemy? It has " + enemyCharacter.getHealth() + "HP now");
        SimpleStoryNode fightEnemyResult = simpleStory.addStoryNode(5L, "Great!");

        //Wanna interact with your Story? Add some variants as answers to your StoryNodes
        SimpleStoryNodeAnswer q1_a1 = new SimpleStoryNodeAnswer("Basement!",
                //You can try to move your characters to other locations
                () -> mainCharacter.setLocation(basementLocation,
                        //They can move successfully...
                        () -> {
                            System.out.println(mainCharacter.getName() + " entered basement location");
                            simpleStory.setNext(fightEnemy);
                        },
                        //Or face the restrictions :(
                        () -> {
                            System.out.println(mainCharacter.getName() + " have less than 80HP. Can't enter basement!");
                            simpleStory.setNext(q3);
                        })
                //Anyway, don't forget to set StoryNode to be the setNext node in game flow
        );
        //Add another answer! Why not?
        SimpleStoryNodeAnswer q3_1 = new SimpleStoryNodeAnswer("Yes!", () -> {
            //You can change you characters! Add some health may be?
            mainCharacter.addHealth(30);
            System.out.println("Ok. Whoosh! " + mainCharacter.getName()
                    + " now have " + mainCharacter.getHealth() + "HP!");
            simpleStory.setNext(startNode);
        });
        SimpleStoryNodeAnswer q3_2 = new SimpleStoryNodeAnswer("No...", () -> simpleStory.setNext(q4));

        //Punch the enemy!
        SimpleStoryNodeAnswer fightEnemyYes = new SimpleStoryNodeAnswer("Yes!", () -> {
            mainCharacter.action("Punch").make();
            System.out.println("Enemy now have " + enemyCharacter.getHealth() + " HP");
            simpleStory.setNext(fightEnemyResult);
        });

        //Don't forget to connect your StoryNodes with your answers! It's important!
        startNode.addAnswer(q1_a1);
        q3.addAnswer(q3_1, q3_2);
        fightEnemy.addAnswer(fightEnemyYes);
    }

    /**
     * Start your story
     */
    public void startStory() {
        addStateActions();
        addLocations();
        addCharacters();
        //Add new location
        basementLocation = simpleStory.addStoryLocation(2L, "Basement", () -> mainCharacter.getHealth() >= 80);
        addItems();
        addQuests();
        addNodes();

        //AND NOW YOUR STORY BEGINS!
        SimpleStoryNode current = simpleStory.begin(startNode);
        //Is this the end of adventure?
        while(current != null) {
            System.out.println(current.getContent());
            //No answers in current StoryNode? Well, your game is over...
            if(!current.hasAnswers()) {
                break;
            }
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
