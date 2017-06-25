package org.scubbo.netrunnerplaybot;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.scubbo.netrunnerplaybot.model.Action;
import org.scubbo.netrunnerplaybot.model.ActionType;
import org.scubbo.netrunnerplaybot.model.CorpBoardState;
import org.scubbo.netrunnerplaybot.model.Run;
import org.scubbo.netrunnerplaybot.model.RunnerBoardState;
import org.scubbo.netrunnerplaybot.model.Server;
import org.scubbo.netrunnerplaybot.model.TurnState;

@RunWith(MockitoJUnitRunner.class)
public class ActionProviderTest {

    private static final ActionProvider provider = new ActionProvider();

    @Mock TurnState turnState;
    @Mock CorpBoardState corpBoardState;
    @Mock RunnerBoardState runnerBoardState;

    @Test
    public void assertsNonNull() throws Exception {
        try {
            provider.getActions(null, corpBoardState, runnerBoardState);
            fail("Passed a null turnState but didn't throw exception");
        } catch (Exception e) {
            assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
        }

        try {
            provider.getActions(turnState, null, runnerBoardState);
            fail("Passed a null corpBoardState but didn't throw exception");
        } catch (Exception e) {
            assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
        }

        try {
            provider.getActions(turnState, corpBoardState, null);
            fail("Passed a null runnerBoardState but didn't throw exception");
        } catch (Exception e) {
            assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
        }

        provider.getActions(turnState, corpBoardState, runnerBoardState);
    }

    @Test
    public void alwaysReturnRunsOnCentralsIfIsRunnerTurn() throws Exception {

        when(turnState.isCorpTurn()).thenReturn(false);

        final List<Action> actions = provider.getActions(turnState, corpBoardState, runnerBoardState);
        final List<Action> runActions =
                actions.stream()
                .filter((a) -> a.getActionType().equals(ActionType.RUN))
                .collect(Collectors.toList());
        assertThat(runActions).extracting("serverId").containsExactly(0, 1, 2);

    }

    @Test
    public void returnRunsForAnyExistingRemotes() throws Exception {

        when(turnState.isCorpTurn()).thenReturn(false);

        Random rand = new Random();
        int i1 = rand.nextInt();
        System.out.println("nextInt is " + Integer.toString(i1));
        final int numberOfRemotes = abs(i1 % 10);

        final List<Integer> remoteIds = new ArrayList<>();
        final List<Server> remotes = new ArrayList<>();
        for (int i = 0; i<numberOfRemotes; i++) {
            remoteIds.add(i+3);
            remotes.add(new Server(i+3)); // +3 because centrals are 0, 1, 2
        }

        when(corpBoardState.getRemotes()).thenReturn(remotes);

        final List<Action> actions = provider.getActions(turnState, corpBoardState, runnerBoardState);
        final List<Integer> runActionServerIds =
                actions.stream()
                        .filter((a) -> a.getActionType().equals(ActionType.RUN))
                        .map((a) -> ((Run)a).getServerId())
                        .collect(Collectors.toList());
        assertThat(runActionServerIds).contains(0, 1, 2);
        assertThat(runActionServerIds).containsAll(remoteIds);

    }

    @Test
    public void doNotReturnRunActionsOnCorpTurn() throws Exception {

        when(turnState.isCorpTurn()).thenReturn(true);

        Random rand = new Random();
        int i1 = rand.nextInt();
        System.out.println("nextInt is " + Integer.toString(i1));
        final int numberOfRemotes = abs(i1 % 10);

        final List<Integer> remoteIds = new ArrayList<>();
        final List<Server> remotes = new ArrayList<>();
        for (int i = 0; i<numberOfRemotes; i++) {
            remoteIds.add(i+3);
            remotes.add(new Server(i+3)); // +3 because centrals are 0, 1, 2
        }

        when(corpBoardState.getRemotes()).thenReturn(remotes);

        final List<Action> actions = provider.getActions(turnState, corpBoardState, runnerBoardState);
        final List<Action> runActionServerIds =
                actions.stream()
                        .filter((a) -> a.getActionType().equals(ActionType.RUN))
                        .collect(Collectors.toList());
        assertThat(runActionServerIds).isEmpty();

    }

}