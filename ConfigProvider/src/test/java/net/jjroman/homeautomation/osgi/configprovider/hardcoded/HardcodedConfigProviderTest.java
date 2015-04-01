package net.jjroman.homeautomation.osgi.configprovider.hardcoded;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import net.jjroman.homeautomation.osgi.pinconsumer.pi4j.CoalBurnerController;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testing existence of CoalBurner required configs
 * Created by Jan on 01/04/2015.
 */
public class HardcodedConfigProviderTest {

    @Test
    public void testProvideConfigsRequiredByCoalBurner(){
        ConfigService configService = new ConfigProvider();

        assertNotNull(configService.getConfigValueByNamespaceAndKey("coalburner", CoalBurnerController.CONFIG_GOTO_ACTIVE_TEMP));
        assertNotNull(configService.getConfigValueByNamespaceAndKey("coalburner", CoalBurnerController.CONFIG_GOTO_STANDBY_TEMP));

    }

    @Test
    public void testProvideConfigsRequiredByCoalBurnerParsableDoubles(){
        ConfigService configService = new ConfigProvider();
        Double v1 =
                Double.parseDouble(
                configService.getConfigValueByNamespaceAndKey("coalburner", CoalBurnerController.CONFIG_GOTO_ACTIVE_TEMP));
        assertNotNull(v1);

        Double v2 =
                Double.parseDouble(
                        configService.getConfigValueByNamespaceAndKey("coalburner", CoalBurnerController.CONFIG_GOTO_STANDBY_TEMP));
        assertNotNull(v2);
    }

    @Test
    public void testStandbyHigherThanActive(){
        ConfigService configService = new ConfigProvider();
        Double active =
                Double.parseDouble(
                        configService.getConfigValueByNamespaceAndKey("coalburner", CoalBurnerController.CONFIG_GOTO_ACTIVE_TEMP));

        Double standby =
                Double.parseDouble(
                        configService.getConfigValueByNamespaceAndKey("coalburner", CoalBurnerController.CONFIG_GOTO_STANDBY_TEMP));

        assertTrue("Temperature for standby mode needs to be higher than standby", standby > active );

    }
}
