package org.example.testmysql;

import org.example.testmysql.mapper.UserMapper;
import org.example.testmysql.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TestMysqlApplicationTests {
	@Autowired
	private UserMapper userMapper;

	@Test
	public void test(){
		List<User> list = userMapper.list();
		list.stream().forEach(user ->{
			System.out.println(user);
		});

	}
//	void contextLoads() {
//	}


}
