package site.hixview.support.naming;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPrefixPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
    private static final String testTablePrefix = "test_";
    private static final Logger log = LoggerFactory.getLogger(TestPrefixPhysicalNamingStrategy.class);

    @Override
    public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment environment) {
        Identifier identifier = super.toPhysicalTableName(logicalName, environment);
        String tableName = identifier.getText().toLowerCase();
        return new Identifier(testTablePrefix + tableName, identifier.isQuoted());
    }
}