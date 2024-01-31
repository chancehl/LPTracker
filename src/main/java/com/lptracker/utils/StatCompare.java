package com.lptracker.utils;

import com.lptracker.models.SummonerLeagueStats;

public class StatCompare {
    public static int compare(SummonerLeagueStats a, SummonerLeagueStats b) {
        if (a.tier == b.tier && a.rank == b.rank) {
            return a.leaguePoints - b.leaguePoints;
        }

        return -1;
    }
}
