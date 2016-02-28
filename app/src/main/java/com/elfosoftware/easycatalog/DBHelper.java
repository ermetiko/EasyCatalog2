package com.elfosoftware.easycatalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {
	private Context context;
	private static final String DB_PATH = "/data/data/com.elfosoftware.easycatalog/databases/";
	private static final String NOME_DB = "EasyCatalog.sqlite";
	private static final int VERSIONE_DB = 1;

	/*
    private static final String CREATE_TABLE_FORNITORI =
        "create table fornitori (" + FornitoriDB.ID+ " INTEGER primary key," + FornitoriDB.RAGIONESOCIALE+ " TEXT);";
    */    
 
    public DBHelper(Context context) {
        super(context, NOME_DB, null, VERSIONE_DB);
        this.context = context;
    	System.err.println("Dentro costruttore DBHelper");
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.err.println("Dentro create tables");
        //db.execSQL(CREATE_TABLE_FORNITORI);
        //db.execSQL(CREATE_TABLE_CLIENTI);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.err.println("Dentro update tables");
        //db.execSQL("DROP TABLE IF EXISTS "+ FornitoriDB.TABELLA);
        //db.execSQL("DROP TABLE IF EXISTS "+ Clienti.TABELLA);
        onCreate(db);
    }
    
    private boolean existDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + NOME_DB;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
     		//database does't exist yet.
    	}
     	if(checkDB != null){
    		checkDB.close();
    	}
    	return (checkDB != null); // ? true : false;
    }
    
    private void copyDataBase() throws IOException{
		/*
		String state = Environment.getExternalStorageState();
		if ((state.equals(Environment.MEDIA_MOUNTED))) {
			Toast.makeText(context.getApplicationContext(), "C'è la card", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(context.getApplicationContext(), "There is no any sd card", Toast.LENGTH_LONG).show();
		}
		*/
		/*
		String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
		File fl = new File(root + "/EasyCatalogImg", "prova.txt");
		//File fl = new File(Environment.getExternalFilesDir());
		File fl = new File("/Removable/MicroSD/EasyCatalogImg", "prova.txt");
		//File fl = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyCatalogImg", "prova.txt");
		fl.mkdirs();
		if (fl.exists ()) fl.delete ();
		try {
			FileOutputStream out = new FileOutputStream(fl);
			out.write(10);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/

		String inFileName =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/easycatalog.zip";
    	//String inFileName =  Environment.getExternalStorageDirectory().getPath() + File.separator +"Cartesio" + File.separator + NOME_DB;
		File file = new File (inFileName);
		if (file.exists())
		{
	    	InputStream myInput = new FileInputStream(inFileName); //Open your local db as the input stream
	    	String outFileName = DB_PATH + NOME_DB; // Path to the just created empty db
	    	OutputStream myOutput = new FileOutputStream(outFileName); //Open the empty db as the output stream
	    	byte[] buffer = new byte[1024]; //transfer bytes from the inputfile to the outputfile
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	myOutput.flush(); //Close the streams
	    	myOutput.close();
			myInput.close();
			file.delete();
		}
     }    
    
    public boolean createDataBase() {
    	boolean ok= true;
    	if (true) //!existDataBase())
    	{
    		this.getReadableDatabase();
    		try {
    			copyDataBase();
    		} catch (IOException e) {
    			mostraErrore(e);
    			//throw new Error("Error copying database");
    		}
    	}
    	return ok;
    	/*
    	boolean dbExist = checkDataBase();
    	if(dbExist){
    		//do nothing - database already exist
    	} else {
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
        	try {
    			copyDataBase();
    		} catch (IOException e) {
    			mostraErrore(e);
        		//throw new Error("Error copying database");
        	}
    	}
    	*/
    }
    	
    private void mostraErrore(Exception ex)
    {
    	AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    	alertDialog.setTitle("Errore");
    	alertDialog.setMessage(ex.getMessage());
    	alertDialog.show();    	
    }
    /*
	public void openDataBase() throws SQLException{
		 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
     
        @Override
    	public synchronized void close() {
     
        	    if(myDataBase != null)
        		    myDataBase.close();
     
        	    super.close();
     
    	}
         	
    } 
    */   
    
}