package org.scubbo.netrunnerplaybot.model;

import java.util.List;

import lombok.Data;

@Data
public class CorpBoardState extends BoardState {

    private Server archives;
    private Server rAndD;
    private Server hq;
    private List<Server> remotes;

}
