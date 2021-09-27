package ru.danilarassokhin;

import ru.danilarassokhin.tengine.StoryState;
import ru.danilarassokhin.tengine.basic.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Your story begins with... Story actually
        SimpleStory simpleStory = SimpleStory.getInstance();
        //Every story have each own state manager
        SimpleStoryStateManager simpleStoryStateManager = SimpleStoryStateManager.getInstance();
        //You can do actions on specified states
        simpleStoryStateManager.addAction(StoryState.NODE_TRANSITION_START, () -> System.out.println("Node transition start"));
        simpleStoryStateManager.addAction(StoryState.NODE_TRANSITION_END, () -> System.out.println("Node transition end"));
        simpleStoryStateManager.addAction(StoryState.LOCATION_MOVE_START, () -> System.out.println("START MOVE TO LOCATION"));
        simpleStoryStateManager.addAction(StoryState.LOCATION_MOVE_COMPLETE, () -> System.out.println("MOVE TO LOCATION COMPLETE"));

        //Now you need some locations
        SimpleStoryLocation castle = simpleStory.addStoryLocation(1L, "Castle");

        //You characters can have items
        SimpleStoryItem sword = simpleStory.addStoryItem(1L, "Sword", 1);
        //And then a character itself with start health and location
        SimpleStoryCharacter mainCharacter = simpleStory.addStoryCharacter(1L, "TextEngineCharacter",
                70, castle);
        //Give an item to your character
        //Be careful! Item will be cloned before adding. You can add same object to another character without collision
        mainCharacter.addItem(sword);

        //Need to move your characters? Add another location
        SimpleStoryLocation basement = simpleStory.addStoryLocation(2L, "Basement", () -> mainCharacter.getHealth() >= 80);

        //Your characters can have some quests
        SimpleStoryQuest simpleStoryQuest = simpleStory.addStoryQuest(1L, "First quest",
                //Quest must have complete condition
                () -> mainCharacter.getLocation().equals(basement),
                //What will happen if quest completed?
                (q) -> System.out.println("Completed quest: " + q.getName())

        );
        //Don't forget to add quest to your characters
        mainCharacter.addQuest(simpleStoryQuest);
        

        //And now the scenery begins
        SimpleStoryNode q1 = new SimpleStoryNode(1L, "Where you wanna go?");
        SimpleStoryNode q3 = new SimpleStoryNode(3L, mainCharacter.getName() + ", wanna health buff?");
        SimpleStoryNode q4 = new SimpleStoryNode(4L, "Well, ok. That's the end");
        //Wanna interact with your Story? Add some variants as answers to your StoryNodes
        SimpleStoryNodeAnswer q1_a1 = new SimpleStoryNodeAnswer("Basement!",
                //You can try to move your characters to other locations
                () -> mainCharacter.setLocation(basement,
                //They can move successfully...
                () -> {
                    System.out.println(mainCharacter.getName() + " entered basement location");
                    simpleStory.setNext(q4);
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
            simpleStory.setNext(q1);
        });
        SimpleStoryNodeAnswer q3_2 = new SimpleStoryNodeAnswer("No...", () -> simpleStory.setNext(q4));
        //Don't forget to connect your StoryNodes with your answers! It's important!
        q1.addAnswer(q1_a1);
        q3.addAnswer(q3_1, q3_2);

        //And here starts the game finally...
        //Start the game with you first StoryNode
        //YOUR GAME MUST START WITH begin() METHOD!
        SimpleStoryNode current = simpleStory.begin(q1);
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
