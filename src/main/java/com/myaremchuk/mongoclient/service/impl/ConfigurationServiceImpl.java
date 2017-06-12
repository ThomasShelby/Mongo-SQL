package com.myaremchuk.mongoclient.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.myaremchuk.mongoclient.service.ConfigurationService;

import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by Mykola Yaremchuk on 6/7/17.
 */
public class ConfigurationServiceImpl implements ConfigurationService {

    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String COLLECTION_NAME = "users";
    private static final String DB_NAME = "testdb";

    @Override
    public DBCollection getCollection() {
        return getDb().getCollection(COLLECTION_NAME);
    }

    @Override
    public void baseConfiguration() {
        insertDBValues(getCollection());
    }

    @Override
    public MongoClient getMongoClient(){
        try {
            return new MongoClient(HOST, PORT);
        } catch (UnknownHostException e) {
            System.out.println("ERROR in creating mongoClient");
            return null;
        }
    }

    @Override
    public DB getDb(){
        return getMongoClient().getDB(DB_NAME);
    }

    @Override
    public void insertDBValues(DBCollection collection) {
        if (getDb().getCollection(collection.getName()).findOne() == null) {

            BasicDBObject document1 = new BasicDBObject();
            document1.put("firstName", "Teodor");
            document1.put("lastName", "Roosevelt");
            document1.put("country", "USA");
            document1.put("age", 60);
            document1.put("createdDate", new Date());
            collection.insert(document1);

            BasicDBObject document2 = new BasicDBObject();
            document2.put("firstName", "Arnold");
            document2.put("lastName", "Schwarzenegger");
            document2.put("country", "Austria");
            document2.put("age", 69);
            document2.put("createdDate", new Date());
            collection.insert(document2);

            BasicDBObject document3 = new BasicDBObject();
            document3.put("firstName", "Winston");
            document3.put("lastName", "Churchill");
            document3.put("country", "UK");
            document3.put("age", 90);
            document3.put("createdDate", new Date());
            collection.insert(document3);

            BasicDBObject document4 = new BasicDBObject();
            document4.put("firstName", "Mark");
            document4.put("lastName", "Zuckerberg");
            document4.put("country", "USA");
            document4.put("age", 33);
            document4.put("createdDate", new Date());
            collection.insert(document4);

            BasicDBObject document5 = new BasicDBObject();
            document5.put("firstName", "Bill");
            document5.put("lastName", "Gates");
            document5.put("country", "USA");
            document5.put("age", 61);
            document5.put("createdDate", new Date());
            collection.insert(document5);
        }
    }

    @Override
    public void clearTable(DBCollection table) {
        getDb().getCollection(table.getName()).remove(new BasicDBObject());
    }
}
