package org.zheng.demo.postgresDateDemo.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zheng.demo.postgresDateDemo.entity.DateTime;

@SpringBootTest
class DateTimeRepositoryTest {

	@Autowired
	private DateTimeRepository dateTimeRepository;

	@Test
	void test_save_found() {

		DateTime dateTime = new DateTime();						// Entity class

		dateTime.setLocalDate(LocalDate.now());					// LocalDate
		dateTime.setLocalTime(LocalTime.now());					// LocalTime
		dateTime.setLocalDateTime(LocalDateTime.now());			// LocalDateTime
		dateTime.setZonedDateTime(ZonedDateTime.now());			// ZonedDateTime
		dateTime.setOffsetDateTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(-6)));		// OffsetDateTime
		dateTimeRepository.save(dateTime);

		List<DateTime> found = dateTimeRepository.findAll();
		assertTrue( found.size() > 0);
	}
}
