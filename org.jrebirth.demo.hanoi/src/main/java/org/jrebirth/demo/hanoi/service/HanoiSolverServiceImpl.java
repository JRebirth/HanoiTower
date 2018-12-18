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
import org.jrebirth.af.api.module.Register;
import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.core.service.DefaultService;
import org.jrebirth.demo.hanoi.bean.Disc;
import org.jrebirth.demo.hanoi.bean.HanoiGame;
import org.jrebirth.demo.hanoi.bean.HanoiTower;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Service ExpressionBuilder is used to build all mathematical tables.
 */
@Register(value = HanoiSolverService.class, priority = PriorityLevel.Normal)
public final class HanoiSolverServiceImpl extends DefaultService implements HanoiSolverService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HanoiSolverServiceImpl.class);

    @Override
    public void initService() {

        // Define the service method
        listen(DO_SOLVE_TOWER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Priority(PriorityLevel.Highest)
    public void doSolveTower(final HanoiGame game, final SolveDirection solveDirection, final Wave wave) throws InterruptedException {

        LOGGER.trace("Solve Tower.");

        switch (solveDirection) {
            case thirdToFirst:
                solve(game.nbDisc(), game.third(), game.second(), game.first());
                break;
            case firstToThird:
            default:
                solve(game.nbDisc(), game.first(), game.second(), game.third());
        }

        LOGGER.trace("Tower Solved !");
    }

    private void solve(final int discCount, final HanoiTower from, final HanoiTower temp, final HanoiTower to) {

        if (discCount > 1) {
            solve(discCount - 1, from, to, temp);
            moveDisc(from, to);
            solve(discCount - 1, temp, from, to);
        } else {
            moveDisc(from, to);
        }
    }

    private void moveDisc(final HanoiTower from, final HanoiTower to) {
        final Disc d = from.stack().get(from.stack().size() - 1);
        from.stack().remove(d);
        to.addStack(d);

        sendWave(DO_TOWER_REFRESHED);
        try {
            Thread.sleep(200);
        } catch (final InterruptedException e) {
        }
    }

}
