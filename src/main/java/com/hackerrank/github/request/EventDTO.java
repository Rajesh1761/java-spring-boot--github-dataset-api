package com.hackerrank.github.request;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackerrank.github.model.Event;

public class EventDTO {
	private Long id;
	private String type;
	private ActorDTO actor;
	private RepoDTO repo;
	@JsonProperty(value = "created_at")
	private String createdAt;

	public static EventDTO convertFrom(Event event) {
		return new EventDTO(event.getId(), event.getType(),
				ActorDTO.convertFrom(event.getActor()),
				RepoDTO.convertFrom(event.getRepo()), new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").format(new Date(event
						.getCreatedAt().getTime())));
	}

	public EventDTO(){
		
	}
	
	public EventDTO(Long id, String type, ActorDTO actor, RepoDTO repo,
			String createdAt) {
		super();
		this.id = id;
		this.type = type;
		this.actor = actor;
		this.repo = repo;
		this.createdAt = createdAt;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((repo == null) ? 0 : repo.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventDTO other = (EventDTO) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (repo == null) {
			if (other.repo != null)
				return false;
		} else if (!repo.equals(other.repo))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventDTO [id=" + id + ", type=" + type + ", actor=" + actor
				+ ", repo=" + repo + ", createdAt=" + createdAt + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ActorDTO getActor() {
		return actor;
	}

	public void setActor(ActorDTO actor) {
		this.actor = actor;
	}

	public RepoDTO getRepo() {
		return repo;
	}

	public void setRepo(RepoDTO repo) {
		this.repo = repo;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
