package org.scubbo.netrunnerplaybot.model;

import lombok.Getter;

@Getter
public class Run extends RunnerAction {

    private final int serverId;

    public Run(final int serverId) {
        super(ActionType.RUN);
        this.serverId = serverId;
    }

}
