package ua.com.sofon.workoutlogger.parts;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created on 22.02.2015.
 * @author Dimowner
 */
public class Workout implements Serializable {

	public Workout() {
		this.id = NO_ID;
		this.name = "";
		this.date = new Date();
		this.weight = 0;
		this.duration = 0;
		this.state = STATE_DEFAULT;
		this.comment = "";
		this.exerciseList = new ArrayList<>();
	}

	public Workout(long id, String name, Date date, int weight, int duration,
						String comment, int state, List<Exercise> exerciseList) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.weight = weight;
		this.duration = duration;
		this.state = state;
		this.comment = comment;
		this.exerciseList = exerciseList;
	}

	/**
	 * Check that Workout has ID.
	 * @return Return true if ID not equals NO_ID;
	 */
	public boolean hasID() {
		return  (id != NO_ID);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public String getDateStr() {
		return dateFormat.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<Exercise> getExerciseList() {
		return exerciseList;
	}

//	public List<Long> getPerformedExeIDs() {
//		return performedExeIDs;
//	}
//
//	public void setPerformedExeIDs(List<Long> performedExeIDs) {
//		this.performedExeIDs = performedExeIDs;
//	}

	public void setExerciseList(List<Exercise> exerciseList) {
		this.exerciseList = exerciseList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Workout[ id: '").append(id).append("', ");
		sb.append("name: '").append(name).append("', ");
		sb.append("date: '").append(getDateStr()).append("', ");
		sb.append("weight: '").append(weight).append("', ");
		sb.append("duration: '").append(duration).append("', ");
		sb.append("comment: '").append(comment).append("', ");
		sb.append("state: '").append(state).append("' \n");
		for (Exercise e : exerciseList) {
			sb.append(e.toString()).append(", ");
		}
		sb.append("];");
		return sb.toString();
	}

	private static final long serialVersionUID = 5085350331476735410L;

	public static final int NO_ID = -1;

	/** Date format to show in date field. */
	public static final SimpleDateFormat dateFormat =
			new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

	public static final int STATE_DEFAULT = 0;
	public static final int STATE_ACTIVE = 1;
	public static final int STATE_DONE = 2;

	private long id = NO_ID;
	private String name;
	private Date date;
	private float weight;
	private int duration;
	private String comment;
	private int state = STATE_DEFAULT;
//	private List<Long> performedExeIDs;
	private List<Exercise> exerciseList;
}
