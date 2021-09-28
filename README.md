# Progressive
Progressive is a simple game library, which lets you to create interactive fiction games.
<br>
Progressive have:
+ Story
    + Represents game story
    + Controls game nodes, items, characters and etc
+ StoryStateManager
    + Represents game manager
    + Manager can have states
    + Manager can execute action on state change
+ StoryNode
    + Represents game story node
    + Node can have content, text for example
+ StoryNodeAnswer
    + Represents game story node answer
    + Answers can have content, text for example
    + Answers can have actions to do on... answer
+ StoryLocation
    + Represents game story location
    + Location can have a name and entering condition
+ StoryCharacter
    + Represents game story character
    + Story character can have a name, health, items and quests
    + They can also have some actions like punch or magic for example
+ StoryInventory
    + Represents game story character inventory
+ StoryItem
    + Represents game story item
    + Item can have a name and count
+ StoryQuest
    + Represents game story quest
    + Quest can have a name, complete condition and on completion action
    + Quest also can be one-time or reusable
+ StorySaveManager
    + You can save and load your game's instance as you wish
    + SimpleSaveManager contains example with json saves 

Look how is simple to create games with Progressive below, 
or see `SimpleAdventure` class for example
<br>
<br>
First you need to create... a story!
````java 
public SimpleAdventure() {
    //Story begins with... a story actually
    simpleStory = SimpleStory.getInstance();
    //and with a state management
    simpleStoryStateManager = SimpleStoryStateManager.getInstance();
    //wanna save your game?
    simpleSaveManager = SimpleSaveManager.getInstance();
}
````
Then you can add some actions to run on game states
````java
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
        System.out.println("NOT AN ITEM!");
    }
    //...or you can use THE OBJECT CASTER
    SimpleObjectCaster simpleObjectCaster = new SimpleObjectCaster();
    //cast will return casted object and make action, you had specified!
    SimpleStoryLocation castedLocation = simpleObjectCaster.cast(newLocation, SimpleStoryLocation.class, (l) -> System.out.println("MOVE TO LOCATION " + l.getName() + " complete"));
});
````
Then fill your game with content
````java
    public void addLocations() {
        //Add start location for your hero
        SimpleStoryLocation startLocation = simpleStory.addStoryLocation(1L, "Castle");
        //Add location for your enemies
        SimpleStoryLocation basement = simpleStory.addStoryLocation(2L, "Basement");
        //You can add entry restrictions to your location
        basement.setEntryRestriction(() -> simpleStory.getCharacterById(1L).getHealth() > 80);
    }
    public void addItems() {
        //Add some items, why not?
        SimpleStoryItem sword = simpleStory.addStoryItem(1L, "Sword", 1);
    }

    public void addQuests() {
        //May be some quest? Hero need to do hero things!
        SimpleStoryQuest enterBasement = simpleStory.addStoryQuest(1L, "Enter basement", true,
                //When this quest will be completed?
                () -> mainCharacter.getLocation().getId().equals(2L),
                //What if quest will be completed?
                (q) -> System.out.println("QUEST COMPLETED: " + q.getName())
        );
    }

    public void addCharacters() {
        //And your hero...
        mainCharacter = simpleStory.addStoryCharacter(1L, "Otis", 80);
        //set your hero to start location...
        mainCharacter.setLocation(
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
        //hero can punch zombies
        mainCharacter.addAction("Punch", () -> enemyCharacter.addHealth(-10));
        //Add some enemies
        enemyCharacter = simpleStory.addStoryCharacter(2L, "Zombie", 10);
        //and set them to locations
        enemyCharacter.setLocation(
                simpleStory.getLocationById(2L)
        );
    }
````
Add game scenery
````java
public void addNodes() {
        //And now scenery...
        //Greet your hero
        SimpleStoryNode greeting = simpleStory.addStoryNode(1L, "Hi, stranger! What do you want?");
        startNode = greeting;
        SimpleStoryNode lowHp = simpleStory.addStoryNode(2L, "Seem like you are injured. Need a help?");
        SimpleStoryNode metZombie = simpleStory.addStoryNode(3L, "You see the zombie! What do you want to do?");
        SimpleStoryNode punchZombieResult = simpleStory.addStoryNode(4L, "I killed the zombie!");

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
````
Start your story
````java
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
````
The result
````
START MOVE TO LOCATION
START MOVE TO LOCATION
Hi, stranger! What do you want?

Choose your answer: 
1. Go to basement
1
START MOVE TO LOCATION
You have 80.0HP. Not enough for basement
Node transition start
Node transition end
Seem like you are injured. Need a help?

Choose your answer: 
1. Yeah
2. No
1
Done! You now have 100.0HP
Node transition start
Node transition end
Hi, stranger! What do you want?

Choose your answer: 
1. Go to basement
1
START MOVE TO LOCATION
Node transition start
MOVE TO LOCATION Basement complete
MOVE TO LOCATION Basement complete
QUEST COMPLETED: Enter basement
Node transition end
You see the zombie! What do you want to do?

Choose your answer: 
1. Punch zombie
1
Node transition start
Node transition end
I killed the zombie!
````