package com.lptracker;

import com.lptracker.api.clients.RiotClient;
import com.lptracker.db.clients.DbClient;
import com.lptracker.models.PersistedLeagueStats;
import com.lptracker.models.SummonerLeagueStats;
import com.lptracker.utils.StatCompare;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LPTracker {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        RiotClient riotClient = new RiotClient(dotenv.get("RIOT_API_KEY"));

        DbClient dbClient = new DbClient(dotenv.get("MONGODB_CONNECTION_STRING"));

        try {
            String summonerId = dotenv.get("SUMMONER_ID");

            // Get latest summoner stats
            List<SummonerLeagueStats> leagueStats = riotClient.getSummonerLeagueStats(summonerId);

            // Get previous summoner stats
            PersistedLeagueStats savedStats = dbClient.getLatestSummonerLeagueStats(summonerId);

            // Compare
            if (savedStats != null) {
                for (SummonerLeagueStats stat : leagueStats) {
                    Optional<SummonerLeagueStats> matchingLeagueStatsFromPast = savedStats
                            .leagueStats
                            .stream()
                            .filter(s -> s.leagueId.equals(stat.leagueId))
                            .findFirst();

                    if (matchingLeagueStatsFromPast.isPresent()) {
                        SummonerLeagueStats previousStat = matchingLeagueStatsFromPast.get();

                        int diff = StatCompare.compare(stat, previousStat);

                        System.out.printf("You have %s %s LP for %s queue since %s.%n", diff > 0 ? "gained" : "lost", diff, stat.queueType, new Date(savedStats.createdOn));
                    } else {
                        System.out.printf("Missing previous stat for %s queue.%n", stat.queueType);
                    }
                }
            }

            // Save the new stats as the latest
            dbClient.saveSummonerStats(summonerId, leagueStats);
        } catch (IOException | InterruptedException | RuntimeException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }
}
