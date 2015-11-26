package io.pivotal.springtrader.quotes;


import io.pivotal.springtrader.quotes.domain.Quote;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.GemfireCacheManager;
import com.gemstone.gemfire.cache.Cache;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableGemfireRepositories
public class QuotesApplication {

    @Bean
    CacheFactoryBean cacheFactoryBean() {
        return new CacheFactoryBean();
    }

    @Bean
    LocalRegionFactoryBean<java.lang.String, Quote> quotesRegionFactoryBean(final Cache cache) {
        return new LocalRegionFactoryBean<java.lang.String, Quote>() {{
            setName("quotes");
            setCache(cache);
            setEnableGateway(false);
            setRegionName("quotes");
        }};
    }

    @Bean
    GemfireCacheManager cacheManager(final Cache gemfireCache) {
        return new GemfireCacheManager() {{
            setCache(gemfireCache);
        }};
    }

    public static void main(String[] args) {
        SpringApplication.run(QuotesApplication.class,args);
    }
}