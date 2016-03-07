package ua.com.sofon.workoutlogger.parts;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;
import junit.framework.TestCase;

/**
 * Tests for {@link Exercise} class.
 * @author Dimowner
 */
public class ExerciseTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
		exe = new Exercise(ID, NAME, DESCRIPTION, TYPE, GROUPS);
	}

	@SmallTest
	public void testEquels() {
		assertEquals(exe, new Exercise(ID, NAME, DESCRIPTION, TYPE, GROUPS));
	}

	@SmallTest
	public void testExerciseParcelable() {
		// Obtain a Parcel object and write the parcelable object to it:
		Parcel parcel = Parcel.obtain();
		exe.writeToParcel(parcel, 0);

		// After you're done with writing, you need to reset the parcel for reading:
		parcel.setDataPosition(0);

		// Reconstruct object from parcel and asserts:
		Exercise createdFromParcel = Exercise.CREATOR.createFromParcel(parcel);
		assertEquals(exe, createdFromParcel);
	}

	@SmallTest
	public void testGroupsArrayToStr() {
		assertEquals("0;1;2;3;4;5;6;9", Exercise.groupsArrayToStr(new int[] {0,1,2,3,4,5,6,9}));
	}

	@SmallTest
	public void testGroupsStrToArray() {
		int[] source = new int[] {0,1,2,3,4,5,9};
		int[] dest = Exercise.groupsStrToArray("0;1;2;3;4;5;9");
		assertTrue(source[0] == dest[0]);
		assertTrue(source[1] == dest[1]);
		assertTrue(source[2] == dest[2]);
		assertTrue(source[3] == dest[3]);
		assertTrue(source[4] == dest[4]);
		assertTrue(source[5] == dest[5]);
		assertTrue(source[6] == dest[6]);
	}

	public void tearDown() throws Exception {
	}


	private final int ID = 15;
	private final String NAME = "Exercise";
	private final String DESCRIPTION = "Exercise description text";
	private final int TYPE = 0;
	private final int[] GROUPS = new int[] {0, 5, 3, 9};

	Exercise exe = null;

}