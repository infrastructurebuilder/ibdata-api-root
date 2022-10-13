# ibdata-database-dialects

IBData dialects are specific implementations of a database driver/Jooq/Liquibase/etc


## I need a new one

The methods for building a new one are simple.  Copy the ibdata-dialect-TEMPLATE to an
item named based on your type.  By default, the hint (passed as the second param of
the constructor) is the same as the JooqDialect. If your hint is different from the
Jooq SQLDialect name, you'll have to override that in you created IBDataDatabaseSupplier.

