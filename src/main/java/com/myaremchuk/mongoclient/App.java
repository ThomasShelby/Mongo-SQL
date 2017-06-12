package com.myaremchuk.mongoclient;

import com.myaremchuk.mongoclient.service.ConfigurationService;
import com.myaremchuk.mongoclient.service.SearchService;
import com.myaremchuk.mongoclient.service.impl.ConfigurationServiceImpl;
import com.myaremchuk.mongoclient.service.impl.SearchServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mykola Yaremchuk on 6/5/17.
 */
public class App {

    public static void main(String[] args) throws IOException {
        ConfigurationService configurationService = new ConfigurationServiceImpl();
        SearchService searchService = new SearchServiceImpl();

        configurationService.baseConfiguration();
        searchService.initOperatorMap();

        while (true) {
            String enteredQuery = searchService.getEnteredQuery();
            List<String> convertedListFromEnteredQuery = searchService.getConvertedListFromEnteredQuery(enteredQuery);

            if (enteredQuery.equals("-exit")) {
                break;
            }

            if (searchService.isQueryValid(convertedListFromEnteredQuery)) {
                searchService.parseQuery(convertedListFromEnteredQuery, configurationService.getCollection());
            } else {
                System.out.println("Query is not valid!");
            }

        }

        configurationService.clearTable(configurationService.getCollection());

        /**** Done ****/
        System.out.println("Done");
    }
}
