package org.deguet.sql;

import android.util.Log;

import org.deguet.sql.model.ProduitAnnot;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public abstract class TestAbstract {
	
	SCRUD<ProduitAnnot> repository;
	long performanceCount = 10000;

    @Before
    public void setUp() {
        repository = getRepo();
        repository.deleteAll();
    }

    public abstract SCRUD<ProduitAnnot> getRepo();

    @After
	public void tearDown() {
		repository.deleteAll();
		repository = null;
	}

    @Test
	public void testSaveAndGetAll(){
        ProduitAnnot p = build("Produit ", 10 , ProduitAnnot.TaxType.BaseProduct);
		repository.save(p);
		Assert.assertEquals(repository.getAll().size(), 1);
	}

    @Test( expected = Exception.class)
    public void testSaveTwice(){
        ProduitAnnot p = build("Produit ", 10 , ProduitAnnot.TaxType.BaseProduct);
        repository.save(p);
        repository.save(p);
        //Assert.assertEquals(repository.getAll().size(), 1);
    }

    @Test
    public void testSaveManyAndGetAll(){
        List<ProduitAnnot> prods = new ArrayList<>();
        int size = 22;
        for (int i = 0 ; i < size ; i++){
            ProduitAnnot p = build("Produit " + i, i*10, ProduitAnnot.TaxType.BaseProduct);
            prods.add(p);
            repository.save(p);
        }

        Assert.assertEquals(prods.size(), repository.getAll().size());
    }

    @Test
	public void testGetById(){
        ProduitAnnot p = build("Produit ", 10 , ProduitAnnot.TaxType.BaseProduct);
        long id = repository.save(p);
        ProduitAnnot recov = repository.getById(id);
        Assert.assertEquals(recov.prixUnitaire.intValue(), 10);
	}

    @Test
    public void testDeleteOneById(){
        ProduitAnnot p = build("Produit ", 10 , ProduitAnnot.TaxType.BaseProduct);
        long id = repository.save(p);
        Assert.assertEquals(1, repository.getAll().size());
        repository.deleteOne(id);
        Log.i("TestsCRUD",repository.getAll().toString());
        Assert.assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testDeleteAll(){
        List<ProduitAnnot> prods = new ArrayList<>();
        int size = 22;
        for (int i = 0 ; i < size ; i++) {
            ProduitAnnot p = build("Produit " + i ,  (i*10), ProduitAnnot.TaxType.BaseProduct);
            prods.add(p);
            repository.save(p);
        }
        Assert.assertEquals(size, repository.getAll().size());
        repository.deleteAll();
        Assert.assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testSaveOnePerformance(){
        long a = System.currentTimeMillis();
        for (int i = 0 ; i < performanceCount ; i++){
            ProduitAnnot p = build("Produit " + i ,  (i*10), ProduitAnnot.TaxType.BaseProduct);
            repository.save(p);
        }
        long b = System.currentTimeMillis();
        Log.i("TestLoad", repository.getClass().getSimpleName()+"  : temps est " + (b - a) + " ms");
    }

    @Test
    public void testSaveOnePerformanceBatch(){
        List<ProduitAnnot> list = new ArrayList<>();
        long a = System.currentTimeMillis();
        for (int i = 0 ; i < performanceCount ; i++){
            ProduitAnnot p = build("Produit " + i ,  (i*10), ProduitAnnot.TaxType.BaseProduct);
            list.add(p);
        }
        repository.saveMany(list);
        long b = System.currentTimeMillis();
        Log.i("TestLoad", repository.getClass().getSimpleName()+"  : temps est " + (b - a) + " ms");
    }

    @Test
    public void testDrolesDeTypes(){
        String japanese = "日本語";
        ProduitAnnot p = build(japanese, Integer.MAX_VALUE, ProduitAnnot.TaxType.BaseProduct);
        long id = repository.save(p);
        // recover and tests for equality
        ProduitAnnot recov = repository.getById(id);
        Assert.assertEquals(japanese,                   recov.nom);
        Assert.assertEquals(Integer.MAX_VALUE,          recov.prixUnitaire.intValue());
    }

    private static ProduitAnnot build(String nom, int prix, ProduitAnnot.TaxType tax){
        ProduitAnnot p = new ProduitAnnot();
        p.nom = nom;
        p.nomSansIndex = nom;
        p.prixUnitaire = prix;
        p.tax = tax;
        return p;
    }

}
