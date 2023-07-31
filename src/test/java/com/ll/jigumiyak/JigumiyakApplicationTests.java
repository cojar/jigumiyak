package com.ll.jigumiyak;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class JigumiyakApplicationTests {

	@Autowired
	private BoardRepository boardRepository;

	@Test
	void contextLoads() {
		Board a = new Board();
		a.setSubject("testsubject");
		a.setContent("testcontent");
		a.setCreateDate(LocalDateTime.now());
		this.boardRepository.save(a);

	}

}
