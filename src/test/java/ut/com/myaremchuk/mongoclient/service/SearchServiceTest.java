package ut.com.myaremchuk.mongoclient.service;

import com.myaremchuk.mongoclient.service.SearchService;
import com.myaremchuk.mongoclient.service.impl.SearchServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mykola Yaremchuk on 6/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

    @Mock
    SearchService searchService;

    List<String> originalColumnNames;
    List<String> testQuery;

    @Before
    public void setUp() throws Exception {
        testQuery = new ArrayList<>();
        testQuery.add("select");
        testQuery.add("*");
        testQuery.add("from");
        testQuery.add("users");

        originalColumnNames = new ArrayList<>();
        originalColumnNames.add("_id");
        originalColumnNames.add("firstName");
        originalColumnNames.add("lastName");
        originalColumnNames.add("country");
        originalColumnNames.add("age");
        originalColumnNames.add("createdDate");

        searchService = new SearchServiceImpl();
    }

    @Test
    public void getMongoOperator1() {
        String actual = searchService.getMongoOperator("=");
        String expected = "$eq";
        assertEquals(expected, actual);
    }

    @Test
    public void getMongoOperator2() {
        String actual = searchService.getMongoOperator(">");
        String expected = "$gt";
        assertEquals(expected, actual);
    }

    @Test
    public void getMongoOperator3() {
        String actual = searchService.getMongoOperator("<=");
        String expected = "$lte";
        assertEquals(expected, actual);
    }

    @Test
    public void getMongoOperatorFalse() {
        String actual = searchService.getMongoOperator(">");
        String expected = "$eq";
        assertFalse(expected.equals(actual));
    }

    @Test
    public void isQueryValidTrue() {
        boolean actual = searchService.isQueryValid(testQuery);
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void isQueryValidFalseWithRandomString() {
        List<String> testQueryList = new ArrayList<>();
        testQueryList.add("hello");

        boolean actual = searchService.isQueryValid(testQueryList);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void isQueryValidFalseWithoutSelectWord() {
        List<String> testQueryList = new ArrayList<>();
        testQueryList.add("*");
        testQueryList.add("from");
        testQueryList.add("users");

        boolean actual = searchService.isQueryValid(testQueryList);
        boolean expected = false;

        assertEquals(expected, actual);
    }

    @Test
    public void isQueryValidFalseWithoutFromWord() {
        List<String> testQueryList = new ArrayList<>();
        testQueryList.add("select");
        testQueryList.add("*");
        testQueryList.add("users");

        boolean actual = searchService.isQueryValid(testQueryList);
        boolean expected = false;

        assertEquals(expected, actual);
    }

    @Test
    public void getOriginalFieldByName() {
        String actual = searchService.getOriginalFieldByName("CouNtrY").get();
        String expected = "country";
        assertEquals(expected, actual);
    }

    @Test
    public void getOriginalFieldByName2() {
        String actual = searchService.getOriginalFieldByName("FIRSTName").get();
        String expected = "firstName";
        assertEquals(expected, actual);
    }

    @Test
    public void getOriginalColumnNamesTrue(){
        List<String> actual =searchService.getOriginalColumnNames();
        List<String> expected = originalColumnNames;
        assertEquals(expected, actual);
    }

    @Test
    public void getOriginalColumnNamesFalse(){
        List<String> origionalColumnNames = new ArrayList<>();
        origionalColumnNames.add("_id");
        origionalColumnNames.add("FIRSTName");
        origionalColumnNames.add("lastNAME");
        origionalColumnNames.add("COUNTRY");
        origionalColumnNames.add("age");
        origionalColumnNames.add("createdDate");

        List<String> actual =searchService.getOriginalColumnNames();
        List<String> expected = origionalColumnNames;
        assertFalse(expected == actual);
    }

    @Test
    public void getConvertedListFromEnteredQuery1(){
        String enteredQuery = "select firstName, country from users where country = USA";
        List<String> actual = searchService.getConvertedListFromEnteredQuery(enteredQuery);
        List<String> expected = new ArrayList<>();
        expected.add("select");
        expected.add("firstname");
        expected.add("country");
        expected.add("from");
        expected.add("users");
        expected.add("where");
        expected.add("country");
        expected.add("=");
        expected.add("usa");

        assertEquals(expected, actual);
    }

    @Test
    public void getConvertedListFromEnteredQuery2(){
        String enteredQuery = "select firstName, , , , ,country, ,, from users where country = USA";
        List<String> actual = searchService.getConvertedListFromEnteredQuery(enteredQuery);
        List<String> expected = new ArrayList<>();
        expected.add("select");
        expected.add("firstname");
        expected.add("country");
        expected.add("from");
        expected.add("users");
        expected.add("where");
        expected.add("country");
        expected.add("=");
        expected.add("usa");

        assertEquals(expected, actual);
    }
}
