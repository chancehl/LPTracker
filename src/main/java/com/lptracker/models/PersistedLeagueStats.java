package com.lptracker.models;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class PersistedLeagueStats {
    public ObjectId id;
    public Long createdOn;
    public String summonerId;
    public List<SummonerLeagueStats> leagueStats;

}
