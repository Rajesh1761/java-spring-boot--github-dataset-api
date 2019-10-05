package com.hackerrank.github.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackerrank.github.model.Actor;

public class ActorDTO {
	private Long id;
	private String login;
	@JsonProperty(value = "avatar_url")
	private String avatar;

	public static ActorDTO convertFrom(Actor actor) {
		return new ActorDTO(actor.getId(), actor.getLogin(), actor.getAvatar());
	}
	
	public ActorDTO(){
		
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "ActorDTO [id=" + id + ", login=" + login + ", avatar=" + avatar
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		ActorDTO other = (ActorDTO) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	public ActorDTO(Long id, String login, String avatar) {
		super();
		this.id = id;
		this.login = login;
		this.avatar = avatar;
	}
	
	
}
