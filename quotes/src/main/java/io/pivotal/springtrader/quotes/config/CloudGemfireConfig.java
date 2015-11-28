package io.pivotal.springtrader.quotes.config;

import com.gemstone.gemfire.cache.ExpirationAction;
import com.gemstone.gemfire.cache.ExpirationAttributes;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.cache.client.ClientCache;
import io.pivotal.spring.cloud.service.gemfire.GemfireServiceConnectorConfig;
import io.pivotal.springtrader.quotes.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.ExpirationAttributesFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;

/**
 * Created by cax on 28/11/2015.
 */
@Configuration
@Profile("cloud")
public class CloudGemfireConfig extends AbstractCloudConfig {

    public ServiceConnectorConfig createGemfireConnectorConfig() {
        GemfireServiceConnectorConfig gemfireConfig = new GemfireServiceConnectorConfig();
        gemfireConfig.setPoolIdleTimeout(7777L);
        return gemfireConfig;
    }

    @Bean
    public ClientCache getGemfireClientCache() throws Exception{
        CloudFactory cloudFactory = new CloudFactory();
        Cloud cloud = cloudFactory.getCloud();

        ClientCache cache = cloud.getServiceConnector("quotes-cache", ClientCache.class, createGemfireConnectorConfig());
        return cache;
    }

    @Bean
    ClientRegionFactoryBean<String, Quote> quoteRegionFactory(final ClientCache cache, RegionAttributes<String, Quote> regionAttributes) {
        return new ClientRegionFactoryBean<String, Quote>() {
            {
                setCache(cache);
                setRegionName("quotes");
                setAttributes(regionAttributes);
                setClose(false);

            }
        };
    }

    @Bean
    public ExpirationAttributesFactoryBean expirationAttributes() {

        ExpirationAttributesFactoryBean expirationAttributes = new ExpirationAttributesFactoryBean();

        expirationAttributes.setAction(ExpirationAction.LOCAL_DESTROY);
        expirationAttributes.setTimeout(60);
        return expirationAttributes;
    }

    @Bean
    @Autowired
    public RegionAttributesFactoryBean localRegionAttributes(@Qualifier("expirationAttributes") ExpirationAttributes expirationAttributes) {

        RegionAttributesFactoryBean regionAttributes = new RegionAttributesFactoryBean();
        regionAttributes.setEntryTimeToLive(expirationAttributes);
        return regionAttributes;
    }
}
