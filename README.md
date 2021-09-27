# Text engine
Text engine is a simple game library, which lets you to create text games.
<br>
Text engine contains:
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
+ StoryInventory
    + Represents game story character inventory
+ StoryItem
    + Represents game story item
    + Item can have a name and count
+ StoryQuest
    + Represents game story quest
    + Quest can have a name, complete condition and on completion action
    + Quest also can be one-time or reusable

`SimpleAdventure` contains example of simple game and library usage.