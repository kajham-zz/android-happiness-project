package com.kajham.happiness;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kajham.happiness.utils.KeyUtils;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {

	private static final int DIALOG_LOGIN_INVALID = 10;
	private static final int DIALOG_LOGIN_FAILED = 20;
	private static final int DIALOG_LOGIN_IN_PROGRESS = 30;
	private static final int DIALOG_SIGNUP_IN_PROGRESS = 40;
	private static final int DIALOG_LOGIN_SUCCESSFUL = 50;
	private static final int DIALOG_SIGNUP_SUCCESSFUL = 60;
	private static final int DIALOG_SIGNUP_FAILED = 70;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Parse.initialize(this, KeyUtils.PARSE_APPLICATION_ID,
				KeyUtils.PARSE_CLIENT_ID);

		// login process
		final TextView userNameTextView = (TextView) findViewById(R.id.user_name_input_field);
		final TextView passwordTextView = (TextView) findViewById(R.id.password_input_field);

		Button loginButton = (Button) findViewById(R.id.login_btn);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseUser.logInInBackground(userNameTextView.getText()
						.toString(), passwordTextView.getText().toString(),
						new LogInCallback() {

							@Override
							public void done(ParseUser user, ParseException e) {

								removeDialog(DIALOG_LOGIN_IN_PROGRESS);

								if (e == null && user != null) {
									// sign up succeeded
								} else if (user == null) {
									// sign up didn't succeed, username and
									// password is invalid
									showDialog(DIALOG_LOGIN_FAILED);
								} else {
									showDialog(DIALOG_LOGIN_INVALID);
								}
							}
						});
				showDialog(DIALOG_LOGIN_IN_PROGRESS);
			}
		});

		// sign up process
		Button signUpButton = (Button) findViewById(R.id.sign_up_btn);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseUser user = new ParseUser();
				user.setUsername(userNameTextView.getText().toString());
				user.setPassword(passwordTextView.getText().toString());
				user.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							showDialog(DIALOG_SIGNUP_SUCCESSFUL);
						} else {
							showDialog(DIALOG_SIGNUP_FAILED);
						}
					}
				});
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_LOGIN_IN_PROGRESS:
			return getProgressDialog("Logging in...");
		case DIALOG_SIGNUP_IN_PROGRESS:
			return getProgressDialog("Creating account...");
		case DIALOG_LOGIN_SUCCESSFUL:
			return getSimpleDialog("Login successful!");
		case DIALOG_SIGNUP_SUCCESSFUL:
			return getSimpleDialog("Signup successful!");
		case DIALOG_SIGNUP_FAILED:
			return getSimpleDialog("Creating account failed!");
		case DIALOG_LOGIN_FAILED:
		case DIALOG_LOGIN_INVALID:
			return getSimpleDialog("Login failed!");

		}

		return super.onCreateDialog(id, args);
	}

	private Dialog getSimpleDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNeutralButton(android.R.string.ok, null);
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	private ProgressDialog getProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(message);
		return dialog;
	}
}