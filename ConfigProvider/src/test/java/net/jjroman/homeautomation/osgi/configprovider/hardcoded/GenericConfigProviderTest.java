package net.jjroman.homeautomation.osgi.configprovider.hardcoded;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
/**
 * Unit test covering generic functionality of Config Provider
 * //TODO package it externally for reuse
 * Created by Jan on 02/04/2015.
 */
public class GenericConfigProviderTest {

    @Test
    public void returnEmptyMapWhenNullInputProvided(){
        ConfigService cs = new ConfigProvider();
        assertNotNull("Not Null should be returned when context is null", cs.getConfigValuesForNamespace(null));
        assertTrue("Empty map should be returned when there is no such context", Map.class.isAssignableFrom(cs.getConfigValuesForNamespace(null).getClass()));
        assertEquals("Empty map should be returned when there is no such context", 0, cs.getConfigValuesForNamespace(null).size());


        //assertNull("Null should be returned when key is null", cs.getConfigValuesForNamespace("context").get(null));
    }

    @Test
    public void returnEmptyMapWhenThereIsNoSuchContext(){
        ConfigService cs = new ConfigProvider();
        assertNotNull("Empty map should be returned when there is no such context", cs.getConfigValuesForNamespace("system-no-such-context"));

        assertTrue("Empty map should be returned when there is no such context", Map.class.isAssignableFrom(cs.getConfigValuesForNamespace("system-no-such-context").getClass()));
        assertEquals("Empty map should be returned when there is no such context", 0, cs.getConfigValuesForNamespace("system-no-such-context").size());
    }

    @Test
    public void systemContextProvidedAndTestKeyExists(){
        ConfigService cs = new ConfigProvider();
        assertEquals("There should be system context and test=test key - value pair", "test", cs.getConfigValuesForNamespace("system-test").get("test"));
    }

}
