/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.game.turn;

import java.util.UUID;
import mage.constants.PhaseStep;
import mage.game.Game;
import mage.game.events.GameEvent.EventType;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class DeclareBlockersStep extends Step {

    public DeclareBlockersStep() {
        super(PhaseStep.DECLARE_BLOCKERS, true);
        this.stepEvent = EventType.DECLARE_BLOCKERS_STEP;
        this.preStepEvent = EventType.DECLARE_BLOCKERS_STEP_PRE;
        this.postStepEvent = EventType.DECLARE_BLOCKERS_STEP_POST;
    }

    public DeclareBlockersStep(final DeclareBlockersStep step) {
        super(step);
    }

    @Override
    public boolean skipStep(Game game, UUID activePlayerId) {
        if (game.getCombat().noAttackers()) {
            return true;
        }
        return super.skipStep(game, activePlayerId);
    }

    @Override
    public void beginStep(Game game, UUID activePlayerId) {
        super.beginStep(game, activePlayerId);
        game.getCombat().selectBlockers(game);
        if (!game.isPaused() && !game.executingRollback()) {
            game.getCombat().acceptBlockers(game);
            game.getCombat().damageAssignmentOrder(game);
        }
    }

    @Override
    public void resumeBeginStep(Game game, UUID activePlayerId) {
        super.resumeBeginStep(game, activePlayerId);
        game.getCombat().resumeSelectBlockers(game);
        game.getCombat().acceptBlockers(game);
        game.getCombat().damageAssignmentOrder(game);
    }

    @Override
    public DeclareBlockersStep copy() {
        return new DeclareBlockersStep(this);
    }

}
