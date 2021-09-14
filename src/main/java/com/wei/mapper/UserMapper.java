package com.wei.mapper;

import com.wei.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
     User queryUserByName(String userName);
}
