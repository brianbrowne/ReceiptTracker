package com.example.receipttracker;

import android.support.v7.app.ActionBarActivity;	import android.support.v4.app.Fragment;			import android.net.Uri;
import android.os.Bundle;						
import android.os.Environment;						import android.provider.MediaStore;				import android.view.LayoutInflater;				
import android.view.Menu;							import android.view.MenuItem;					import android.view.View;						
import android.view.ViewGroup;						import android.view.View.OnClickListener;		import android.content.Intent;					
import android.database.Cursor;						import android.graphics.Bitmap;					import android.graphics.Bitmap.CompressFormat;	
import android.graphics.BitmapFactory;
import android.text.Editable;						

import java.io.ByteArrayOutputStream;
import java.io.File;							
import java.io.FileOutputStream;					import java.io.IOException;						import java.lang.String;						
import java.util.Calendar;

import android.text.TextWatcher;					import android.widget.Button;					import android.widget.EditText;					
import android.widget.ImageButton;					import android.widget.ImageView;				import android.widget.Toast;

public class MainActivityReceiptTracker extends ActionBarActivity implements TextWatcher{

	Button submitButton;    EditText Etitle;	EditText Eauthor;    EditText Esubject;		EditText Emodule;    EditText Ecomment;

	String title;    String author;	String subject;	String module;    String comment;

	File folder;    Bitmap pic;	DBAdapter myDb;		ImageView imageView1;		FileOutputStream fOut;

	//private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;	
	Intent i;	private ImageButton ib;
	
	public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_receipt_tracker);


		Button FindYourRoom = (Button) findViewById(R.id.GoToDB);

		FindYourRoom.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PullDatabase.class);
				startActivityForResult(intent, 0);
			}
		});
		
		Button GoToPicture = (Button) findViewById(R.id.GoToPicture);

		GoToPicture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent2 = new Intent(v.getContext(), PullPictures.class);
				startActivityForResult(intent2, 0);
			}
		});

		imageView1 = (ImageView) findViewById(R.id.imageView1);

		submitButton = (Button) findViewById(R.id.CSSubmit);
		Etitle = (EditText) findViewById(R.id.editBookTitle);
		Eauthor = (EditText) findViewById(R.id.editBookAuthor);                //assign the initialised elements to the actual UI elements using the element ID's
		Esubject = (EditText) findViewById(R.id.editBookSubject);
		Emodule = (EditText) findViewById(R.id.editModuleCode);
		Ecomment = (EditText) findViewById(R.id.editComment);


		Etitle.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (Etitle.getText().toString().matches("[A-Z]*[a-z]*||[a-z]*[A-Z]*") && s.length() > 0) {
					Toast.makeText(getApplicationContext(), "Shop name is valid", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Shop name is invalid", Toast.LENGTH_SHORT).show();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		Eauthor.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (Eauthor.getText().toString().matches("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)") && s.length() > 0) {
					Toast.makeText(getApplicationContext(), "Date valid", Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getApplicationContext(),"Date is invalid",Toast.LENGTH_SHORT).show();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		Esubject.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				try {
					if (Esubject.getText().toString().matches("[0-9]*") && s.length() > 0) {
						Toast.makeText(getApplicationContext(), "Euro amount is valid", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "Euro amount is invalid", Toast.LENGTH_SHORT).show();
					}
				} catch (NumberFormatException e) {
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		Emodule.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				try {
					if (Esubject.getText().toString().matches("[0-9]*") && Esubject.length() >= 2) {
						Toast.makeText(getApplicationContext(), "Cent amount is valid", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "Cent amount is invalid", Toast.LENGTH_SHORT).show();
					}
				} catch (NumberFormatException e) {
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		Ecomment.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (Ecomment.getText().toString().matches("[A-Z]*[a-z]*||[a-z]*[A-Z]*") && s.length() > 0) {
					Toast.makeText(getApplicationContext(), "Comment is valid", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Comment is invalid", Toast.LENGTH_SHORT).show();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});


		openDB();


		folder = new File(Environment.getExternalStorageDirectory() + "/MyReceiptTracker");
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		if (success) {
			//Toast.makeText(this, "File created", Toast.LENGTH_LONG).show();
		} else {
			// Do something else on failure 
		}

		ib = (ImageButton) findViewById(R.id.buttonToast);

		/*ib.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i, CAPTURE_IMAGE_CAPTURE_CODE);
			}
		});*/

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		
		Calendar c = Calendar.getInstance(); 
        int seconds = c.get(Calendar.SECOND);
		 
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/MyReceiptTracker";
        File dir = new File(file_path);
        File file = new File(dir, seconds + ".png");
		//File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);


		//if (savedInstanceState == null) {
		//getSupportFragmentManager().beginTransaction()
		//	.add(R.id.container, new PlaceholderFragment()).commit();
		//}
	}



	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*if (requestCode == CAPTURE_IMAGE_CAPTURE_CODE) 
		{
			if (resultCode == RESULT_OK) 
			{
				Bundle extras = data.getExtras();
				 
	            // get bitmap
	            pic = (Bitmap) extras.get("data");
	            imageView1.setImageBitmap(pic);
	            
	            Calendar c = Calendar.getInstance(); 
	            int seconds = c.get(Calendar.SECOND);
				 
	            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/MyReceiptTracker";
	            File dir = new File(file_path);
	            File file = new File(dir, seconds + ".png");
	           
	            try {
	            	
					fOut = new FileOutputStream(file);	
					pic.compress(CompressFormat.PNG, 100, fOut);
					fOut.flush();
					fOut.close();
					}
				 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Toast.makeText(this, "Image Captured and Saved", Toast.LENGTH_LONG).show();
			} 
			
			
			else if (resultCode == RESULT_CANCELED) 
			{
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			}
		}*/
		
		if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) 
	    {
	        //Get our saved file into a bitmap object:
			Calendar c = Calendar.getInstance(); 
            int seconds = c.get(Calendar.SECOND);
			 
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/MyReceiptTracker";
            File dir = new File(file_path);
            File file = new File(dir, seconds + ".png");
	       //File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
	       pic = decodeSampledBitmapFromFile(file.getAbsolutePath(), 250, 100);
	    }
		
	}
	
	
	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) 
	{ // BEST QUALITY MATCH
	     
	    //First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, options);
	    	 
	    // Calculate inSampleSize, Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    options.inPreferredConfig = Bitmap.Config.RGB_565;
	    int inSampleSize = 1;
	 
	    if (height > reqHeight) 
	    {
	        inSampleSize = Math.round((float)height / (float)reqHeight);
	    }
	    int expectedWidth = width / inSampleSize;
	 
	    if (expectedWidth > reqWidth) 
	    {
	        //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
	        inSampleSize = Math.round((float)width / (float)reqWidth);
	    }
	 
	    options.inSampleSize = inSampleSize;
	 
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	 
	    return BitmapFactory.decodeFile(path, options);
	}


	public void onClickdeletedatabase(View v){			//when you click "Delete Database" (This would not be available on the actual
		//phone app but since we don't have a server to demonstrate this is to show
		//that we do actually have a delete function)		
		//displayText("Clicked clear all!");			//Display confirmation text		
		myDb.deleteAll();								//delete entire database

	}


	protected void onDestroy() {					//when the activity is closed
		super.onDestroy();
		closeDB();									//close the database.
	}

	private void openDB() {							//create a method that when called opens the sql database through the adapter we have written
		myDb = new DBAdapter(this);
		myDb.open();
	}
	private void closeDB() {				//make method to close the database
		myDb.close();
	}

	public void onClickCSSubmit(View v){					//when you click the submit button

		title = Etitle.getText().toString();
		author = Eauthor.getText().toString();					//get text entered into the UI elements, cast them as Strings and store them in the initialised varaibles
		subject = Esubject.getText().toString();
		module = Emodule.getText().toString();
		comment = Ecomment.getText().toString();

		Toast.makeText(getApplicationContext(), "Receipt Saved", Toast.LENGTH_SHORT).show();
		long newID = myDb.insertRow(title,author,subject,module,comment);			//call the insertRow method and save the returned ID as a long
		//testview.setText("hello"); (just for testing if the Text Views were working

		Cursor cursor = myDb.getRow(newID);			//create a new cursor and get the row just created and pass it to the cursor
		displayRecordSet(cursor);					//display the row the cursor retrieved


	}

	private void displayRecordSet(Cursor cursor) {
		String message = "";
		// populate the message from the cursor

		// Reset cursor to start, checking to see if there's data:
		if (cursor.moveToFirst()) {
			do {
				// Process the data:
				int id = cursor.getInt(DBAdapter.COL_ROWID);
				String name = cursor.getString(DBAdapter.COL_TITLE);
				String author = cursor.getString(DBAdapter.COL_AUTHOR);
				String subject = cursor.getString(DBAdapter.COL_SUBJECT);
				String module = cursor.getString(DBAdapter.COL_MODULE);
				String comment = cursor.getString(DBAdapter.COL_COMMENT);

				// Append data to the message:
				message += "id=" + id
						+", Title=" + name
						+", Author=" + author
						+", Subject=" + subject
						+", Module =" + module
						+", Comment=" + comment
						+"\n";
			} while(cursor.moveToNext());
		}

		// Close the cursor to avoid a resource leak.
		cursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_receipt_tracker, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_main_activity_receipt_tracker, container,
					false);
			return rootView;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}


	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

}
