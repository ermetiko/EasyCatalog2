package com.elfosoftware.easycatalog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategorieArticoliDB {
	public static final String ID = "id";
	public static final String DESCRIZIONE = "Descrizione";
	public static final String IDPADRE = "idPadre";

	public static final String TABELLA = "CategorieArticoli";
	public static final String[] COLONNE = new String[] { ID, DESCRIZIONE, IDPADRE };

	public static void insertCategoria(SQLiteDatabase db, Integer id, String descrizione, int idPadre) {
		ContentValues v = new ContentValues();
		v.put(ID, id);
		v.put(DESCRIZIONE, descrizione);
		v.put(IDPADRE, idPadre);
		db.insert(TABELLA, null, v);
	}

	public static Cursor getAllCategorieByPadre(SQLiteDatabase db, int idPadre) {
		return db.query(false, TABELLA, COLONNE,
				IDPADRE + "=" + Integer.toString(idPadre), null, null, null,
				DESCRIZIONE, null);
	}

	public static boolean deleteAll(SQLiteDatabase db) {
		return db.delete(TABELLA, null, null) > 0;
	}

	public static Cursor getCategorieJoin(SQLiteDatabase db, int idFornitore) {
		String query="select a.idcategoria, c.descrizione, count(*) from articoli a inner join categoriearticoli c on a.idcategoria=c.id";
		if (idFornitore!=0)
			query += " where a.idfornitore=" + Integer.toString(idFornitore);
		query += " group by a.idcategoria order by c.descrizione;";
		return db.rawQuery(query, null);
	}

	public static Cursor getSottocategorieJoin(SQLiteDatabase db, int idFornitore, int idCategoria) {
		String query="select a.idsottocategoria, c.descrizione, count(*)  from articoli a inner join categoriearticoli c on a.idsottocategoria=c.id where a.idcategoria=" + Integer.toString(idCategoria);
		if (idFornitore!=0)
			query += " and a.idfornitore=" + Integer.toString(idFornitore);
		query += " group by a.idsottocategoria order by c.descrizione;";
		return db.rawQuery(query, null);
	}

}
