package ua.com.sofon.workoutlogger;

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
		this.comment = "";
		this.exerciseList = new ArrayList<>();
	}

	public Workout(long id, String name, Date date, int weight, int duration,
						String comment, List<Exercise> exerciseList) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.weight = weight;
		this.duration = duration;
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
		return dateFormat.format(new Date());
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

	public List<Exercise> getExerciseList() {
		return exerciseList;
	}

	public void setExerciseList(List<Exercise> exerciseList) {
		this.exerciseList = exerciseList;
	}

	private static final long serialVersionUID = 5085350331476735410L;

	public static final int NO_ID = -1;

	/** Date format to show in date field. */
	public static final SimpleDateFormat dateFormat =
			new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
	private long id = NO_ID;
	private String name;
	private Date date;
	private float weight;
	private int duration;
	private String comment;
	private List<Exercise> exerciseList;
}
