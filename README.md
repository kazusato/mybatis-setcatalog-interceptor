# Calling Connection#setCatalog with MyBatis interceptor (plugin)

## Purpose

We sometimes want to change a database in runtime to dispatch to a tenant-specific database
with a single (non-tenant-specific) SQL when we create a multi-tenant web application.

To change a database to be connected in runtime during a JDBC call,
we need to call Connection#setCatalog for MySQL.

## API Description

### UserApi#listUsers

This API calls catalog-specific mappers: TokyoUserMapper and OsakaUserMapper.
These mappers specify catalog in their SQL statements as shown below.

```
@Mapper
public interface TokyoUserMapper {

    @Select("select * from tokyo.users order by user_id")
    List<UserEntity> listUsers();

}
```

### UserApi#listUsersGeneral

This API calls non-specific catalog mappers: GeneralUserMapper.
This mapper does not specify a catalog in the SQL statement, as shown below.

```
@Mapper
public interface GeneralUserMapper {

    @Select("select * from users order by user_id")
    List<UserEntity> listUsers();

}
```

This means a database (catalog) specified in a connection URL is used if Connection#setCatalog is not called.

## Interceptor design

### Specify catalog through ThreadLocal

Before invocation, there is no interface to pass some arguments to a MyBatis interceptor.
So, we use ThreadLocal to pass catalog information.

- First, we create a class with a ThreadLocal field, setter, and getter to pass a catalog name.
- Next, we call the class's setter to set a catalog name.
- Then, we call a mapper method to retrieve data from a database.

Before calling the mapper method, the interceptor is called automatically, and the specified catalog is set
through Catalog#setCatalog.

### Behavior for null catalog name

If no one sets a catalog name to ThreadLocal, null is returned within the interceptor.
However, we cannot set null to Connection#setCatalog.

We have two options:

- Set a default catalog name
- Throw an exception for null catalog
- Skip calling Connection#setCatalog (then, a catalog specified in a connection URL will be used)

In this sample, the skipping option is chosen.

## Enabling interceptor

To enable an interceptor, we need to specify a class name in a MyBatis configuration file.
The file, mybatis-config.xml, is in src/main/resources and is specified in the mybatis.config-location
parameter in application.yml.

An example of using these files is available in mybatis-spring-boot-sample-xml.

## References

### MyBatis interceptor to call Connection#setCagalog

https://stackoverflow.com/questions/9430294/mybatis-spring-in-a-multi-tenant-application

### Enabling MyBatis configuration XML for Spring Boot

https://github.com/mybatis/spring-boot-starter/tree/master/mybatis-spring-boot-samples/mybatis-spring-boot-sample-xml
