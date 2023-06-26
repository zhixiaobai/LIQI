package com.xb.mapper;

import com.xb.entity.User;
import com.zxb.liqi.annotation.*;
import com.zxb.liqi.interfaces.BaseRepository;

import java.util.List;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
public interface UserMapper extends BaseRepository<User> {
    /**
     * selectList
     * @param user
     * @param id
     * @return
     */
//    @Select({"select * from dm where username = #{user.username} and password = #{user.password} and id = #{id}",
//            "select * from dm where id = #{id}"})
//    List<User> selectList(@ListParam(itemName = "user", entityClass = User.class) List<User> user, @Param("id") int id);


    @Select("select * from dm where username in (#{user.username}) and password in (#{user.password}) and id = #{id}")
    List<User> selectList(@Param("user") List<User> user, @Param("id") int id);


    @Insert("insert into dm (dm_name, username, password) values (#{user.dmName}, #{user.username}, #{user.password})")
    int insertData(User user);

    @Insert("insert into dm (dm_name, username, password) values (#{user.dmName}, #{user.username}, #{user.password})")
    int insertData2(List<User> user);


    @Update("update dm set username = #{user.username}, dm_name = #{user.dmName} where id = #{user.id}")
    int updateData(User user);

    @Delete("delete from dm where id = #{id}")
    int deleteData(int id);
}
