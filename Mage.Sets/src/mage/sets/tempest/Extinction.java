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
package mage.sets.tempest;

import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.repository.CardRepository;
import mage.choices.Choice;
import mage.choices.ChoiceImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;

/**
 *
 * @author fireshoes
 */
public class Extinction extends CardImpl {

    public Extinction(UUID ownerId) {
        super(ownerId, 29, "Extinction", Rarity.RARE, new CardType[]{CardType.SORCERY}, "{4}{B}");
        this.expansionSetCode = "TMP";

        // Destroy all creatures of the creature type of your choice.
        this.getSpellAbility().addEffect(new ExtinctionEffect());
    }

    public Extinction(final Extinction card) {
        super(card);
    }

    @Override
    public Extinction copy() {
        return new Extinction(this);
    }
}

class ExtinctionEffect extends OneShotEffect {

    public ExtinctionEffect() {
        super(Outcome.UnboostCreature);
        staticText = "Destroy all creatures of the creature type of your choice";
    }

    public ExtinctionEffect(final ExtinctionEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (player != null) {
            Choice typeChoice = new ChoiceImpl(true);
            typeChoice.setMessage("Choose a creature type:");
            typeChoice.setChoices(CardRepository.instance.getCreatureTypes());
            while (!player.choose(outcome, typeChoice, game)) {
                if (!player.canRespond()) {
                    return false;
                }
            }
            if (typeChoice.getChoice() != null) {
                game.informPlayers(sourceObject.getLogName() + " chosen type: " + typeChoice.getChoice());
            }
            FilterCreaturePermanent filterCreaturePermanent = new FilterCreaturePermanent();
            filterCreaturePermanent.add(new SubtypePredicate(typeChoice.getChoice()));
            for (Permanent creature : game.getBattlefield().getActivePermanents(filterCreaturePermanent, source.getSourceId(), game)) {
                creature.destroy(source.getSourceId(), game, true);
            }
            return true;
        }
        return false;
    }

    @Override
    public ExtinctionEffect copy() {
        return new ExtinctionEffect(this);
    }
}
