package org.deguet.sql;

import android.content.Context;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import org.deguet.sql.model.ProduitAnnot;
import org.junit.Assert;

/**
 * Created by joris on 15-04-15.
 */
public class TestRepoSQLite extends TestAbstract {


    @Override
    public SCRUD<ProduitAnnot> getRepo() {
        Context ctx = getApplicationContext();
        return RepoSQLite.get(ctx, "DataBaseTest", 2);
    }
}
