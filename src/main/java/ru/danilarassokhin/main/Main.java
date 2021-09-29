package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.exception.StoryException;
import ru.danilarassokhin.progressive.exception.StoryRequirementException;

public class Main {

    public static void main(String[] args) {
        SimpleAdventure simpleAdventure = new SimpleAdventure();
        try {
            simpleAdventure.startStory();
        } catch (StoryException | StoryRequirementException e) {
            System.out.println(e.getMessage());
        }
    }
}
