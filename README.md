# Progressive
Progressive is a simple game library, which lets you to create interactive fiction games such as novels,
text quests and much more! Progressive is like a constructor. You can add needed systems to your story and use their components. Don't need quests? Don't add them! 
Progressive gives you an abstraction level to let you create any systems and components you need. It also has ready-made implementation for interactive fiction games. And there will be much more soon! 
<br>
<br>
Progressive have:
+ Story
    + Represents game story
    + Controls game nodes, items, characters and etc
+ StoryStateManager
    + Represents game manager
    + Manager can have states
    + Manager can execute action on state change and gives you some data about states
+ StoryNode
    + Represents game story node
    + Node can have content, text for example
+ StoryNodeAnswer
    + Represents game story node answer
    + Answers can have content, text for example
    + Answers can have actions to do on... answer
+ StoryComponent
    + Represents some story component such as character or location
+ StorySystem
    + Represents some story system such as character system
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
+ Some utils and actions to make your development simpler and games more functional

Look how is simple to create games with Progressive in [wiki](https://github.com/CrissNamon/progressive/wiki), 
or look at `SimpleAdventure` class for example