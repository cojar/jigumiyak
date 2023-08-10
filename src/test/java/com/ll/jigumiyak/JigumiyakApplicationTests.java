package com.ll.jigumiyak;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.address.AddressService;
import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardRepository;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class JigumiyakApplicationTests {

	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;

	@Test
	@DisplayName("User Signup #1")
	void t1() {
		String loginId = "asdf1234";
		String password = "asdf1234";
		String email = "asd@asd";
		Address address = null;

		SiteUser user = this.userService.create(loginId, password, email, address);
	}

	@Test
	@DisplayName("User Signup #2")
	void t2() {
		String loginId = "asdf12345";
		String password = "asdf12345";
		String email = "asdf@asdf";
		Integer zoneCode = 12345;
		String mainAddress = "대전 서구";
		String subAddress = "굿모닝";

		Address address = this.addressService.create(zoneCode, mainAddress, subAddress);
		SiteUser user = this.userService.create(loginId, password, email, address);
	}
}
