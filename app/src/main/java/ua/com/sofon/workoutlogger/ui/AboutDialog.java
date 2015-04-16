package ua.com.sofon.workoutlogger.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;
import ua.com.sofon.workoutlogger.R;

/**
 * Dialog shows information about application.
 * @author Dimowner
 */
public class AboutDialog extends DialogFragment {

	private static final String VERSION_UNAVAILABLE = "N/A";

	public AboutDialog() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Get app version
		PackageManager pm = getActivity().getPackageManager();
		String packageName = getActivity().getPackageName();
		String versionName;
		try {
			PackageInfo info = pm.getPackageInfo(packageName, 0);
			versionName = info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			versionName = VERSION_UNAVAILABLE;
		}

		// Build the about body view and append the link to see OSS licenses
		SpannableStringBuilder aboutBody = new SpannableStringBuilder();
		aboutBody.append(Html.fromHtml(getString(R.string.about_body, versionName)));

		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		TextView aboutBodyView = (TextView) layoutInflater.inflate(R.layout.dialog_about, null);
		aboutBodyView.setText(aboutBody);
		aboutBodyView.setMovementMethod(new LinkMovementMethod());

		return new AlertDialog.Builder(getActivity())
				.setTitle("About")
				.setView(aboutBodyView)
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}
				)
				.create();
	}
}
