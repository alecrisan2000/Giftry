package com.example.giftry;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.ServerApi;

public class DatabaseRegistration {
    public static void main(String[] args) {
   //     String uri = "mongodb+srv://admin:admin@giftry.edlstbv.mongodb.net/?retryWrites=true&w=majority";

        ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:admin@giftry.edlstbv.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
     //   MongoDatabase database = mongoClient.getDatabase("test");


   //     try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase database = mongoClient.getDatabase("users");
            MongoCollection<Document> collection = database.getCollection("users");

            try {
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("username", "user1")
                        .append("password", "pass1"));

                System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
     //   }
    }
}
