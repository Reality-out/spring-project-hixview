package site.hixview.support.jpa.naming;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@Slf4j
public class TestPrefixPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    @Override
    public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment environment) {
        Identifier identifier = super.toPhysicalTableName(logicalName, environment);
        String tableName = identifier.getText().toLowerCase();
        return new Identifier(TEST_TABLE_PREFIX + tableName, identifier.isQuoted());
    }
}