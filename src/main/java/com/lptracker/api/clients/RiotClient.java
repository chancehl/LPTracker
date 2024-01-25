package com.lptracker.api.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lptracker.models.SummonerLeagueStats;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RiotClient implements Serializable {
    private final ObjectMapper mapper;
    private final String apiKey;

    public RiotClient (String apiKey) {
        this.mapper = new ObjectMapper();
        this.apiKey = apiKey;
    }

    public List<SummonerLeagueStats> getSummonerLeagueStats(String summonerId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://na1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s", summonerId)))
                .header("X-Riot-Token", this.apiKey)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return this.mapper.readValue(response.body(), new TypeReference<List<SummonerLeagueStats>>(){});
    }
}
