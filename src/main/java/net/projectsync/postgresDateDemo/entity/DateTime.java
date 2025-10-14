package net.projectsync.postgresDateDemo.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "datetime")
public class DateTime {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@Column(name = "localDate")
	private LocalDate localDate;

	@Column(name = "localTime")
	private LocalTime localTime;

	@Column(name = "localDateTime")
	private LocalDateTime localDateTime;

	@Column(name = "zonedDateTime")
	private ZonedDateTime zonedDateTime;

	@Column(name = "offsetDateTime")
	private OffsetDateTime offsetDateTime;
	
	@Column(name = "instant")
	private Instant instant;

	@Override
	public String toString() {
		return "DateTime{" +
				"id=" + id +
				", localDate=" + localDate +
				", localTime=" + localTime +
				", localDateTime=" + localDateTime +
				", zonedDateTime=" + zonedDateTime +
				", offsetDateTime=" + offsetDateTime +
				", instant=" + instant +
				'}';
	}
}
