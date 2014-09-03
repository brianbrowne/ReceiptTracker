package com.example.receipttracker;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import com.example.receipttracker.R;

public class PullDatabase extends ActionBarActivity {

	//Button displaydatabase;
	Button deletedatabase;		//initialise some buttons and views.
	//TextView databaseshow;
	ListView list;
	DBAdapter myDb;

	int id = 0;
	String author;	String subject;	
	String module; String comment; String name;

	private void openDB() {
		myDb = new DBAdapter(this);		//make a method that when called opens the sql database through the adapter we have written
		myDb.open();
	}
	private void closeDB() {
		myDb.close();					//make a method that will close the database after we are done.
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pull_database);

		openDB();													//open the database
		//displaydatabase = (Button)findViewById(R.id.ddb);			//set the initialised buttons and views to the actual UI elements.
		deletedatabase = (Button)findViewById(R.id.deletedb);
		//databaseshow = (TextView)findViewById(R.id.databaseshow);
		list = (ListView)findViewById(R.id.list_view);

		Cursor cursor = myDb.getAllRows();				//set the cursor to select all of the rows (all the information in the database)
		displayRecordSet(cursor);   




		list.addHeaderView(new View(this));
		list.addFooterView(new View(this));





		//if (savedInstanceState == null) {
		//getSupportFragmentManager().beginTransaction()
		//	.add(R.id.container, new PlaceholderFragment()).commit();
		//}
	}

	protected void onDestroy() {
		super.onDestroy();				//when the activity is closed
		closeDB();						//close the database
	}

	public void onClickdisplaydatabase(View v){			//when you click "Display Database"

		//displayText("Clicked display record!");			//display confirmation text

		Cursor cursor = myDb.getAllRows();				//set the cursor to select all of the rows (all the information in the database)
		displayRecordSet(cursor);						//pass the cursor with the rows to the method that displays it on screen

	}

	private void displayRecordSet(Cursor cursor) {			//take in a cursor holding certain rows/columns
		String message = "";
		// Create a String that will be populated by the cursor and printed by the method.

		// Reset cursor to start, checking to see if there's data available in the database.
		//for (int i = 0; i <= 500; i++)
		//{

			if (cursor.moveToFirst()) 
			{
				do {
					// Process the database:

					int id = cursor.getInt(DBAdapter.COL_ROWID);
					String name = cursor.getString(DBAdapter.COL_TITLE);
					String author = cursor.getString(DBAdapter.COL_AUTHOR);				//pass the information from each column of the row to the cursor
					String subject = cursor.getString(DBAdapter.COL_SUBJECT);
					String module = cursor.getString(DBAdapter.COL_SUBJECT);
					String comment = cursor.getString(DBAdapter.COL_SUBJECT);


					//BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());

					//CardItemData data = new CardItemData("id=" + id +", Shop=" + name +", Date=" + author +", Cost=" + subject  +"." + module  +", Comment=" + comment  +"\n", comment, comment, comment, comment, comment);
					//adapter.addItem(data, false);



					//list.setAdapter(adapter);




					// Append data to the message:
					message += ", Shop=" + name
					   +", Date=" + author
					 +", Cost=" + subject  +"." + module
					+", Comment=" + comment
					+"\n";
				} while(cursor.moveToNext());		//while there is still data move the cursor to the next row.

			//}

			// Close the cursor to avoid a resource leak.
				cursor.close();
		}
		
		displayText(message);	
		//pass all the information stored in the cursor (all the rows that were parsed) to the method that will display on the text view.
	}


	private void displayText(String message) {									//take in string
		TextView textView = (TextView) findViewById(R.id.databaseshow);			//find the text view through its ID
		textView.setText(message);												//display the string to the text view
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pull_database, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_pull_database,
					container, false);
			return rootView;
		}
	}

}
