package reconnect.server.global.config.mongo.user;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import reconnect.server.global.config.AbstractMongoConfiguration;
import reconnect.server.global.config.mongo.MongoConfig;

import static com.mongodb.WriteConcern.MAJORITY;

@Configuration
public class UserMongoConfiguration extends AbstractMongoConfiguration {

    private final MongoConfig userMongoConfig;

    public UserMongoConfiguration(@Qualifier("userMongoConfig") UserMongoConfig userMongoConfig) {
        this.userMongoConfig = userMongoConfig;
    }

    @Override
    public MongoConfig getMongoConfig() {
        return userMongoConfig;
    }

    @Bean(name = "userMongoTransactionManager")
    public MongoTransactionManager transactionManager(@Qualifier("userMongoDbFactory") MongoDatabaseFactory dbFactory) {
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(MAJORITY)
                .build();
        return new MongoTransactionManager(dbFactory, txnOptions);
    }

    @Bean(name = "userMongoDbFactory")
    public MongoDatabaseFactory userMongoDbFactory() {
        return super.mongoDbFactory();
    }

    @Bean("userMongoTemplate")
    public MongoTemplate userMongoTemplate(@Qualifier("userMongoDbFactory") MongoDatabaseFactory mongoDbFactory,
                                           MappingMongoConverter mappingMongoConverter) {
        return super.mongoTemplate(mongoDbFactory, mappingMongoConverter);
    }
}
