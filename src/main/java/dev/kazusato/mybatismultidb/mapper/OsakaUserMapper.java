package dev.kazusato.mybatismultidb.mapper;

import dev.kazusato.mybatismultidb.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OsakaUserMapper {

    @Select("select * from osaka.users order by user_id")
    List<UserEntity> listUsers();

}
