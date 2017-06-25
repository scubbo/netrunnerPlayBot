package org.scubbo.netrunnerplaybot;

import java.util.ArrayList;
import java.util.List;

import org.scubbo.netrunnerplaybot.model.Action;
import org.scubbo.netrunnerplaybot.model.CorpBoardState;
import org.scubbo.netrunnerplaybot.model.Run;
import org.scubbo.netrunnerplaybot.model.RunnerBoardState;
import org.scubbo.netrunnerplaybot.model.TurnState;

public class ActionProvider {

    public List<Action> getActions(final TurnState turnState,
                                   final CorpBoardState corpBoardState,
                                   final RunnerBoardState runnerBoardState)
            throws IllegalArgumentException {

        if (turnState == null || corpBoardState == null || runnerBoardState == null) {
            throw new IllegalArgumentException();
        }

        List<Action> actions = new ArrayList<>();

        if (!turnState.isCorpTurn()) {
            // Generate run actions
            actions.add(new Run(0));
            actions.add(new Run(1));
            actions.add(new Run(2));
            corpBoardState.getRemotes().forEach((s) -> {actions.add(new Run(s.getId()));});
        }

        return actions;

    }

}
