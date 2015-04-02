package net.jjroman.homeautomation.osgi.configprovider.hardcoded;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Unit test covering generic functionality of Config Provider
 * //TODO package it externally for reuse
 * Created by Jan on 02/04/2015.
 */
public class GenericConfigProviderTest {

    @Test
    public void returnNullWhenNullInputProvided(){
        ConfigService cs = new ConfigProvider();
        assertNull("Null should be returned when context is null", cs.getConfigValueByNamespaceAndKey(null, "key"));
        assertNull("Null should be returned when key is null", cs.getConfigValueByNamespaceAndKey("context", null));
        assertNull("Null should be returned when key and context are both nulls", cs.getConfigValueByNamespaceAndKey(null, null));
    }

    @Test
    public void returnNullWhenThereIsNoSuchContext(){
        ConfigService cs = new ConfigProvider();
        assertNull("Null should be returned when there is no such context", cs.getConfigValueByNamespaceAndKey("system-no-such-context", "key"));
    }

    @Test
    public void returnNullWhenThereIsNoSuchKey(){
        ConfigService cs = new ConfigProvider();
        assertNull("Null should be returned when there is no such key in existing context", cs.getConfigValueByNamespaceAndKey("system-test", "no-such-key"));
        assertNull("Null should be returned when there is no such key in existing context", cs.getConfigValueByNamespaceAndKey("system-null", "key"));
    }

    @Test
    public void systemContextProvidedAndTestKeyExists(){
        ConfigService cs = new ConfigProvider();
        assertEquals("There should be system context and test=test key - value pair", "test", cs.getConfigValueByNamespaceAndKey("system-test", "test"));
    }

}
