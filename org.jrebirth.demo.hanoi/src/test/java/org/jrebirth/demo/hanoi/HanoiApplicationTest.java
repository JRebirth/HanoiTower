package org.jrebirth.demo.hanoi;

import java.util.concurrent.TimeUnit;

import javafx.scene.input.MouseButton;

import org.jrebirth.af.api.facade.LocalFacade;
import org.jrebirth.af.api.ui.Model;
import org.jrebirth.af.core.application.JRebirthApplicationTest;
import org.jrebirth.af.core.concurrent.JRebirthThread;
import org.jrebirth.demo.hanoi.ui.page.TowerModel;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

@Ignore
public class HanoiApplicationTest extends JRebirthApplicationTest<HanoiApplication> {

    @BeforeClass
    public static void startUp() throws Exception {
        ApplicationTest.launch(HanoiApplication.class);
    }

    @Test
    public void checkFullConf() {

        final HanoiApplication app = application;

        sleep(5, TimeUnit.SECONDS);

        final LocalFacade<Model> uF = JRebirthThread.getThread().getFacade().uiFacade();
        final TowerModel tm = uF.retrieve(TowerModel.class);

        clickOn("#solve", MouseButton.PRIMARY);
        sleep(5, TimeUnit.SECONDS);

        clickOn("#reset", MouseButton.PRIMARY);
        sleep(5, TimeUnit.SECONDS);

    }

}
