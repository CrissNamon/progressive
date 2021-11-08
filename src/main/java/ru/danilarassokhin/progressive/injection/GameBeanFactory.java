package ru.danilarassokhin.progressive.injection;

import java.util.Map;
import ru.danilarassokhin.progressive.basic.injection.Bean;

public interface GameBeanFactory {

  Bean createBeanMetaInformationFromClass(Class<?> beanClass);

}
