package com.myaremchuk.mongoclient.service;

import com.mongodb.DBCollection;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mykola Yaremchuk on 6/5/17.
 */
public interface SearchService {

    /**
     * This method checks if entered query is valid
     * @param queryList - user's query list
     * @return true or false
     */
    boolean isQueryValid(List<String> queryList);

    /**
     * This method provides parsing of SQL query into Mongo syntax via Java implementation
     * @param queryList
     * @param table
     */
    void parseQuery(List<String> queryList, DBCollection table);

    /**
     * This method provides mapping of SQL operators to Mongo
     */
    void initOperatorMap();

    /**
     * This method returns related Mongo operator basing on entered SQL operator
     * @param sqlOperator
     * @return mongo operator
     */
    String getMongoOperator(String sqlOperator);

    /**
     * This method reads user's entered row
     * @return entered query as String
     */
    String getEnteredQuery();

    /**
     * This method converts entered query into list of words to provide possibility to operate with query elements
     * @param enteredQuery
     * @return list of query elements
     */
    List<String> getConvertedListFromEnteredQuery(String enteredQuery);

    /**
     * This method provides column names from DB
     * @return list of original column names
     */
    List<String>  getOriginalColumnNames();

    /**
     * This method provides values from DB related to entered column name
     * @param columnName - name of column in DB which will be used in searching
     * @return list of original DB values
     */
    List<String> getOriginalDBValuesByColName(String columnName);

    /**
     * This method provides original name from DB of entered field
     * This is needed to provide possibility to enter case insensitive column names
     * @param name
     * @return original field name from DB
     */
    Optional<String> getOriginalFieldByName(String name);

    /**
     * This method provides original value from DB of entered column name
     * This is needed to provide possibility to enter case insensitive names in where condition
     * @param columnName
     * @param columnValue
     * @return  original field value from DB
     */
    String getOriginalFieldValByColName(String columnName, String columnValue);
}
