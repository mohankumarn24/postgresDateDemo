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

		List<DateTime> datetimes = demoRepository.findAll();
		for (DateTime dateTime : datetimes) {
			System.out.println(String.format("dateTime value: %s", dateTime.toString()));
		}
		//  dateTime value: DateTime{id=353, localDate=2025-09-09, localTime=20:00:19.830, localDateTime=2025-09-09T20:00:19.830218, zonedDateTime=2025-09-09T14:30:19.830218Z, offsetDateTime=2025-09-10T02:00:19.830218Z}

		return datetimes;
	}

	@PostMapping("/demo")
	public DateTime saveDemo() {

		DateTime dateTime = new DateTime();						// Entity class

		dateTime.setLocalDate(LocalDate.now());					// LocalDate=2025-09-09
		dateTime.setLocalTime(LocalTime.now());					// LocalTime=20:00:19.830218
		dateTime.setLocalDateTime(LocalDateTime.now());			// LocalDateTime=2025-09-09T20:00:19.830218						 --> server time
		dateTime.setZonedDateTime(ZonedDateTime.now());			// ZonedDateTime=2025-09-09T20:00:19.830218+05:30[Asia/Calcutta] --> server time with server time zone 		[UTC: 2025-09-09T14:30:19.830218Z] (20:00 - 05:30 = 14:30 hrs)
		dateTime.setOffsetDateTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(-6)));
																// OffsetDateTime=2025-09-09T20:00:19.830218-06:00}  			 --> server time with time zone as -06:00	[UTC: 2025-09-10T02:00:19.830218Z] (20:00 + 06:00 = 26:00 ie., next day 02:00 hrs)
		/*
		dateTime.setOffsetDateTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHoursMinutes(5, 30)));
																// OffsetDateTime=2025-09-09T20:00:19.830218+05:30}  			 --> server time with time zone as +05:30	[UTC: 2025-09-09T14:30:19.830218Z] (20:00 - 05:30 = 14:30 hrs)
		*/

		// save to repository
		DateTime fetchedDateTime = demoRepository.save(dateTime);

		// compare data
		System.out.println(String.format("dateTime value: %s", dateTime.toString()));			// saved to db
		// dateTime value: DateTime{id=353, localDate=2025-09-09, localTime=20:00:19.830218, localDateTime=2025-09-09T20:00:19.830218, zonedDateTime=2025-09-09T20:00:19.830218+05:30[Asia/Calcutta], offsetDateTime=2025-09-09T20:00:19.830218-06:00}
		System.out.println(String.format("dateTime value: %s", fetchedDateTime.toString()));	// fetched from db
		// dateTime value: DateTime{id=353, localDate=2025-09-09, localTime=20:00:19.830218, localDateTime=2025-09-09T20:00:19.830218, zonedDateTime=2025-09-09T20:00:19.830218+05:30[Asia/Calcutta], offsetDateTime=2025-09-09T20:00:19.830218-06:00}

		return fetchedDateTime;
	}
}

/***********************************************************************************************************************
Date: 09-SEP-2025
Time: 08:00 PM (19:50:07)

 // POST Response: (values are not in UTC format)
 {
 	"id"			 : 353,
 	"localDate"		 : "2025-09-09",
 	"localTime"		 : "20:00:19.830218",
 	"localDateTime"	 : "2025-09-09T20:00:19.830218",
 	"zonedDateTime"	 : "2025-09-09T20:00:19.830218+05:30",
 	"offsetDateTime" : "2025-09-09T20:00:19.830218-06:00"
 }

 // GET Response: (values are in UTC format)
 {
 	"id"			 : 353,
 	"localDate"		 : "2025-09-09",
 	"localTime"		 : "20:00:19.83",
 	"localDateTime"	 : "2025-09-09T20:00:19.830218",
 	"zonedDateTime"	 : "2025-09-09T14:30:19.830218Z",
 	"offsetDateTime" : "2025-09-10T02:00:19.830218Z"
 }


 DB Contents: (check last row)
 +-+---+----------+----------+-----------------------+-----------------------------+-----------------------------+
 |#|id |local_date|local_time|local_date_time        |zoned_date_time              |offset_date_time             |
 +-+---+----------+----------+-----------------------+-----------------------------+-----------------------------+
 |1|353|2025-09-09|  20:00:19|2025-09-09 20:00:19.830|2025-09-09 20:00:19.830 +0530|2025-09-10 07:30:19.830 +0530|
 +-+---+----------+----------+-----------------------+-----------------------------+-----------------------------+

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

 -- select query
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
