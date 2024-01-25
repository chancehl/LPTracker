package com.lptracker.db.clients;

import com.lptracker.models.PersistedLeagueStats;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.lptracker.models.SummonerLeagueStats;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class DbClient {
    private final MongoCollection<PersistedLeagueStats> collection;

    public DbClient(String connectionString) {
        String DATABASE_NAME = "LPTracker";
        String COLLECTION_NAME = "summonerLeagueStats";

        ServerApi api = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .build()
        );

        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .codecRegistry(codecRegistry)
                .serverApi(api)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

        this.collection = database.getCollection(COLLECTION_NAME, PersistedLeagueStats.class);
    }

    public PersistedLeagueStats getLatestSummonerLeagueStats(String summonerId) throws MongoException {
       Bson filter = Filters.all("summonerId", summonerId);
       Bson sort = Sorts.descending("createdOn");

       return this.collection.find(filter).sort(sort).first();
    }

    public void saveSummonerStats(String summonerId, List<SummonerLeagueStats> leagueStats) throws MongoException {
        PersistedLeagueStats persistedLeagueStats = new PersistedLeagueStats();

        persistedLeagueStats.createdOn = new Date().getTime();
        persistedLeagueStats.summonerId = summonerId;
        persistedLeagueStats.leagueStats = leagueStats;

        this.collection.insertOne(persistedLeagueStats);
    }
}
