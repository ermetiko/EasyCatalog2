package com.elfosoftware.easycatalog;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ArticoliDB {
    public static final String ID = "id";
    public static final String DESCRIZIONE = "Descrizione";
    public static final String IDCATEGORIA = "IdCategoria";
    public static final String IDSOTTOCATEGORIA = "IdSottocategoria";
    public static final String PROMOZIONE = "Promozione";
    public static final String SCADENZAPROMOZIONE = "ScadenzaPromozione";
    public static final String IDFORNITORE = "IdFornitore";
 
    public static final String TABELLA = "articoli";
    public static final String[] COLONNE = new String[]{ID, DESCRIZIONE, IDCATEGORIA, IDSOTTOCATEGORIA, PROMOZIONE, SCADENZAPROMOZIONE, IDFORNITORE};
 
    public static void insertArticolo(SQLiteDatabase db, Integer id, String descrizione, int idCategoria, int idSottocategoria, boolean promozione, Date scadenzaPromozione, int idFornitore){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    	ContentValues v = new ContentValues();
        v.put(ID, id);
        v.put(DESCRIZIONE, descrizione);
        v.put(IDCATEGORIA, idCategoria);
        v.put(IDSOTTOCATEGORIA, idSottocategoria);
        v.put(PROMOZIONE, promozione);
        v.put(SCADENZAPROMOZIONE, dateFormat.format(scadenzaPromozione));
        v.put(IDFORNITORE, idFornitore);
        db.insert(TABELLA, null, v);
    }
    
    public static Cursor getAllArticoli(SQLiteDatabase db){
        return db.query(TABELLA, COLONNE, null, null, null, null, DESCRIZIONE);
    }

    public static Cursor getArticoli(SQLiteDatabase db, int idFornitore, int idCategoria, int idSottocategoria){
    	String query="";
    	if (idFornitore != 0)
    		query += ((query.isEmpty() ? "": " AND ") + IDFORNITORE +"=" + Integer.toString(idFornitore));
    	if (idSottocategoria != 0)
    		query += ((query.isEmpty() ? "": " AND ") + IDSOTTOCATEGORIA +"=" + Integer.toString(idSottocategoria));
    	else
    	{
        	if (idCategoria != 0)
        		query += ((query.isEmpty() ? "": " AND ") + IDCATEGORIA +"=" + Integer.toString(idCategoria));
    	}
    	if (query.isEmpty())
    		query=null;
    	
        return db.query(TABELLA, new String[]{ID, DESCRIZIONE}, query, null, null, null, DESCRIZIONE);
        //return db.query(TABELLA, COLONNE, query, null, null, null, DESCRIZIONE);
    }

    public static boolean deleteArticolo(SQLiteDatabase db, long id) {
        return db.delete(TABELLA, ID + "=" + id, null) > 0;
    }

    public static boolean deleteAll(SQLiteDatabase db) {
        return db.delete(TABELLA, null, null) > 0;
    }

    public static Cursor getArticolo(SQLiteDatabase db, int id) throws SQLException {
        Cursor c = db.query(true, TABELLA, COLONNE, ID + "=" + id, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    
}
