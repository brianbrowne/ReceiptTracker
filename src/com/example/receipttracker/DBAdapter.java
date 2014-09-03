/*
 * 	This is the adapter that will act as an intermediary between
 * 	the java code and the sql database. The methods used were provided
 * 	with android in order to make database development easier.
 * 	 
 */



package com.example.receipttracker;

import android.content.ContentValues;				import android.content.Context;			import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;		import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {

	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	
	// For logging:
	private static final String TAG = "DBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";			
	public static final int COL_ROWID = 0;
	public static final String KEY_TITLE = "title";	
	public static final String KEY_AUTHOR = "author";				//This is where the fields that will appear in the database are defined
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_MODULE = "module";
	public static final String KEY_COMMENT = "comment";
	
	
	public static final int COL_TITLE = 1;
	public static final int COL_AUTHOR = 2;
	public static final int COL_SUBJECT = 3;						//Each field is assigned a column number (The ID field takes column 0)
	public static final int COL_MODULE = 4;
	public static final int COL_COMMENT = 5;

	//Boiler plate: the keys of every field created is stored in a single string
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_TITLE, KEY_AUTHOR, KEY_SUBJECT, KEY_MODULE, KEY_COMMENT};  
	
	
	//The names of the database and the table we are using.
	public static final String DATABASE_NAME = "MyDb";
	public static final String DATABASE_TABLE = "mainTable";
	public static final int DATABASE_VERSION = 2;				//keeps track of the version of your database in case you change the format later on, this should
																//prevent crashing if you, for example, create a new version with another field added.
	
	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_TITLE + " text not null, "
			+ KEY_AUTHOR + " text not null, "										//The actual database is created
			+ KEY_SUBJECT + " text not null,"										//the field key followed by the type of information it will contain
			+ KEY_MODULE + " text not null,"
			+ KEY_COMMENT + " text not null"
			+ ");";
	
	// Context of application who uses us.
	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	/////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////
	
	public DBAdapter(Context ctx) {
		this.context = ctx;								
		myDBHelper = new DatabaseHelper(context);
	}
	
	
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();			//open a connection to the database
		return this;
	}
	
	
	public void close() {			// Close the database connection.
		myDBHelper.close();
	}
	
	// Add a new set of values to the database:

	public long insertRow(String title, String author, String subject, String module, String comment) { //take in information when method is called
	
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_AUTHOR, author);
		initialValues.put(KEY_SUBJECT, subject);					//allocate the information taken in to each field.
		initialValues.put(KEY_MODULE, module);
		initialValues.put(KEY_COMMENT, comment);
		
		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;						// Delete a row from the database, by rowId (primary key)
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	
	public void deleteAll() {
		Cursor c = getAllRows();									//create cursor and allocate every row to it
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);			//get row index and and store it in a long
		if (c.moveToFirst()) {										//if the cursor can move forward
			do {
				deleteRow(c.getLong((int) rowId));					//delete the row
			} while (c.moveToNext());								//and keep moving to the next
		}
		c.close();													//after close the cursor to prevent resource leak
	}
	
	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;											
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 					//create cursor and assign it the data recieved when every column of a row is queried
							where, null, null, null, null, null);
		if (c != null) {														//if the data returned wasn't null (if we aren't at the end of the database)
			c.moveToFirst();													//move the cursor to the first row
		}
		return c;																//then return all the information in the cursor back to where the method was called
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {									//take in long (the rowID)
		String where = KEY_ROWID + "=" + rowId;							//create string to hold row ID (so we can search for it)
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 			//create cursor and query the database at the rowID
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;														//return the information to where the method was called
	}
	
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String title, String author, String subject, String module, String comment) {
			String where = KEY_ROWID + "=" + rowId;

		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TITLE, title);
		newValues.put(KEY_AUTHOR, author);							//pretty much the same as the insertRow method but using .update instead of .insert
		newValues.put(KEY_SUBJECT, subject);
		newValues.put(KEY_MODULE, subject);
		newValues.put(KEY_COMMENT, subject);
		
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////
	//	Private Helper Classes:
	/////////////////////////////////////////////////////////////////////
	
	/*
	 * 	Private class which handles database creation and upgrading.
	 * 	Used to handle low-level database access.
	 * 
	 * 	Most importantly if the database version is changed (if we add/remove new fields etc) it will destroy the old database
	 * 	and create a new. This was very important as before we added this we were getting problems where everytime we ran the app it would
	 * 	crash as it tried to open the database we created the last time we ran it and append/create/remove fields that no longer existed (because we changed them)
	 * 
	 *	Boiler plate:
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}
