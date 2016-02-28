package com.elfosoftware.easycatalog;

import java.io.File;
import java.util.ArrayList;

import android.R.dimen;
import android.R.string;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class Adapters {

	//public static int dimensioneGrigliaGrande=0;
	
	public static class Categoria {
		int Id;
		String Nome;
		Integer Articoli;

		public Categoria(int id, String nome, Integer numero) {
			Id = id;
			Nome = nome;
			Articoli = numero;
		}

		@Override
		public String toString() {
			return this.Nome;
		}
	}
	
	
	public static class CategorieAdapter extends ArrayAdapter<Categoria> {
		private ArrayList<Categoria> items;
		private CategoriaHolder categoriaHolder;
		private Context context;

		private class CategoriaHolder {
			TextView nome;
			TextView numero;
		}

		public CategorieAdapter(Context context, int tvResId, ArrayList<Categoria> items) {
			super(context, tvResId, items);
			this.context = context;
			this.items = items;
		}
		
		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.categorie, null);
				categoriaHolder = new CategoriaHolder();
				categoriaHolder.nome = (TextView) v.findViewById(R.id.nomeCategoria);
				categoriaHolder.numero = (TextView) v.findViewById(R.id.articoliCategoria);
				v.setTag(categoriaHolder);
			} else
				categoriaHolder = (CategoriaHolder) v.getTag();

			Categoria cate = items.get(pos);

			if (cate != null) {
				categoriaHolder.nome.setText(cate.Nome);
				categoriaHolder.numero.setText(Integer.toString(cate.Articoli)
						+ " articoli");
			}

			return v;
		}
	}

	public static class Articolo {
		int Id;
		String Descrizione;
		String Categoria;
		String Famiglia;
		String Fornitore;
		Drawable Thumb;
		Drawable Immagine;
		

		public Articolo(int id, String descrizione, Drawable thumb, Drawable immagine ) {
			Id = id;
			Descrizione = descrizione;
			Thumb = thumb;
			Immagine = immagine;
		}

		@Override
		public String toString() {
			return this.Descrizione;
		}
	}

	public static class ThumbsAdapter extends ArrayAdapter<Articolo> {
		private ArrayList<Articolo> items;
		private ItemHolder itemHolder;
		private Context context;

		private class ItemHolder {
			ImageView img;
			TextView nome;
		}

		public ThumbsAdapter(Context context, int tvResId, ArrayList<Articolo> items) {
			super(context, tvResId, items);
			this.context = context;
			this.items = items;
		}
		
		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View v = convertView;
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.thumb, null);
				itemHolder = new ItemHolder();
				itemHolder.img = (ImageView) v.findViewById(R.id.thumbImmagine);
				itemHolder.nome = (TextView) v.findViewById(R.id.thumbDescrizione);
				v.setTag(itemHolder);
			} else
				itemHolder = (ItemHolder) convertView.getTag();

			Articolo arti = items.get(pos);
			if (arti != null) {
				
				if (arti.Thumb==null)
					arti.Thumb= getImmagine(arti.Id, true);
				itemHolder.img.setImageDrawable(arti.Thumb);
				itemHolder.nome.setText(arti.Descrizione);
			}
			return v;
		}
	}
	
	
	public static class ImmagineLargeAdapter extends ArrayAdapter<Articolo> {
		private ArrayList<Articolo> items;
		private ItemHolder itemHolder;
		private Context context;

		private class ItemHolder {
			ImageView img;
			TextView codice;
			TextView nome;
			TextView categoria;
			TextView sottocategoria;
		}

		public ImmagineLargeAdapter(Context context, int tvResId, ArrayList<Articolo> items) {
			super(context, tvResId, items);
			this.context = context;
			this.items = items;
		}
		
		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View v = convertView;
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.immagine_large, null);
				itemHolder = new ItemHolder();
				itemHolder.img = (ImageView) v.findViewById(R.id.immagineLarge);
				
				//itemHolder.img.setLayoutParams(new LayoutParams(dimensioneGrigliaGrande, dimensioneGrigliaGrande));
				
				itemHolder.codice = (TextView) v.findViewById(R.id.imgCodiceArticolo);
				itemHolder.nome = (TextView) v.findViewById(R.id.imgDescrizioneArticolo);
				itemHolder.categoria = (TextView) v.findViewById(R.id.imgCategoria);
				itemHolder.sottocategoria = (TextView) v.findViewById(R.id.imgSottocategoria);
				v.setTag(itemHolder);
			} else
				itemHolder = (ItemHolder) convertView.getTag();

			Articolo arti = items.get(pos);
			if (arti != null) {
				
				if (arti.Immagine==null)
					arti.Immagine= getImmagine(arti.Id, false);
				itemHolder.img.setImageDrawable(arti.Immagine);
				itemHolder.codice.setText(Integer.toString(arti.Id));
				itemHolder.nome.setText(arti.Descrizione);
				itemHolder.categoria.setText("categoria");
				itemHolder.sottocategoria.setText("Sottocategoria");
			}
			return v;
		}
	}
	
	public static Drawable getImmagine(int idArticolo, boolean ridotta)
	{
		Drawable img=null;
		/*
		int numero = (int)Math.ceil(Math.random()*79);
		String nomeFile =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyCatalogImg/" + Integer.toString(numero+1) + (ridotta?  "_th.jpg":".jpg");
		*/
		String nomeFile =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyCatalogImg/" + Integer.toString(idArticolo) + (ridotta?  "_th.jpg":".jpg");

		File file = new File (nomeFile);
		if (file.exists())
		{
			img= Drawable.createFromPath(file.getAbsolutePath());
		}
		return img;
	}
	
	/*
	public static Drawable getImmagineLarge(int idArticolo)
	{
		Drawable img=null;
		int numero = (int)Math.ceil(Math.random()*72);
		String estenzione = (numero<32 ? ".jpg" : ".png");
		String nomeFile =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/immagini2/" + Integer.toString(numero+1) + estenzione;

		File file = new File (nomeFile);
		if (file.exists())
		{
			img= Drawable.createFromPath(file.getAbsolutePath());
		}
		return img;
	}
	*/

	
	/*
	public static class ImageAdapter extends BaseAdapter {
		private Context context;
		private Drawable[] images;

		public ImageAdapter(Context context, Drawable[] images) {
			this.context = context;
			this.images = images;
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return images[position];
		}

		@Override
		public long getItemId(int position) {
			return images[position].hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// ImageView imageView;
			View v;

			if (convertView == null) {
//				 * imageView = new ImageView(context);
//				 * imageView.setLayoutParams(new GridView.LayoutParams(190,
//				 * 190)); imageView.setScaleType(ImageView.ScaleType.CENTER);
//				 * imageView.setPadding(5, 5, 5, 5);

				LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.thumb, null);
				// v.setLayoutParams(new GridView.LayoutParams(190, 190));
				// v.setPadding(5, 5, 5, 5);

				TextView tv = (TextView) v.findViewById(R.id.thumbDescrizione);
				tv.setText("descrizione " + position);

				ImageView iv = (ImageView) v.findViewById(R.id.thumbImmagine);
				iv.setImageDrawable(images[position]); // setImageResource(R.drawable.icon);
			} else {
				v = convertView;
			}

//			 * if (convertView == null) { imageView = new ImageView(context);
//			 * imageView.setLayoutParams(new GridView.LayoutParams(190, 190));
//			 * imageView.setScaleType(ImageView.ScaleType.CENTER);
//			 * imageView.setPadding(5, 5, 5, 5);
//			 * 
//			 * imageView.setImageDrawable(images[position]); } else { imageView
//			 * = (ImageView) convertView; }

			return v; // imageView;
		}
	}
	*/

	public static class BigSpinnerAdapter extends ArrayAdapter<Categoria> implements SpinnerAdapter	
	{
		private ArrayList<Categoria> items;
		private CategoriaHolder categoriaHolder;
		private Context context;

		private class CategoriaHolder {
			TextView nome;
			TextView numero;
		}

		public BigSpinnerAdapter(Context context, int tvResId,
				ArrayList<Categoria> items) {
			super(context, tvResId, items);
			this.context = context;
			this.items = items;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.categorie, null);
				categoriaHolder = new CategoriaHolder();
				categoriaHolder.nome = (TextView) v.findViewById(R.id.nomeCategoria);
				categoriaHolder.numero = (TextView) v.findViewById(R.id.articoliCategoria);
				v.setTag(categoriaHolder);
			} else
				categoriaHolder = (CategoriaHolder) v.getTag();

			Categoria cate = items.get(pos);

			if (cate != null) {
				categoriaHolder.nome.setText(cate.Nome);
				categoriaHolder.numero.setText(Integer.toString(cate.Articoli) + " articoli");
			}
			return v;
		}
		
		@Override
	     public View getDropDownView(int position, View convertView, ViewGroup parent){
			return getView(position, convertView, parent);
	     }

	}

	public static class Marca {
		Integer Id;
		String Nome;
		Integer Articoli;

		public Marca(Integer id, String nome, Integer numero) {
			Id = id;
			Nome = nome;
			Articoli = numero;
		}

		@Override
		public String toString() {
			return this.Nome;
		}

	}

}
