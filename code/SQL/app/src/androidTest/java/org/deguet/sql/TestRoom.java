package org.deguet.sql;

import android.arch.persistence.room.Room;
import android.content.Context;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import org.deguet.sql.model.ProduitAnnot;
import org.deguet.sql.room.MaBD;

public class TestRoom extends TestAbstract {

    @Override
    public SCRUD<ProduitAnnot> getRepo() {
        Context ctx = getApplicationContext();
        //MaBD mDb = Room.inMemoryDatabaseBuilder(ctx, MaBD.class).build();
        MaBD mDb = Room.databaseBuilder(ctx, MaBD.class, "Test-BD").build();
        return mDb.dao();
    }
}
