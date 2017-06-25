package org.scubbo.netrunnerplaybot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Action {

    private final ActionType actionType;

}
