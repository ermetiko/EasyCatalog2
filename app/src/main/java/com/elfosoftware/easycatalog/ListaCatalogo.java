package com.elfosoftware.easycatalog;

import java.util.ArrayList;

import com.elfosoftware.easycatalog.Adapters.Articolo;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ListaCatalogo extends Fragment {

	private static ListView lista;
	private static Spinner spnMarche;
	private static Spinner spnCategorie;
	//private static Activity main;
	private static MainActivity main;
	//private static DBHelper databaseHelper;
	//private static OnSottocategoriaSelectedListener mCallback;    
	//private MenuItem mnuRecordCount = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listacatalogo, container, false);
	}	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			//main= (MainActivity)activity;
			//mCallback = (OnSottocategoriaSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " deve implementare OnSottocategoriaSelectedListener");
		}
	}		

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		main =(MainActivity)getActivity();
		//databaseHelper = new DBHelper(main);
		lista = (ListView) main.findViewById(R.id.lista);
		spnMarche = (Spinner) main.findViewById(R.id.spnMarche);
		spnCategorie = (Spinner) main.findViewById(R.id.spnFamiglia);

		spnMarche.setOnItemSelectedListener(new spnFornitoriListener());
		spnCategorie.setOnItemSelectedListener(new spnCategorieListener());
		lista.setOnItemClickListener(new listaListener());

		caricaFornitori();
	}

	public interface OnSottocategoriaSelectedListener {
		public void onSottocategoriaSelected(ArrayList<Articolo> articoli); //int idFornitore, int idCategoria, int idSottocategoria);
	}

	private class listaListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> a, View v, int i, long l) {
			try {

				//Spinner spn = (Spinner)findViewById(R.id.spnMarche);
				int idFornitore = ((Adapters.Categoria)spnMarche.getSelectedItem()).Id;

				//spn = (Spinner)findViewById(R.id.spnFamiglia);
				int idCategoria = ((Adapters.Categoria)spnCategorie.getSelectedItem()).Id;

				int idSottocategoria = ((Adapters.Categoria)a.getItemAtPosition(i)).Id;
				
				main.onSottocategoriaSelected(leggiArticoli(idFornitore, idCategoria, idSottocategoria));
				//mCallback.onSottocategoriaSelected(idFornitore, idCategoria, idSottocategoria);
				//caricaGriglia(idFornitore, idCategoria, idSottocategoria);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}        	
	}

	private ArrayList<Articolo> leggiArticoli(int idFornitore, int idcategoria, int idSottocategoria)
	{
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
				articoli.add(new Adapters.Articolo(idArticolo , crs.getString(1),  (cnt++ <100 ? Adapters.getImmagine(idArticolo, true):null), null));
			}
		}
		finally
		{
			crs.close();
			db.close();
		}
		return articoli;
		//griglia.setAdapter(new Adapters.ThumbsAdapter(main, R.layout.thumb, articoli));
	}	
	
	private class spnCategorieListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			int idFornitore = ((Adapters.Categoria)spnMarche.getSelectedItem()).Id;			
			Adapters.Categoria cate =  (Adapters.Categoria)parentView.getItemAtPosition(position);
			caricaSottocategorie(idFornitore, cate.Id);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parentView) {
			// your code here
		}						
	}

	private class spnFornitoriListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			caricaCategorie(spnCategorie);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parentView) {
			// your code here
		}						
	}	

	public void caricaFornitori()
	{
		ArrayList<Adapters.Categoria> listaFornitori = new ArrayList<Adapters.Categoria>();
		//listaFornitori.add(new Adapters.Categoria(0, " Tutti i fornitori", (int) (Math.random()*100)));
		SQLiteDatabase db = main.databaseHelper.getReadableDatabase();

		Cursor crs = FornitoriDB.getFornitoriJoin(db);
		//Cursor crs = FornitoriDB.getAllFornitori(db);
		int totale=0;
		try
		{
			while (crs.moveToNext())
			{
				int arti = crs.getInt(2);
				totale += arti;
				listaFornitori.add(new Adapters.Categoria(crs.getInt(0), crs.getString(1), arti));
			}
			//listaFornitori.add(new Adapters.Categoria(crs.getInt(0), crs.getString(1), (int) (Math.random()*100)));
		}
		finally
		{
			crs.close();
			db.close();
		}
		listaFornitori.add(0, new Adapters.Categoria(0, " Tutti i fornitori", totale));
		Adapters.BigSpinnerAdapter adpMarche = new Adapters.BigSpinnerAdapter(main, R.layout.categorie, listaFornitori);
		spnMarche.setAdapter(adpMarche);
	}


	private void caricaCategorie(Spinner spn)
	{
		ArrayList<Adapters.Categoria> listaCategorie = new ArrayList<Adapters.Categoria>();
		SQLiteDatabase db = main.databaseHelper.getReadableDatabase();
		int idFornitore = ((Adapters.Categoria)spnMarche.getSelectedItem()).Id;
		Cursor crs = CategorieArticoliDB.getCategorieJoin(db, idFornitore);
		//Cursor crs = CategorieArticoliDB.getAllCategorieByPadre(db, 1);
		//int numCategorie = crs.getCount();
		//int totale=0;
		try
		{
			while (crs.moveToNext())
			{
				int arti = crs.getInt(2);
				//totale += arti;
				listaCategorie.add(new Adapters.Categoria(crs.getInt(0), crs.getString(1), arti));
			}
			//listaCategorie.add(new Adapters.Categoria(crs.getInt(0), crs.getString(1), 1));
		}
		finally
		{
			crs.close();
			db.close();
		}
		//if ((idFornitore!=0) && (numCategorie>1))
		//	listaCategorie.add(0, new Adapters.Categoria(0, " Tutte le categorie", totale));
		Adapters.BigSpinnerAdapter adpCategorie = new Adapters.BigSpinnerAdapter(main, R.layout.categorie, listaCategorie);
		spn.setAdapter(adpCategorie);
	}


	private void caricaSottocategorie(int idFornitore, int idCategoria)
	{
		ArrayList<Adapters.Categoria> listaCategorie = new ArrayList<Adapters.Categoria>();
		//listaCategorie.add(  new Adapters.Categoria(0, " Tutte le sottocategorie", (int) (Math.random()*100)));
		SQLiteDatabase db = main.databaseHelper.getReadableDatabase();
		Cursor crs = CategorieArticoliDB.getSottocategorieJoin(db, idFornitore, idCategoria);
		int numSottocategorie = crs.getCount();
		//Cursor crs = CategorieArticoliDB.getAllCategorieByPadre(db, idCategoria);

		int totale=0;
		try
		{
			while (crs.moveToNext())
			{
				int arti=crs.getInt(2);
				totale += arti;
				listaCategorie.add(  new Adapters.Categoria(crs.getInt(0), crs.getString(1), arti));
				//listaCategorie.add(  new Adapters.Categoria(crs.getInt(0), crs.getString(1), (int) (Math.random()*100)));
			}
		}
		finally
		{
			crs.close();
			db.close();
		}
		if (((idFornitore!=0) && (numSottocategorie>1)) || (numSottocategorie==0))
			listaCategorie.add(0, new Adapters.Categoria(0, " Tutte le sottocategorie", totale));
		Adapters.CategorieAdapter catAdapter = new Adapters.CategorieAdapter(main, R.layout.categorie, listaCategorie);
		lista.setAdapter(catAdapter); 
	}

}
