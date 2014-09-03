package com.example.receipttracker;

import android.app.Activity;				import android.app.Fragment;				import android.content.Intent;
import android.graphics.Bitmap;				import android.graphics.BitmapFactory;		import android.os.Bundle;
import android.view.LayoutInflater;			import android.view.Menu;					import android.view.MenuItem;
import android.view.View;					import android.view.ViewGroup;				import android.widget.ImageView;
import android.widget.RelativeLayout;		import android.widget.TextView;

public class ViewImage extends Activity 
{

	TextView text;		ImageView imageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);

		
		imageview = (ImageView) findViewById(R.id.full_image_view);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(950, 1050);
		imageview.setLayoutParams(layoutParams);
		Intent i = getIntent();
		int position = i.getExtras().getInt("position");
		String[] filepath = i.getStringArrayExtra("filepath");
		String[] filename = i.getStringArrayExtra("filename");
		Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
		imageview.setImageBitmap(bmp);




		//if (savedInstanceState == null) {
		//getFragmentManager().beginTransaction()
		//	.add(R.id.container, new PlaceholderFragment()).commit();
		//}
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_view_image,
					container, false);
			return rootView;
		}
	}

}
