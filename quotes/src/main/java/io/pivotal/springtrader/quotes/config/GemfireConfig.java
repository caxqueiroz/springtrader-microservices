package io.pivotal.springtrader.quotes.config;


import com.gemstone.gemfire.cache.*;
import io.pivotal.springtrader.quotes.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.*;


/**
 * Created by cax on 27/11/2015.
 */
@Configuration
@Profile("local")
public class GemfireConfig {

    @Bean
    CacheFactoryBean cacheFactoryBean() {
        return new CacheFactoryBean();
    }


    @Bean
    LocalRegionFactoryBean<String, Quote> quoteRegionFactory(final GemFireCache cache,  RegionAttributes<String, Quote> regionAttributes) {
        return new LocalRegionFactoryBean<String, Quote>() {
            {
                setCache(cache);
                setName("quotes");
                setClose(false);
                setAttributes(regionAttributes);
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
