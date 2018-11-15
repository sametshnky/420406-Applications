package org.deguet.sql.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import org.deguet.sql.model.ProduitAnnot;

/**
 * TODO explain how to convert enum types for example, DateTime, BigDecimal
 * TODO explain how to schema location to understand what sql DEBUG  room.schemaLocation buidl.gradle
 * TODO explain how to get the file from device and open it to see
 * TODO allowMainThreadQueries() or solution for async as BD op can be long
 * TODO show how it reacts to null values, bad data etc. very long strings, kanji
 */

@Database(entities = {ProduitAnnot.class}, version = 1)
@TypeConverters({EnumTypeConverter.class})
public abstract class MaBD extends RoomDatabase {

    public abstract BDDAO dao();




}
