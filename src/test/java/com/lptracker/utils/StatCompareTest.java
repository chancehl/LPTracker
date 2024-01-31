package com.lptracker.utils;

import com.lptracker.models.Rank;
import com.lptracker.models.SummonerLeagueStats;
import com.lptracker.models.Tier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatCompareTest {

    @Test
    void compareSameElo() {
        SummonerLeagueStats a = SummonerLeagueStats.builder()
                .leaguePoints(99)
                .rank(Rank.IV)
                .tier(Tier.SILVER)
                .build();

        SummonerLeagueStats b = SummonerLeagueStats.builder()
                .leaguePoints(98)
                .rank(Rank.IV)
                .tier(Tier.SILVER)
                .build();

        assertEquals(1,StatCompare.compare(a, b));
    }
}