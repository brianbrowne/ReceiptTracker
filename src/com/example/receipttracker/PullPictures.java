package com.example.receipttracker;

import java.io.File;

import android.app.Activity;				import android.app.ActionBar;
import android.app.Fragment;				import android.content.Intent;
import android.graphics.Bitmap;				import android.graphics.BitmapFactory;
import android.os.Bundle;					import android.os.Environment;
import android.view.LayoutInflater;			import android.view.Menu;
import android.view.MenuItem;				import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;				import android.os.Build;

public class PullPictures extends Activity {

	File folder = new File(Environment.getExternalStorageDirectory() + "/MyReceiptTracker");
	private String[] FilePathStrings;
	private String[] FileNameStrings;
	private File[] listFile;
	GridView grid;
	GridViewAdapter adapter;
	File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pull_pictures);

		if (folder.isDirectory()) {
			listFile = folder.listFiles();
			// Create a String array for FilePathStrings
			FilePathStrings = new String[listFile.length];
			// Create a String array for FileNameStrings
			FileNameStrings = new String[listFile.length];

			for (int i = 0; i < listFile.length; i++) {
				// Get the path of the image file
				FilePathStrings[i] = listFile[i].getAbsolutePath();
				// Get the name image file
				FileNameStrings[i] = listFile[i].getName();
				
			}
		}

		// Locate the GridView in gridview_main.xml
		grid = (GridView) findViewById(R.id.gridview);
		// Pass String arrays to LazyAdapter Class
		adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
		// Set the LazyAdapter to the GridView
		grid.setAdapter(adapter);

		// Capture gridview item click
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(PullPictures.this, ViewImage.class);
				// Pass String arrays FilePathStrings
				i.putExtra("filepath", FilePathStrings);
				// Pass String arrays FileNameStrings
				i.putExtra("filename", FileNameStrings);
				// Pass click position
				i.putExtra("position", position);
				startActivity(i);
			}

		});
		
		//if (savedInstanceState == null) {
		//getFragmentManager().beginTransaction()
		//	.add(R.id.container, new PlaceholderFragment()).commit();
		//}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pull_pictures, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_pull_pictures,
					container, false);
			return rootView;
		}
	}

}
