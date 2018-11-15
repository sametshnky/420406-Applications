package org.deguet.sql.room;

import android.arch.persistence.room.TypeConverter;

import org.deguet.sql.model.ProduitAnnot;

public class EnumTypeConverter {


    @TypeConverter
    public static ProduitAnnot.TaxType toTaxType(String value) {
        return ProduitAnnot.TaxType.valueOf(value);
    }

    @TypeConverter
    public static String toString(ProduitAnnot.TaxType value) {
        return value.toString();
    }
}