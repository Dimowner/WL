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
		this.comment = "";
		this.exerciseList = new ArrayList<>();
	}

	public Workout(long id, String name, String comment, List<Exercise> exerciseList) {
		this.id = id;
		this.name = name;
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
		this.exerciseList = new ArrayList<>();
		in.readList(exerciseList, Exercise.class.getClassLoader());
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeStringArray(new String[]{name, comment});
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Exercise> getExerciseList() {
		return exerciseList;
	}

	public int getExercisesCount() {
		if (exerciseList != null) {
			return exerciseList.size();
		}
		return 0;
	}

	public void setExerciseList(List<Exercise> exerciseList) {
		this.exerciseList = exerciseList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Workout[ id: '").append(id).append("', ");
		sb.append("name: '").append(name).append("', ");
		sb.append("comment: '").append(comment).append("', Exercises:[");
		for (int i = 0; i < exerciseList.size(); i++) {
			sb.append(exerciseList.get(i).toString()).append(", ");
		}
		sb.append("]];");
		return sb.toString();
	}

	private String name;
	private String comment;
	private List<Exercise> exerciseList;
}
