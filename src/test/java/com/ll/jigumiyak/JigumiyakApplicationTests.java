package com.ll.jigumiyak;

<<<<<<< HEAD
import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


=======
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

>>>>>>> affa3591282f08167ec02591228315de6fde46dc
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
