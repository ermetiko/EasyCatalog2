package com.elfosoftware.easycatalog;

import java.util.ArrayList;

import com.elfosoftware.easycatalog.Adapters.Articolo;
import com.elfosoftware.easycatalog.Adapters.ThumbsAdapter;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class GrigliaMedia extends Fragment{

	private static GridView griglia;
	private static MainActivity main;
	//private static DBHelper databaseHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.grigliamedia, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		main =(MainActivity)getActivity();
		//databaseHelper = new DBHelper(main);
		griglia = (GridView) main.findViewById(R.id.grigliaMedia);

		griglia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override 
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
				// TODO Auto-generated method stub} })
				main.ingrandisci(arg2);
			}
		});
		
		if (main.listaArticoli!=null)
			
			griglia.setAdapter(new Adapters.ThumbsAdapter(main, R.layout.thumb, main.listaArticoli));
	}

	
	public void caricaGriglia(ArrayList<Articolo> articoli) // int idFornitore, int idcategoria, int idSottocategoria)
	{
		/*
		ArrayList<Articolo> articoli = new ArrayList<Adapters.Articolo>();
		SQLiteDatabase db = main.databaseHelper.getReadableDatabase();
		Cursor crs = ArticoliDB.getArticoli(db, idFornitore, idcategoria, idSottocategoria);
		//main.mnuRecordCount.setTitle(Integer.toString(crs.getCount()));	    
		TextView  tv = (TextView)main.findViewById(R.id.mnuContatore);
		tv.setText(Integer.toString(crs.getCount())+" ");		
		int cnt=0;
		try
		{
			while (crs.moveToNext())
			{
				int idArticolo = crs.getInt(0);
				articoli.add(new Adapters.Articolo(idArticolo , crs.getString(1),  (cnt++ <100 ? Adapters.getThumbsImg(idArticolo):null)));
			}
		}
		finally
		{
			crs.close();
			db.close();
		}
		*/
		griglia.setAdapter(new Adapters.ThumbsAdapter(main, R.layout.thumb, articoli));
	}

}
