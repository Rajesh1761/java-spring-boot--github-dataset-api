package com.hackerrank.github.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.github.model.Actor;
import com.hackerrank.github.model.Event;
import com.hackerrank.github.model.Repo;
import com.hackerrank.github.repository.ActorRepository;
import com.hackerrank.github.repository.EventRepository;
import com.hackerrank.github.repository.RepoRepository;
import com.hackerrank.github.request.ActorDTO;
import com.hackerrank.github.request.ActorTuple;
import com.hackerrank.github.request.EventDTO;
import com.hackerrank.github.request.RepoDTO;

@RestController
public class GithubApiRestController {

	private final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private final EventRepository eventRepository;
	private final ActorRepository actorRepository;
	private final RepoRepository repoRepository;

	public GithubApiRestController(EventRepository eventRepository,
			ActorRepository actorRepository, RepoRepository repoRepository) {
		this.eventRepository = eventRepository;
		this.actorRepository = actorRepository;
		this.repoRepository = repoRepository;
	}

	@DeleteMapping(value = "/erase")
	public ResponseEntity deleteEvents() {
		eventRepository.deleteAll();
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/events")
	public ResponseEntity addEvent(@RequestBody EventDTO body) {

		if (Objects.nonNull(eventRepository.findOne(body.getId()))) {
			return ResponseEntity.badRequest().build();
		}

		ActorDTO actorDTO = body.getActor();
		Actor actor = new Actor(actorDTO.getId(), actorDTO.getLogin(),
				actorDTO.getAvatar());
		actorRepository.save(actor);

		RepoDTO repoDTO = body.getRepo();
		Repo repo = new Repo(repoDTO.getId(), repoDTO.getName(),
				repoDTO.getUrl());
		repoRepository.save(repo);

		Timestamp timestamp;
		try {
			timestamp = new Timestamp(format.parse(body.getCreatedAt())
					.getTime());
		} catch (ParseException e) {
			timestamp = new Timestamp(Instant.now().toEpochMilli());
		}

		Event event = new Event(body.getId(), body.getType(), actor, repo,
				timestamp);
		eventRepository.save(event);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping(value = "/events", produces = "application/json")
	public ResponseEntity<List<EventDTO>> getAllEvents() {

		List<Event> events = eventRepository.findAll(new Sort(
				Sort.Direction.ASC, "id"));

		return events.isEmpty() ? ResponseEntity.ok(new ArrayList<>())
				: ResponseEntity.ok(events.stream().map(EventDTO::convertFrom)
						.collect(Collectors.toList()));
	}

	@GetMapping(value = "/events/actors/{actorID}", produces = "application/json")
	public ResponseEntity<List<EventDTO>> getAllEventsByActorId(
			@PathVariable Long actorID) {
		Actor actor = actorRepository.findOne(actorID);
		if (isNull(actor)) {
			return ResponseEntity.notFound().build();
		}
		List<Event> events = eventRepository
				.findAllByActorIdOrderByIdAsc(actorID);

		return events.isEmpty() ? ResponseEntity.ok(new ArrayList<>())
				: ResponseEntity.ok(events.stream().map(EventDTO::convertFrom)
						.collect(Collectors.toList()));
	}

	private boolean isNull(Actor actor) {
		if (actor == null) {
			return true;
		} else {
			return false;
		}
	}

	@PutMapping(value = "/actors", produces = "application/json")
	public ResponseEntity<ActorDTO> updateActorAvatarURL(
			@RequestBody ActorDTO body) {
		Actor actor = actorRepository.findOne(body.getId());

		if (Objects.isNull(actor)) {
			return ResponseEntity.notFound().build();
		}
		if (!body.getLogin().equals(actor.getLogin())) {
			return ResponseEntity.badRequest().build();
		}
		actor.setAvatar(body.getAvatar());
		Actor actorUpdated = actorRepository.save(actor);
		return ResponseEntity.ok(ActorDTO.convertFrom(actorUpdated));
	}

	@GetMapping(value = "/actors", produces = "application/json")
	public ResponseEntity<List<ActorDTO>> getActors() {
		List<Event> events = eventRepository.findAll();
		List<Actor> actors = actorRepository.findAll();

		List<ActorTuple> actorTuples = new ArrayList<>();

		for (Actor actor : actors) {
			List<Event> collect = events
					.stream()
					.filter(event -> event.getActor().equals(actor))
					.sorted(Comparator.comparing(Event::getCreatedAt)
							.reversed()).collect(Collectors.toList());
			if (!collect.isEmpty()) {
				actorTuples.add(new ActorTuple(actor, collect.size(), collect
						.get(0).getCreatedAt()));
			}
		}

		List<ActorDTO> actorList = getCollectionWithCriteria(actorTuples);

		return ResponseEntity.ok(actorList);
	}

	@GetMapping(value = "/actors/streak", produces = "application/json")
	public ResponseEntity<List<ActorDTO>> getActorsStreak() {
		List<Event> events = eventRepository.findAll();
		List<Actor> actors = actorRepository.findAll();

		List<ActorTuple> actorTupleStreaks = new ArrayList<>();

		for (Actor actor : actors) {
			List<Event> collect = events
					.stream()
					.filter(event -> event.getActor().equals(actor)
							&& event.getType().equals("PushEvent"))
					.sorted(Comparator.comparing(Event::getCreatedAt)
							.reversed()).collect(Collectors.toList());

			if (!collect.isEmpty()) {
				if (collect.size() == 1) {
					actorTupleStreaks.add(new ActorTuple(actor, 0, collect.get(
							0).getCreatedAt()));
				} else {
					Integer mayorStreak = getStreak(collect);
					actorTupleStreaks.add(new ActorTuple(actor, mayorStreak,
							collect.get(0).getCreatedAt()));
				}
			}
		}
		List<ActorDTO> actorList = getCollectionWithCriteria(actorTupleStreaks);
		return ResponseEntity.ok(actorList);
	}

	private int getStreak(List<Event> collect) {
		int mayorStreak = 0;
		int streak = 0;
		for (int i = collect.size() - 1; i > 0; i--) {
			LocalDateTime currentDate = collect.get(i).getCreatedAt()
					.toLocalDateTime();
			LocalDateTime nextDate = collect.get(i - 1).getCreatedAt()
					.toLocalDateTime();
			LocalDateTime currentDateEndOfDay = currentDate.with(
					ChronoField.NANO_OF_DAY, LocalTime.MAX.toNanoOfDay());

			long hours = ChronoUnit.HOURS.between(currentDate, nextDate);
			long hoursFinalDay = ChronoUnit.HOURS.between(currentDate,
					currentDateEndOfDay);
			long days = ChronoUnit.DAYS.between(currentDate, nextDate);

			if (currentDate.getDayOfMonth() == nextDate.getDayOfMonth()
					|| days > 1) {
				streak = 0;
			} else if (hours - hoursFinalDay <= 24) {
				streak++;
				if (streak > mayorStreak)
					mayorStreak = streak;
			}
		}
		return mayorStreak;
	}

	private List<ActorDTO> getCollectionWithCriteria(
			List<ActorTuple> actorTuples) {
		return actorTuples
				.stream()
				.sorted(Comparator.comparing(o -> o.getActor().getLogin()))
				.sorted(Comparator.comparing(ActorTuple::getLast).reversed())
				.sorted(Comparator.comparing(ActorTuple::getcEvents).reversed())
				.map(ActorTuple::getActor).map(ActorDTO::convertFrom)
				.collect(Collectors.toList());
	}
}
