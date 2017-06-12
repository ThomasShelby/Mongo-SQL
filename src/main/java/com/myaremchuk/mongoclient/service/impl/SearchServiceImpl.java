package com.myaremchuk.mongoclient.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.myaremchuk.mongoclient.service.ConfigurationService;
import com.myaremchuk.mongoclient.service.SearchService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mykola Yaremchuk on 6/7/17.
 */
public class SearchServiceImpl implements SearchService {

    private Map<String, String> operatorMap;
    private DB db;
    private List<String> originalColumnNames;

    @Override
    public void initOperatorMap() {
        if (operatorMap == null) {
            operatorMap = new HashMap<>();
            operatorMap.put("!=", "$ne");
            operatorMap.put("=", "$eq");
            operatorMap.put("<>", "$ne");
            operatorMap.put(">", "$gt");
            operatorMap.put("<", "$lt");
            operatorMap.put(">=", "$gte");
            operatorMap.put("<=", "$lte");
        }
    }

    @Override
    public String getMongoOperator(String sqlOperator) {
        if(operatorMap == null)
            initOperatorMap();

        return operatorMap.get(sqlOperator);
    }

    @Override
    public String getEnteredQuery(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("SQL query:");
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Problem with query reading..");
        }
        return null;
    }

    @Override
    public List<String> getConvertedListFromEnteredQuery(String enteredQuery){
        List<String> queryList = new ArrayList<>();

        String cleanedLine = enteredQuery.replaceAll("[,;]", "");
        StringTokenizer defaultTokenizer = new StringTokenizer(cleanedLine);

        while (defaultTokenizer.hasMoreTokens()) {
            String element = defaultTokenizer.nextToken();
            queryList.add(element);
        }
        List<String> caseInsensitiveQueryList = queryList.stream().map(String::toLowerCase).collect(Collectors.toList());
        return caseInsensitiveQueryList;
    }

    @Override
    public boolean isQueryValid(List<String> queryList) {
        if (queryList.contains("select") && queryList.contains("from") || queryList.contains("-exit"))
            return true;

        return false;
    }

    @Override
    public void parseQuery(List<String> queryList, DBCollection collection) {
        BasicDBObject allQuery = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<BasicDBObject>();

        if (queryList.contains("where")) {
            int indexOfWhereWord = queryList.indexOf("where");
            List<String> whereList = queryList.subList(indexOfWhereWord + 1, queryList.size());
            System.out.println("WHERE LIST: " + whereList.toString());

            parseWhereQueryPart(whereList, allQuery);
        }

        parseSelectQueryPart(queryList, fields);

        DBCursor cursor = collection.find(allQuery, fields);

        while (cursor.hasNext()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(cursor.next());
            System.out.println(json);
        }
    }

    @Override
    public Optional<String> getOriginalFieldByName(String name) {
        try {
            if (getOriginalColumnNames().stream().filter(name::equalsIgnoreCase).findFirst().isPresent()) {
                return getOriginalColumnNames().stream().filter(name::equalsIgnoreCase).findFirst();
            } else {
                System.out.println("Wrong criteria name. Please check it!");
            }
        } catch (NoSuchElementException ex) {
            System.out.println("Wrong column name. Please check it!");
        }
        return Optional.empty();
    }

    @Override
    public String getOriginalFieldValByColName(String columnName, String columnValue) {
        try {
            return getOriginalDBValuesByColName(columnName).stream().filter(columnValue::equalsIgnoreCase).findFirst().get();
        } catch (NoSuchElementException ex) {
            System.out.println("Wrong column name. Please check it!");
        }
        return null;
    }

    @Override
    public List<String> getOriginalColumnNames() {
        ConfigurationService configurationService = new ConfigurationServiceImpl();
        if (originalColumnNames == null || originalColumnNames.size() == 0) {
            originalColumnNames = new ArrayList();
            if(db == null)
                db = configurationService.getDb();

            DBCursor dbCursor = db.getCollection("users").find();
            for (String key : dbCursor.next().keySet()) {
                originalColumnNames.add(key);
            }
            return originalColumnNames;
        }
        return originalColumnNames;
    }

    @Override
    public List<String> getOriginalDBValuesByColName(String columnName) {
        DBCursor dbCursor = db.getCollection("users").find();
        List<DBObject> list = dbCursor.toArray();
        return list.stream().map(o ->
                String.valueOf(o.get(getOriginalFieldByName(columnName).get()))).collect(Collectors.toList());
    }

    private void parseWhereQueryPart(List<String> whereQueryList, BasicDBObject allQuery) {
        whereQueryList.forEach(element -> {
            if (element.matches("(=|!=|>|<|>=|<|<=|<>)")) {
                Optional<String> fieldName = getOriginalFieldByName(whereQueryList.get(whereQueryList.indexOf(element.toLowerCase()) - 1));
                if (fieldName.isPresent()) {
                    allQuery.put(fieldName.get(),
                            getFieldObject(fieldName.get(), element, whereQueryList)
                    );
                }
            }
        });
    }

    private void parseSelectQueryPart(List<String> caseInsensitiveList, BasicDBObject fields) {
        if (caseInsensitiveList.contains("from")) {
            int lastSelectFieldIndex = caseInsensitiveList.indexOf("from");
            List<String> selectFieldsList = caseInsensitiveList.subList(1, lastSelectFieldIndex);

            if (!selectFieldsList.contains("*")) {
                selectFieldsList.forEach(name -> {
                    if(getOriginalFieldByName(name).isPresent()) {
                        fields.put(getOriginalFieldByName(name).get(), 1);
                    } else {
                        System.out.println("Wrong parameter name in select list. Please check it!");
                    }
                }
                );
            }
        }
    }

    private BasicDBObject getFieldObject(String columnName, String element, List<String> whereQueryList) {
        Object fieldValue;
        try {
            fieldValue = Integer.parseInt(whereQueryList.get(whereQueryList.indexOf(element.toLowerCase()) + 1));
            return new BasicDBObject(getMongoOperator(element), fieldValue);
        } catch (NumberFormatException exp) {
            fieldValue = whereQueryList.get(whereQueryList.indexOf(element) + 1);
            return new BasicDBObject(getMongoOperator(element),
                    getOriginalFieldValByColName(columnName, fieldValue.toString())
            );
        }
    }
}
