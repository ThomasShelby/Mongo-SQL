package com.myaremchuk.mongoclient.service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Created by Mykola Yaremchuk on 6/4/17.
 */
public interface ConfigurationService {

    /**
     * This method returns MongoClient instance
     * @return
     */
    MongoClient getMongoClient();

    /**
     * This method provides instance of DB
     * @return DB instance
     */
    DB getDb();

    /**
     * This method provides collection
     * @return collection instance
     */
    DBCollection getCollection();

    /**
     * This method inserts values into DB if there is no data
     * @param collection  - collection instance
     */
    void insertDBValues(DBCollection collection);

    /**
     * This method removes values from table
     * @param table - collection instance
     */
    void clearTable(DBCollection table);

    /**
     * This method provides base configuration:
     * MongoClient creation
     * Collection creation
     * Inserting values into collection
     */
    void baseConfiguration();
}
