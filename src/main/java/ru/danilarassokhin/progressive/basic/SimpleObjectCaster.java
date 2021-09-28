package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.lambdas.StoryActionParam;
import ru.danilarassokhin.progressive.utils.StoryObjectCaster;

public class SimpleObjectCaster implements StoryObjectCaster {
    @Override
    public <T, O> T cast(O from, Class<T> to, StoryActionParam<T> onSuccessCast) {
       try{
           T casted = to.cast(from);
           onSuccessCast.make(casted);
           return casted;
       }catch (ClassCastException e) {
           return null;
       }
    }
}
