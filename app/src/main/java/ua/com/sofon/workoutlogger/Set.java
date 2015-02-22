package ua.com.sofon.workoutlogger;

/**
 * Created on 22.02.2015.
 * @author Dimowner
 */
public class Set {

	public Set(long id, int setNumber, int reps, int weight) {
		this.id = id;
		this.setNumber = setNumber;
		this.reps = reps;
		this.weight = weight;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSetNumber() {
		return setNumber;
	}

	public void setSetNumber(int setNumber) {
		this.setNumber = setNumber;
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	private long id;
	private int setNumber;
	private int reps;
	private int weight;
}
