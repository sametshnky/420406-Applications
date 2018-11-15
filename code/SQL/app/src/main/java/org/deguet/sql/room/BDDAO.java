package org.deguet.sql.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.deguet.sql.SCRUD;
import org.deguet.sql.model.ProduitAnnot;

import java.util.List;

/**
 * you can switch between interface if only generated methods
 *
 * or abstract class if a transaction method is necessary
 *
 *
 */

@Dao
public abstract class BDDAO implements SCRUD<ProduitAnnot> {

    @Query("SELECT * FROM produitannot")
    public abstract List<ProduitAnnot> getAll();

    @Query("SELECT * FROM produitannot WHERE id IN (:userIds)")
    public abstract List<ProduitAnnot> loadAllByIds(Long[] userIds);

    @Query("SELECT * FROM produitannot WHERE name LIKE :nom LIMIT 1")
    public abstract ProduitAnnot findByName(String nom);

    @Insert
    public abstract void insertAll(ProduitAnnot... users);

    @Delete
    public abstract void delete(ProduitAnnot user);

    @Query("DELETE FROM produitannot")
    public abstract void deleteAll();

    @Insert
    public abstract long save(ProduitAnnot o);

    @Insert
    public abstract List<Long> saveMany(List<ProduitAnnot> o);

    @Query("SELECT * FROM produitannot WHERE id = :p")
    public abstract ProduitAnnot getById(Long p);

    @Query("DELETE FROM produitannot WHERE id = :o")
    public abstract void deleteOne(Long o);

}
