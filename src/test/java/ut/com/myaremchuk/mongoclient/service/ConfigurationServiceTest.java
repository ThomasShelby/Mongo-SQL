package ut.com.myaremchuk.mongoclient.service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.myaremchuk.mongoclient.service.ConfigurationService;
import com.myaremchuk.mongoclient.service.impl.ConfigurationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mykola Yaremchuk on 6/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @Mock
    ConfigurationService configurationService;

    MongoClient mongoClient;
    DB db;
    DBCollection collection;

    @Before
    public void setUp() throws Exception {
        mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("testdb");
        collection = db.getCollection("users");

        configurationService = new ConfigurationServiceImpl();
    }

    @Test
    public void getCollection() {
        String actual= configurationService.getCollection().getName();
        String expected = collection.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getDb() {
        String actual = configurationService.getDb().getName();
        String expected = db.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getMongoClient(){
        ServerAddress actual = configurationService.getMongoClient().getAddress();
        ServerAddress expected = mongoClient.getAddress();
        assertEquals(expected, actual);
    }
}
