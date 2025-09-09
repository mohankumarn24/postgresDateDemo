package org.zheng.demo.postgresDateDemo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zheng.demo.postgresDateDemo.entity.DateTime;
import org.zheng.demo.postgresDateDemo.repository.DateTimeRepository;

@RestController
public class DemoRestController {

	@Autowired
	private DateTimeRepository demoRepository;

	@GetMapping("/demo")
	public List<DateTime> getDemoDates() {
		return demoRepository.findAll();
	}

	@PostMapping("/demo")
	public DateTime saveDemo() {

		DateTime dateTime = new DateTime();
		dateTime.setLocalDate(LocalDate.now());					// LocalDate
		dateTime.setLocalTime(LocalTime.now());					// LocalTime
		dateTime.setLocalDateTime(LocalDateTime.now());			// LocalDateTime

		ZonedDateTime zonedNow = ZonedDateTime.now();
		dateTime.setZonedDateTime(zonedNow);					// ZonedDateTime
		dateTime.setOffsetDateTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(-6)));		// OffsetDateTime

		return demoRepository.save(dateTime);
	}
}

/***********************************************************************************************************************
Date: 09-SEP-2025
Time: 07:30 PM (19:30:01)

 {
 	"id"			 : 1,
 	"localDate"		 : "2025-09-09",
 	"localTime"		 : "19:30:01.8586053",
 	"localDateTime"	 : "2025-09-09T19:30:01.8586053",
 	"zonedDateTime"	 : "2025-09-09T19:30:01.8586053+05:30",
 	"offsetDateTime" : "2025-09-09T19:30:01.8586053-06:00"
 }

DB Contents: (check last row)
 +-+--+----------+----------+-----------------------+-----------------------------+-----------------------------+
 |#|id|local_date|local_time|local_date_time        |zoned_date_time              |offset_date_time             |
 +-+--+----------+----------+-----------------------+-----------------------------+-----------------------------+
 |1| 1|2025-09-09|  19:30:01|2025-09-09 19:30:01.858|2025-09-09 19:30:01.858 +0530|2025-09-10 07:00:01.858 +0530|
 +-+--+----------+----------+-----------------------+-----------------------------+-----------------------------+

 -- datetimedemo.datetime definition
 -- Drop table
 -- DROP TABLE datetimedemo.datetime;

 CREATE TABLE datetimedemo.datetime (
 	id int4 NOT NULL,
 	local_date date NULL,
 	local_date_time timestamp(6) NULL,
 	local_time time(6) NULL,
 	offset_date_time timestamptz(6) NULL,
 	zoned_date_time timestamptz(6) NULL,
 	CONSTRAINT datetime_pkey PRIMARY KEY (id)
 );

 -- Permissions
 ALTER TABLE datetimedemo.datetime OWNER TO postgres;
 GRANT ALL ON TABLE datetimedemo.datetime TO postgres;


 select
 	id,
 	local_date,
 	local_time,
 	local_date_time,
 	zoned_date_time,
 	offset_date_time
 from
 	datetimedemo.datetime;
 **********************************************************************************************************************/


/***********************************************************************************************************************
package com.notes.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Iso8601Example {

	public static void main(String[] args) {

		// Current date and time in local time zone
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println("LocalDateTime	: " + localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));

		// Current date and time with zone info
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		System.out.println("ZonedDateTime	: " + zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME));

		// Current time with offset (e.g. +05:30)
		OffsetDateTime offsetDateTime = OffsetDateTime.now();
		System.out.println("OffsetDateTime	: " + offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

		// Instant (always in UTC, ends with 'Z')
		Instant instant = Instant.now();
		System.out.println("Instant			: " + instant.toString());

		Date now = new Date();
		System.out.println("java.util.Date	: " + now);
	}
}

/*
LocalDateTime	: 2025-09-07T11:43:04.1861596
ZonedDateTime	: 2025-09-07T11:43:04.1971602+05:30[Asia/Calcutta]
OffsetDateTime	: 2025-09-07T11:43:04.1971602+05:30
Instant			: 2025-09-07T06:13:04.197160200Z
java.util.Date	: Sun Sep 07 11:43:04 IST 2025
 **********************************************************************************************************************/
