package com.elfosoftware.easycatalog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FornitoriDB {
    public static final String ID = "id";
    public static final String RAGIONESOCIALE = "RagioneSociale";
    public static final String TABELLA = "fornitori";
    public static final String[] COLONNE = new String[]{ID, RAGIONESOCIALE};
 
    public static void insertFornitore(SQLiteDatabase db, Integer id, String ragioneSociale){
        ContentValues v = new ContentValues();
        v.put(ID, id);
        v.put(RAGIONESOCIALE, ragioneSociale);
        db.insert(TABELLA, null, v);
    }
    
    public static Cursor getAllFornitori(SQLiteDatabase db){
        return db.query(TABELLA, COLONNE, null, null, null, null, RAGIONESOCIALE);
    }

    public static boolean deleteFornitore(SQLiteDatabase db, long id) {
        return db.delete(TABELLA, ID + "=" + id, null) > 0;
    }

    public static boolean deleteAll(SQLiteDatabase db) {
        return db.delete(TABELLA, null, null) > 0;
    }
    
    public static Cursor getFornitore(SQLiteDatabase db, int id) throws SQLException {
        Cursor c = db.query(true, TABELLA, COLONNE, ID + "=" + id, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    
    public static boolean updateFornitore(SQLiteDatabase db, int id, String ragioneSociale){
        ContentValues v = new ContentValues();
        v.put(ID, id);
        v.put(RAGIONESOCIALE, ragioneSociale);
        return db.update(TABELLA, v, ID + "=" + id, null) >0; 
    }
 
    public static Cursor getFornitoriJoin(SQLiteDatabase db) {
		String query="select a.idFornitore, f.ragionesociale, count(*) from articoli a inner join fornitori f on a.idfornitore=f.id group by a.idfornitore order by f.ragionesociale;";
		return db.rawQuery(query, null);
	}    
    
}
