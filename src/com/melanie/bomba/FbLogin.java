package com.melanie.bomba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;

public class FbLogin extends FacebookActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fb);
		this.openSession();

	}

	@Override
	protected void onSessionStateChange(SessionState state, Exception exception) {
		// TODO Auto-generated method stub
		super.onSessionStateChange(state, exception);
		if (state.isOpened()) {
			Request fbRequest = Request.newMeRequest(this.getSession(),
					new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							// TODO Auto-generated method stub
							if (user != null) {
								TextView welcome = (TextView) findViewById(R.id.tvUserName);
								welcome.setText("Hi " + user.getName()
										+ " you just logged into Bomba Enjoy!");
								startActivity(new Intent(FbLogin.this, TabView.class));
							}

						}
					});
			Request.executeBatchAsync(fbRequest);
		}

	}

}
