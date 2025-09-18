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
 * ZonedDateTime
 *	- Represents a date-time with a time zone ID (e.g., Europe/Paris).
 *	- Can resolve historical/future daylight saving rules.
 *	- Heavier object, more suitable for user-facing "local time" display.
 *	
 * OffsetDateTime
 *	- Represents a date-time with an offset from UTC (e.g., 2025-09-18T12:34:56+02:00).
 *	- Encodes the offset, but not the full time zone rules (no daylight saving changes, etc.).
 *	- Useful when you care about the offset at that moment but not the evolving rules of a time zone.
 *
 * Instant
 *	- Represents a single point in time in UTC (basically a timestamp like 2025-09-18T12:34:56Z).
 *	- Has no concept of time zone or calendar system.
 *	- Best for absolute moments in time (e.g., event logs, database records).
 *  - You can always convert Instant → ZonedDateTime later when you want to display in a user’s local time.
 *  - Easy to sort, compare, and correlate across distributed systems.
 *  - Captures the exact point in time in UTC, independent of location.
 *
 *
 * Summary Recommendation:
 * - Logging: Instant
 * - Database (Postgres): TIMESTAMPTZ, mapped from Instant
 * - Business logic / UI: Use ZonedDateTime when you need to show user-friendly local times (e.g., "Europe/Paris").
 * - Business logic / UI: Use ZonedDateTime when you need to show user-friendly local times (e.g., "Europe/Paris").
 * 
 * Note:
 * Postgres timestamp without time zone 				→ Stores raw date-time, no offset or zone info.
 * Postgres timestamp with time zone (aka timestamptz) 	→ Always stored in UTC internally, but will convert to the client’s session time zone when queried.
 * 
 * Warnings:
 * ⚠️ Avoid storing ZonedDateTime directly — Postgres doesn’t track the named time zone, only UTC (and your client session zone when displaying).
 * ⚠️ Avoid OffsetDateTime unless you explicitly need the offset preserved (rare in most systems).
 */

/***********************************************************************************************************************
Date: 09-SEP-2025
Time: 08:00 PM (19:50:07)

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
