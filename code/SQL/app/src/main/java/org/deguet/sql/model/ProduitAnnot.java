package org.deguet.sql.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name"},// declares an index on name
        unique = true)})                    // adds a unique constraint on name
public class ProduitAnnot{


    @PrimaryKey(autoGenerate = true)
    public Long id;

    public enum TaxType { BaseProduct, TaxedProduct};

    //@ColumnInfo(name = "tax")
    //@TypeConverters(EnumTypeConverter.class)
    public TaxType tax;

    @ColumnInfo(name = "name")
    public String nom;

    @ColumnInfo(name = "nameNoIndex")
    public String nomSansIndex;

    @ColumnInfo(name = "unit_price")
    public Integer prixUnitaire;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProduitAnnot{");
        sb.append("uid=").append(id);
        sb.append(", nom='").append(nom).append('\'');
        sb.append(", prixUnitaire=").append(prixUnitaire);
        sb.append('}');
        return sb.toString();
    }
}
