package org.deguet.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.deguet.sql.room.MaBD;
import org.deguet.sql.model.ProduitAnnot;


public class DemoActivity extends Activity {

    SCRUD<ProduitAnnot> repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final SCRUD<ProduitAnnot> sql = RepoSQLite.get(getApplicationContext(), "DataBaseMain", 4);

        final MaBD db = Room.databaseBuilder(getApplicationContext(),
                MaBD.class, "database-name")
                .allowMainThreadQueries()       // dangerous as it could freeze UI
                .build();

        ListView lvsql = findViewById(R.id.lvsql);
        final ArrayAdapter<ProduitAnnot> adapter = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1, new ArrayList<ProduitAnnot>());
        adapter.addAll(sql.getAll());
        lvsql.setAdapter(adapter);

        findViewById(R.id.ajout_sql).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sql.save(makeOneP());
                adapter.clear();
                adapter.addAll(sql.getAll());
                adapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.vider_sql).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sql.deleteAll();
                adapter.clear();
                adapter.addAll(sql.getAll());
                adapter.notifyDataSetChanged();
            }
        });



        ListView lvroom = findViewById(R.id.lvroom);
        final ArrayAdapter<ProduitAnnot> adapterRoom = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1, new ArrayList<ProduitAnnot>());
        adapterRoom.addAll(db.dao().getAll());
        lvroom.setAdapter(adapterRoom);

        findViewById(R.id.ajout_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.dao().insertAll(makeOneP());
                adapterRoom.clear();
                adapterRoom.addAll(db.dao().getAll());
                adapterRoom.notifyDataSetChanged();
            }
        });

        findViewById(R.id.vider_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.dao().deleteAll();
                adapterRoom.clear();
                adapterRoom.addAll(db.dao().getAll());
                adapterRoom.notifyDataSetChanged();
            }
        });




        Toast.makeText(getApplicationContext(), "Pensez à exécuter les tests unitaires du projet", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo_sqlite, menu);
        return true;
    }

    public ProduitAnnot makeOneP(){
        ProduitAnnot p = new ProduitAnnot();
        List<String> base = Arrays.asList("Quiche", "Pizza", "Spaghetti", "Lasagne");
        List<String> mode = Arrays.asList("bolognese", "végé", "Joris", "mexicaine");
        Random r= new Random();
        p.nom = (base.get(r.nextInt(base.size()))+" a la "+mode.get(r.nextInt(mode.size())));
        p.nomSansIndex = p.nom;
        p.prixUnitaire = (10+r.nextInt(100));
        p.tax = (ProduitAnnot.TaxType.BaseProduct);
        return p;
    }

}
