package com.crud;

import com.model.Empl;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpExchange;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrudOperationMongoDB {

    private static final String DATABASE = "lab6pad";
    private static final String COLLECTION = "pad";

    public void insertOne(Empl empl){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION);

        mongoCollection.insertOne(empl.getDocument());
    }

    public void findOneAndReplace(HttpExchange httpExchange, Empl empl){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION);

        mongoCollection.findOneAndReplace(
                new BsonDocument("firstname", new BsonString(httpExchange.getRequestHeaders().get("firstname").get(0))),
                empl.getDocument()
        );
    }

    public void delete(String firstname){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION);

        mongoCollection.deleteOne(
                new BsonDocument("firstname", new BsonString(firstname))
        );
    }

    public List<Empl> find(){
        List<Empl> empls = new ArrayList<>();
        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION);

        FindIterable<Document> datas = mongoCollection.find();
        datas.forEach((Block<? super Document>) document -> {
            Empl empl = new Empl(document);
            empls.add(empl);
        });
        return empls;
    }

    public List<Empl> findOffsetLimit(Map<String, Integer> values) {
        List<Empl> empls = new ArrayList<>();
        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION);

        FindIterable<Document> datas = mongoCollection.find().skip(values.get("offset")).limit(values.get("limit"));
        datas.forEach((Block<? super Document>) document -> {
            Empl empl = new Empl(document);
            empls.add(empl);
        });
        return empls;
    }
}
