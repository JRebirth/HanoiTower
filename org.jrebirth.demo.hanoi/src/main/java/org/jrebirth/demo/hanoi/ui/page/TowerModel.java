/**
 * Get more info at : www.jrebirth.org .
 * Copyright JRebirth.org © 2011-2013
 * Contact : sebastien.bordes@jrebirth.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jrebirth.demo.hanoi.ui.page;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jrebirth.af.api.annotation.Link;
import org.jrebirth.af.api.ui.annotation.RootNodeId;
import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.annotation.OnWave;
import org.jrebirth.af.core.ui.simple.DefaultSimpleModel;
import org.jrebirth.af.core.wave.WBuilder;
import org.jrebirth.demo.hanoi.bean.Disc;
import org.jrebirth.demo.hanoi.bean.HanoiGame;
import org.jrebirth.demo.hanoi.bean.HanoiTower;
import org.jrebirth.demo.hanoi.service.HanoiSolverService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PageModel.
 */
@RootNodeId("TowerPanel")
public class TowerModel extends DefaultSimpleModel<BorderPane> {

    private static final int DISC_HEIGHT = 20;
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory
                                                      .getLogger(TowerModel.class);
    private HanoiGame game;
    private Rectangle firstTower;
    private Rectangle secondTower;
    private Rectangle thirdTower;
    private List<Rectangle> discs;

    @Link
    private HanoiSolverService solver;
    private Button solveButton;
    private Button resetButton;
    private ChoiceBox<Integer> nbDiscChoice;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initModel() {

        buildGame(3);

    }

    /**
     * TODO To complete.
     * 
     * @return
     */
    private void buildGame(final int nbDisc) {
        this.game = HanoiGame.of().nbDisc(nbDisc).first(HanoiTower.of())
                             .second(HanoiTower.of())
                             .third(HanoiTower.of());

        for (int i = nbDisc; i > 0; i--) {
            this.game.first().addStack(Disc.of().size(i));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initSimpleView() {
        super.initSimpleView();

        final Pane p = new Pane();
        node().setCenter(p);

        final Color[] colors = new Color[] {
                Color.BROWN,
                Color.ORANGE,
                Color.BISQUE,
                Color.BLANCHEDALMOND,
                Color.BURLYWOOD,
                Color.CHARTREUSE,
                Color.CHOCOLATE,
                Color.ANTIQUEWHITE,
        };
        this.discs = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {

            final Rectangle r = new Rectangle(i * 20 + 10, DISC_HEIGHT);
            r.setFill(colors[i - 1]);
            this.discs.add(r);

        }

        final Rectangle base = new Rectangle(800, 20);
        base.setFill(Color.GRAY);
        base.setLayoutX(0);
        base.setLayoutY(260);

        this.firstTower = new Rectangle(10, 200);
        this.firstTower.setFill(Color.GRAY);
        this.firstTower.setLayoutX(200);
        this.firstTower.setLayoutY(80);

        this.secondTower = new Rectangle(10, 200);
        this.secondTower.setFill(Color.GRAY);
        this.secondTower.setLayoutX(400);
        this.secondTower.setLayoutY(80);

        this.thirdTower = new Rectangle(10, 200);
        this.thirdTower.setFill(Color.GRAY);
        this.thirdTower.setLayoutX(600);
        this.thirdTower.setLayoutY(80);

        p.getChildren().addAll(base, this.firstTower, this.secondTower, this.thirdTower);

        this.discs.stream().forEach(p.getChildren()::add);

        final HBox box = new HBox();

        final Label lb = new Label("Disc count");

        final Integer[] choices = new Integer[] { 3, 4, 5, 6, 7, 8 };
        this.nbDiscChoice = new ChoiceBox<>(FXCollections.observableArrayList(choices));
        this.nbDiscChoice.setId("discChoice");
        this.nbDiscChoice.getSelectionModel().select(0);
        this.nbDiscChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            // if the item of the list is changed
            @Override
            public void changed(final ObservableValue<? extends Number> ov, final Number value, final Number new_value) {
                updateDisc(choices[new_value.intValue()]);
            }
        });

        this.solveButton = new Button("Solve Me!");
        this.solveButton.setId("solve");
        this.solveButton.setOnAction(this::solve);

        this.resetButton = new Button("Reset");
        this.resetButton.setId("reset");
        this.resetButton.setOnAction(this::reset);

        box.getChildren().addAll(lb, this.nbDiscChoice, this.solveButton, this.resetButton);
        node().setBottom(box);
    }

    @Override
    protected void showView() {
        refreshGame();
    }

    private void refreshGame() {

        redrawStack(this.firstTower, this.game.first().stack());
        redrawStack(this.secondTower, this.game.second().stack());
        redrawStack(this.thirdTower, this.game.third().stack());

    }

    private void redrawStack(final Rectangle tower, final List<Disc> stack) {

        int step = 0;
        for (final Disc d : stack) {
            final Rectangle rd = getDisc(d);
            rd.setLayoutX(tower.getLayoutX() - rd.getWidth() / 2 + 5);
            rd.setLayoutY(tower.getLayoutY() + tower.getHeight() - 40 - step);
            step += DISC_HEIGHT;
        }

    }

    private Rectangle getDisc(final Disc d) {
        return this.discs.get(d.size() - 1);
    }

    private void updateDisc(final int nbDisc) {
        this.discs.stream().forEach(d -> {
            d.setLayoutX(-200);
            d.setLayoutY(-200);
        });

        buildGame(nbDisc);
        refreshGame();
    }

    private void solve(final ActionEvent event) {
        disableButtons(true);
        returnData(HanoiSolverService.class, HanoiSolverService.DO_SOLVE_TOWER,
                   WBuilder.waveData(HanoiSolverService.GAME_ITEM, this.game),
                   WBuilder.waveData(HanoiSolverService.SOLVE_DIRECTION, HanoiSolverService.SolveDirection.firstToThird));
    }

    private void reset(final ActionEvent event) {
        disableButtons(true);
        returnData(HanoiSolverService.class, HanoiSolverService.DO_SOLVE_TOWER,
                   WBuilder.waveData(HanoiSolverService.GAME_ITEM, this.game),
                   WBuilder.waveData(HanoiSolverService.SOLVE_DIRECTION, HanoiSolverService.SolveDirection.thirdToFirst));
    }

    private void disableButtons(final boolean disable) {
        this.nbDiscChoice.setDisable(disable);
        this.solveButton.setDisable(disable);
        this.resetButton.setDisable(disable);
    }

    @OnWave(HanoiSolverService.TOWER_REFRESHED)
    public void refreshed(final Wave wave) {
        refreshGame();
    }

    @OnWave(HanoiSolverService.TOWER_SOLVED)
    public void solved(final Wave wave) {
        refreshGame();
        disableButtons(false);
    }
}
