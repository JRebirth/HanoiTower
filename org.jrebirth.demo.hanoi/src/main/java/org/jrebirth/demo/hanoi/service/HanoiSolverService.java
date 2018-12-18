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
package org.jrebirth.demo.hanoi.service;

import org.jrebirth.af.api.annotation.PriorityLevel;
import org.jrebirth.af.api.concurrent.Priority;
import org.jrebirth.af.api.module.RegistrationPoint;
import org.jrebirth.af.api.service.Service;
import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.annotation.OnWave;
import org.jrebirth.af.api.wave.contract.WaveItem;
import org.jrebirth.af.api.wave.contract.WaveType;
import org.jrebirth.af.core.wave.JRebirthItems;
import org.jrebirth.af.core.wave.WBuilder;
import org.jrebirth.af.core.wave.WaveItemBase;
import org.jrebirth.demo.hanoi.bean.HanoiGame;

@RegistrationPoint(exclusive = true)
public interface HanoiSolverService extends Service {

    enum SolveDirection {
        firstToThird, thirdToFirst
    }

    WaveItem<HanoiGame> GAME_ITEM = new WaveItemBase<HanoiGame>() {
    };

    WaveItem<SolveDirection> SOLVE_DIRECTION = new WaveItemBase<SolveDirection>() {
    };

    String SOLVE_TOWER = "SOLVE_TOWER";

    String TOWER_SOLVED = "TOWER_SOLVED";

    String TOWER_REFRESHED = "TOWER_REFRESHED";

    WaveType DO_SOLVE_TOWER = WBuilder.waveType(SOLVE_TOWER)
                                      .items(GAME_ITEM, SOLVE_DIRECTION)
                                      .returnAction(TOWER_SOLVED)
                                      .returnItem(JRebirthItems.voidItem);

    WaveType DO_TOWER_REFRESHED = WBuilder.waveType(TOWER_REFRESHED);

    @OnWave(SOLVE_TOWER)
    @Priority(PriorityLevel.Highest)
    void doSolveTower(final HanoiGame game, SolveDirection solveDirection, final Wave wave) throws InterruptedException;

}
