package com.elfosoftware.easycatalog;

import java.util.ArrayList;

import com.elfosoftware.easycatalog.Adapters.Articolo;

import android.R.string;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;

public class GrigliaGrande extends Fragment{
	private static GridView griglia;
	private static MainActivity main;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.griglia_grande, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		main =(MainActivity)getActivity();
		griglia = (GridView) main.findViewById(R.id.grigliaGrande);

		griglia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override 
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
				// TODO Auto-generated method stub} })
				main.ingrandisci();
			}
		});
		
		/*
		griglia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		};
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		})
		*/
		//databaseHelper = new DBHelper(main);
		if (main.listaArticoli!=null)
		{
			//Adapter adp = new Adapters.ImmagineLargeAdapter(main, R.layout.immagine_large, main.listaArticoli);
			griglia.setAdapter(new Adapters.ImmagineLargeAdapter(main, R.layout.immagine_large, main.listaArticoli, main.immaginiPath));
	        int posizione = (getArguments() == null ? -1 : getArguments().getInt("POSIZIONE", -1));
			if (posizione!=1)
			{
				griglia.setSelected(true);
				griglia.setSelection(posizione);
			}
		}			
	}
	
	

	public void caricaGriglia(ArrayList<Articolo> articoli)
	{
		griglia.setAdapter(new Adapters.ImmagineLargeAdapter(main, R.layout.immagine_large, articoli, main.immaginiPath));
	}

}
