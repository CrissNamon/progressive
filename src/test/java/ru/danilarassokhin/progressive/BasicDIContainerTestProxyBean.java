package ru.danilarassokhin.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicDIContainer;

public class BasicDIContainerTestProxyBean {

    @Test
    public void getNotExistingBeanByClass() {
        Assertions.assertThrows(RuntimeException.class,
                () -> BasicDIContainer.getInstance().getBean(BasicGame.class)
        );
    }

    @Test
    public void getNotExistingBeanByNameAndClass() {
        Assertions.assertThrows(RuntimeException.class,
                () -> BasicDIContainer.getInstance().getBean("TestBeanNotExists", Long.class)
        );
    }

    @Test
    public void createBean() {
        BasicDIContainer.getInstance().loadConfiguration(TestConfiguration.class);
        Assertions.assertDoesNotThrow(() -> BasicDIContainer.getInstance().getBean("TestBean", Long.class));
    }

}
