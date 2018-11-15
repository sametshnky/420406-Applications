package org.deguet.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.deguet.sql.model.ProduitAnnot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Uses a SQL database to hold the products
 */
public class RepoSQLite extends SQLiteOpenHelper implements SCRUD<ProduitAnnot> {
	class CursorAdapterCashProduct implements Iterable<ProduitAnnot> {
		private Cursor[] c; private int currentC = 0;
		CursorAdapterCashProduct(Cursor... c){this.c = c;}
		@Override
        public Iterator<ProduitAnnot> iterator() {
			currentC = 0 ;return new Iterator<ProduitAnnot>(){
				@Override
                public boolean hasNext() {boolean next = c[currentC].moveToNext(); if (next) return next; currentC++; if (currentC == c.length) return false; return c[currentC].moveToNext();}
				@Override
                public ProduitAnnot next() { return convertProduct(c[currentC]);}
				@Override
                public void remove() {throw new IllegalArgumentException("Cannot remove here");}
			};
		}
	}

	private SQLiteDatabase db;

	private Context context;
	
	private String nom = "Default";

	private static RepoSQLite one;	// Singleton instance

	public static synchronized RepoSQLite get(Context context, String nom, int version) {
		if (one == null) one = new RepoSQLite(context, nom, version);
		return one;
	}

	public static synchronized void release() {
		one = null;
	}

	private RepoSQLite(Context context, String nom, int version) {
		super(context, nom, null, version);
		this.db = this.getWritableDatabase();
		this.nom = nom;
		this.context = context;
		Log.i(nom, "Creation of database");
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
		Log.i(nom, "On create will create tables");
		createTables(db);
		Log.i(nom, "On create has created tables");
	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(nom, "On upgrade will delete database from " + oldVersion + " > " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + T_Product);
		Log.i(nom, "On upgrade has deleted database");
		onCreate(db);
	}
	
	@Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(nom, "On downgrade will call upgrade");
		onUpgrade( db, oldVersion, newVersion);
		Log.i(nom, "On downgrade finished");
	}
	
	public List<String> allTableNames() {
		List<String> result = new ArrayList<String>() ;
		String selectQuery = "select name from sqlite_master where type = 'table'" ;
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
			String n =  c.getString(c.getColumnIndex("name"));
			result.add(n);
			} while (c.moveToNext());
		}
		return result;
	}
	
	public void deleteDBFiles() {
		Log.i(nom, "Suppression des fichiers " + context);
		context.deleteDatabase(nom);
	}
	
	public void createTables(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_Product);
	}

	public final static String T_Product = "Product";		// nom de la table
	public final static String F_id = "F_id";				// nom de chacun des champs (F pour field)
	public final static String F_nom = "F1_1";
	public final static String F_description = "F1_2";
	public final static String F_tax = "F1_3";
	public final static String F_codeBarre = "F1_4";
	public final static String F_prixUnitaire = "F1_5";

	private static final String CREATE_TABLE_Product = "CREATE TABLE "+T_Product+" (" +
			F_id 					+ " INTEGER PRIMARY KEY AUTOINCREMENT ," +  
			F_nom 					+ " TEXT ," +  
			F_description 			+ " TEXT ," +  
			F_tax 					+ " INTEGER ," +  
			F_codeBarre 			+ " TEXT ," +  
			F_prixUnitaire 			+ " INTEGER ," +  
			"GEN_LASTMOD  INTEGER " + // Used for debugging purpose 
			" )" ;

    @Override
    public long save(ProduitAnnot o) {
		ContentValues values = new ContentValues();
		values.put( F_nom , o.nom );
		values.put( F_description , o.nom );
		{ProduitAnnot.TaxType eee  = o.tax;
		if (eee != null) values.put( F_tax , eee.ordinal() );}
		values.put( F_prixUnitaire , o.prixUnitaire );
		values.put( "GEN_LASTMOD" , (new Date()).getTime() );
		Long id = o.id;
		if (id == null){
			id = db.insert(T_Product, null, values);
			o.id = id;
		}else{ 
			db.update(T_Product, values, F_id+" = ?", new String[]{String.valueOf(id)});
		}
		return o.id;
	}

	@Override
	public List<Long> saveMany(List<ProduitAnnot> list) {
		List<Long> ids = new ArrayList<>();
		for (ProduitAnnot p : list){
			ids.add(this.save(p));
		}
		return ids;
	}

	public ProduitAnnot randomCashProduct(){
		ProduitAnnot o = new ProduitAnnot();
		Random r = new Random();
		o.nom = "Bli bla "+r.nextInt(100);
		o.tax = (ProduitAnnot.TaxType.values()[r.nextInt(ProduitAnnot.TaxType.values().length)] );
		o.prixUnitaire = (r.nextInt() );
		return o;
	}
	
	@Override
    public ProduitAnnot getById(java.lang.Long p) {
		Cursor c = db.query(T_Product, null, F_id + " = ? "  , new String[]{""+p}, null, null, null);
		if (c != null && c.moveToFirst()){
			ProduitAnnot res = convertProduct(c);
			return res;
		}
		return null;
	}

	private Cursor getAllCashProductAsCursor() {
		String selectQuery = "SELECT  * FROM " + T_Product;
		Cursor c = db.rawQuery(selectQuery, null);
		return c;
	}

	public List<ProduitAnnot> convertCursor(Cursor c){
		List<ProduitAnnot> res = new ArrayList<>();
		if (c.moveToFirst()) {
		do {
			ProduitAnnot p = convertProduct(c);
			res.add(p);
		} while (c.moveToNext());
		}
		return res;
	}
	
	@Override
    public List<ProduitAnnot> getAll() {
		Cursor c = getAllCashProductAsCursor();
		return convertCursor(c);
	}

	@Override
    public void deleteOne(Long o) {
		db.delete(T_Product, F_id + " = ? ", new String[] { String.valueOf(o)});
	}

	@Override
    public void deleteAll() {
		db.delete(T_Product,null,null);
	}

	private ProduitAnnot convertProduct(Cursor c){
		ProduitAnnot o = new ProduitAnnot();

		{Long lll = c.getLong(c.getColumnIndex(F_id) );
		o.id = (lll );}
		o.nom = (c.getString(c.getColumnIndex(F_nom) ) );
		o.tax = (ProduitAnnot.TaxType.values()[c.getInt(c.getColumnIndex(F_tax) ) ] );
		o.prixUnitaire = (c.getInt(c.getColumnIndex(F_prixUnitaire) ) );
		return o;
	}

}