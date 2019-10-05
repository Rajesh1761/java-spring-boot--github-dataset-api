package com.hackerrank.github.request;

import java.sql.Timestamp;

import com.hackerrank.github.model.Actor;

public class ActorTuple {
	Actor actor;
	Integer cEvents;
	Timestamp last;
	public Actor getActor() {
		return actor;
	}
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	public Integer getcEvents() {
		return cEvents;
	}
	public void setcEvents(Integer cEvents) {
		this.cEvents = cEvents;
	}
	public Timestamp getLast() {
		return last;
	}
	public void setLast(Timestamp last) {
		this.last = last;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((cEvents == null) ? 0 : cEvents.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
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
		ActorTuple other = (ActorTuple) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (cEvents == null) {
			if (other.cEvents != null)
				return false;
		} else if (!cEvents.equals(other.cEvents))
			return false;
		if (last == null) {
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ActorTuple [actor=" + actor + ", cEvents=" + cEvents
				+ ", last=" + last + "]";
	}
	public ActorTuple(Actor actor, Integer cEvents, Timestamp last) {
		super();
		this.actor = actor;
		this.cEvents = cEvents;
		this.last = last;
	}
	public ActorTuple(){
		
	}
	
}
