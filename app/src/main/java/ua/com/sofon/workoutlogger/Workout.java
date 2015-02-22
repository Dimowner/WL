package ua.com.sofon.workoutlogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 22.02.2015.
 * @author Dimowner
 */
public class Workout {

	public Workout() {
		this.id = -1;
		this.date = new Date();
		this.weight = 0;
		this.duration = 0;
		this.workoutlist = new ArrayList<>();
	}

	public Workout(long id, Date date, int weight, int duration, List<Exercise> exerciseList) {
		this.id = id;
		this.date = date;
		this.weight = weight;
		this.duration = duration;
		this.workoutlist = exerciseList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<Exercise> getWorkoutlist() {
		return workoutlist;
	}

	public void setWorkoutlist(List<Exercise> workoutlist) {
		this.workoutlist = workoutlist;
	}

	private long id;
	private Date date;
	private int weight;
	private int duration;
	private List<Exercise> workoutlist;

}
