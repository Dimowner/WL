package ua.com.sofon.workoutlogger.parts;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.com.sofon.workoutlogger.util.DateUtil;

/**
 * Created on 22.02.2015.
 * @author Dimowner
 */
public class Workout extends BaseParticle implements Parcelable {

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

	public Workout(long id, String name, Date date, float weight, int duration,
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

	//----- START Parcelable implementation ----------
	public Workout(Parcel in) {
		id = in.readLong();
		String[] strData = new String[2];
		in.readStringArray(strData);
		name = strData[0];
		comment = strData[1];
		int[] intData = new int[2];
		in.readIntArray(intData);
		duration = intData[0];
		state = intData[1];
		weight = in.readFloat();
		date = (Date) in.readSerializable();
		this.exerciseList = new ArrayList<>();
		in.readList(exerciseList, Exercise.class.getClassLoader());
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeStringArray(new String[]{name, comment});
		out.writeIntArray(new int[]{duration, state});
		out.writeFloat(weight);
		out.writeSerializable(date);
		out.writeList(exerciseList);
	}

	public static final Parcelable.Creator<Workout> CREATOR
			= new Parcelable.Creator<Workout>() {
		public Workout createFromParcel(Parcel in) {
			return new Workout(in);
		}

		public Workout[] newArray(int size) {
			return new Workout[size];
		}
	};
	//----- END Parcelable implementation ----------

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
		return DateUtil.getActiveDateFormat().format(date);
	}

	public String getDateStr(SimpleDateFormat format) {
		return format.format(date);
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
		for (int i = 0; i < exerciseList.size(); i++) {
			sb.append(exerciseList.get(i).toString()).append(", ");
		}
		sb.append("];");
		return sb.toString();
	}

	public static final int STATE_DEFAULT = 0;
	public static final int STATE_ACTIVE = 1;
	public static final int STATE_DONE = 2;

	private String name;
	private Date date;
	private float weight;
	private int duration;
	private String comment;
	private int state = STATE_DEFAULT;
	private List<Exercise> exerciseList;
}
