package org.zheng.demo.postgresDateDemo.controller;

import java.time.Instant;
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
		// dateTime value: DateTime{id=2, localDate=2025-09-18, localTime=18:09:47.468, localDateTime=2025-09-18T18:09:47.467586, zonedDateTime=2025-09-18T12:39:47.467586Z, offsetDateTime=2025-09-19T00:09:47.467586Z, instant=2025-09-18T12:39:47.467586Z}
		// always fecthes values in UTC only
		return datetimes;
	}

	@PostMapping("/demo")
	public DateTime saveDemo() {

		DateTime dateTime = new DateTime();

		dateTime.setLocalDate(LocalDate.now());					// LocalDate=2025-09-18
		dateTime.setLocalTime(LocalTime.now());					// LocalTime=18:09:47.467585900
		dateTime.setLocalDateTime(LocalDateTime.now());			// LocalDateTime=2025-09-18T18:09:47.467585900
		dateTime.setZonedDateTime(ZonedDateTime.now());			// ZonedDateTime=2025-09-18T18:09:47.467585900+05:30[Asia/Calcutta]	(already in local server time zone)
		dateTime.setOffsetDateTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(-6)));
																// OffsetDateTime=2025-09-18T18:09:47.467585900-06:00 (add +06:00 hrs to get UTC date time. Then add +05:30 to get date time in local server time zone)
		dateTime.setInstant(Instant.now());						// 2025-09-18T12:39:47.467585900Z (add +05:30 to get date time in local server time zone))						

		// save to repository
		DateTime fetchedDateTime = demoRepository.save(dateTime);

		// compare data
		System.out.println(String.format("dateTime value: %s", dateTime.toString()));			// saved to db
		// dateTime value: DateTime{id=2, localDate=2025-09-18, localTime=18:09:47.467585900, localDateTime=2025-09-18T18:09:47.467585900, zonedDateTime=2025-09-18T18:09:47.467585900+05:30[Asia/Calcutta], offsetDateTime=2025-09-18T18:09:47.467585900-06:00, instant=2025-09-18T12:39:47.467585900Z}
		System.out.println(String.format("dateTime value: %s", fetchedDateTime.toString()));	// fetched from db
		// dateTime value: DateTime{id=2, localDate=2025-09-18, localTime=18:09:47.467585900, localDateTime=2025-09-18T18:09:47.467585900, zonedDateTime=2025-09-18T18:09:47.467585900+05:30[Asia/Calcutta], offsetDateTime=2025-09-18T18:09:47.467585900-06:00, instant=2025-09-18T12:39:47.467585900Z}


		return fetchedDateTime;
	}
}

/**
 * https://chatgpt.com/share/68cc4c77-3f48-8004-9b87-99f272b8da23
 * https://chatgpt.com/share/68cc4c88-244c-8004-93ce-2cf36fafd48f
 * https://claude.ai/share/3a4b9208-5b81-4306-9eba-2876e459fb0d
 * 
 * https://medium.com/elca-it/how-to-get-time-zones-right-with-java-8dea13aabe5c
 * https://medium.com/@davoud.badamchi/tackling-timezones-in-java-a-comprehensive-guide-for-developers-5bad69b2c079
 * https://medium.com/@ysrgozudeli/mastering-datetime-management-in-java-spring-boot-for-cloud-applications-6c16ef7b0667
 * 
 * Notes:
 * PDF's located at github/javanotes/myresources/pdf's/Time
 */

/***********************************************************************************************************************
Date: 18-SEP-2025
Time: 18:09:47

 // POST Response: (values are not in UTC format)
{
    "id": 2,
    "localDate": "2025-09-18",
    "localTime": "18:09:47.4675859",
    "localDateTime": "2025-09-18T18:09:47.4675859",
    "zonedDateTime": "2025-09-18T18:09:47.4675859+05:30",
    "offsetDateTime": "2025-09-18T18:09:47.4675859-06:00",
    "instant": "2025-09-18T12:39:47.467585900Z"
}

 // GET Response: (values are in UTC format)
{
    "id": 2,
    "localDate": "2025-09-18",
    "localTime": "18:09:47.468",
    "localDateTime": "2025-09-18T18:09:47.467586",
    "zonedDateTime": "2025-09-18T12:39:47.467586Z",
    "offsetDateTime": "2025-09-19T00:09:47.467586Z",
    "instant": "2025-09-18T12:39:47.467586Z"
}


 DB Contents: (check last row)
+-+--+----------+----------+-----------------------+-----------------------------+-----------------------------+-----------------------------+
|#|id|local_date|local_time|local_date_time        |zoned_date_time              |offset_date_time             |instant                      |
+-+--+----------+----------+-----------------------+-----------------------------+-----------------------------+-----------------------------+
|1| 2|2025-09-18|  18:09:47|2025-09-18 18:09:47.467|2025-09-18 18:09:47.467 +0530|2025-09-19 05:39:47.467 +0530|2025-09-18 18:09:47.467 +0530|
+-+--+----------+----------+-----------------------+-----------------------------+-----------------------------+-----------------------------+

 -- datetimedemo.datetime definition
 -- Drop table
 -- DROP TABLE datetimedemo.datetime;
 CREATE TABLE datetime (
	id int4 NOT NULL,
	local_date date NULL,
	local_time time(6) NULL,
	local_date_time timestamp(6) NULL,			--> Stores raw date-time, no offset or zone info.
	zoned_date_time timestamptz(6) NULL,		--> timestamptz: stores value in UTC internally (but will convert to the client’s session time zone when queried)
	offset_date_time timestamptz(6) NULL,		--> timestamptz: stores value in UTC internally (but will convert to the client’s session time zone when queried)
	instant timestamptz(6) NULL,
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
 	offset_date_time,
 	instant
 from
 	datetimedemo.datetime;
 **********************************************************************************************************************/


/***********************************************************************************************************************
package org.zheng.demo.postgresDateDemo.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.OffsetDateTime;
import java.time.Instant;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class Iso8601Example {

	public static void main(String[] args) {

		// Current Date
		LocalDate localDate = LocalDate.now();
		System.out.println("LocalDate	: " + localDate);																// LocalDate		: 2025-09-18
		
		// Current Time
		LocalTime localTime = LocalTime.now();
		System.out.println("LocalTime	: " + localTime);																// LocalTime		: 17:51:01.397524200
		
		// Current date and time in local time zone
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println("\nLocalDateTime	: " + localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));				// LocalDateTime	: 2025-09-18T17:49:15.0294723
		System.out.println("LocalDateTime	: " + localDateTime);														// LocalDateTime	: 2025-09-18T17:49:15.029472300

		// Current date and time with zone info
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		System.out.println("\nZonedDateTime	: " + zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME));				// ZonedDateTime	: 2025-09-18T17:51:01.4015235+05:30[Asia/Calcutta]
		System.out.println("ZonedDateTime	: " + zonedDateTime);														// ZonedDateTime	: 2025-09-18T17:51:01.401523500+05:30[Asia/Calcutta]

		// Current time with offset (e.g. +05:30)
		OffsetDateTime offsetDateTime = OffsetDateTime.now();
		System.out.println("\nOffsetDateTime	: " + offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));	// OffsetDateTime	: 2025-09-18T17:51:01.4015235+05:30
		System.out.println("OffsetDateTime	: " + offsetDateTime);														// OffsetDateTime	: 2025-09-18T17:51:01.401523500+05:30

		// Instant (always in UTC, ends with 'Z')
		Instant instant = Instant.now();
		System.out.println("\nInstant		: " + instant.toString());													// Instant			: 2025-09-18T12:21:01.402523Z

		Date now = new Date();
		System.out.println("\njava.util.Date	: " + now);																// java.util.Date	: Thu Sep 18 17:51:01 IST 2025
	}
}

/*
LocalDate		: 2025-09-18
LocalTime		: 17:51:01.397524200

LocalDateTime	: 2025-09-18T17:51:01.3975242
LocalDateTime	: 2025-09-18T17:51:01.397524200

ZonedDateTime	: 2025-09-18T17:51:01.4015235+05:30[Asia/Calcutta]
ZonedDateTime	: 2025-09-18T17:51:01.401523500+05:30[Asia/Calcutta]

OffsetDateTime	: 2025-09-18T17:51:01.4015235+05:30
OffsetDateTime	: 2025-09-18T17:51:01.401523500+05:30

Instant			: 2025-09-18T12:21:01.402523Z

java.util.Date	: Thu Sep 18 17:51:01 IST 2025
 **********************************************************************************************************************/
