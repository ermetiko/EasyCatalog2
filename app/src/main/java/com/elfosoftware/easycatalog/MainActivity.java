package com.elfosoftware.easycatalog;

//import android.R.bool;

import java.io.File;
import java.util.ArrayList;

import com.elfosoftware.easycatalog.Adapters.Articolo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity implements ListaCatalogo.OnSottocategoriaSelectedListener {

    public DBHelper databaseHelper;
    public ArrayList<Articolo> listaArticoli;
    public File microSdPath;
    public static File immaginiPath;
    private int stato = 0;
    private boolean caricato = false;

    //public MenuItem mnuRecordCount = null;
    /*
	@Override
    protected void onResume() {
		super.onResume();
		if (!caricato)
		{
			caricato=true;
			if (!databaseHelper.exitsDataBaseFile()){
				//copia il db
				String[] listaFiles = new String[] {"easycatalog.zip"};
				Updater upd = new Updater();
				if (upd.scaricaFileFtp(this, listaFiles))
				{
				}
			}
		}
	}
	*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File microSdPath = getMicroSdPath();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            messaggio("Inizializzazione", "Impossibile accedere alla microSD");
        }
        else {
            immaginiPath = new File(microSdPath, "immagini");
            if (!immaginiPath.exists())
                messaggio("Inizializzazione", "Impossibile reperire la cartella immagini");
        }

        setContentView(R.layout.activity_main);

        //carica la lista e la griglia media
        ListaCatalogo frgLista = new ListaCatalogo();
        GrigliaMedia fragment = new GrigliaMedia();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragmentlista, frgLista);
        ft.add(R.id.fragmentgriglia, fragment);
        ft.commit();


        databaseHelper = new DBHelper(this);
        if (databaseHelper.createDataBase()) {
            // setup action bar for tabs
            ActionBar actionBar = getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayShowTitleEnabled(true);

            Tab tab = actionBar.newTab()
                    .setText("Catalogo")
                    .setTabListener(new TabsListener());
            actionBar.addTab(tab);

            tab = actionBar.newTab()
                    .setText("Offerte")
                    .setTabListener(new TabsListener());
            actionBar.addTab(tab);

            tab = actionBar.newTab()
                    .setText("Carrello")
                    .setTabListener(new TabsListener());
            actionBar.addTab(tab);
        }
    }

    public void onSottocategoriaSelected(ArrayList<Articolo> listaArticoli) { //int idFornitore, int idCategoria, int idSottocategoria) {
        this.listaArticoli = listaArticoli;
        android.app.Fragment frg = getFragmentManager().findFragmentById(R.id.fragmentgriglia);
        if (frg instanceof GrigliaMedia)
            ((GrigliaMedia) frg).caricaGriglia(listaArticoli);
        else
            ((GrigliaGrande) frg).caricaGriglia(listaArticoli);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_importaAggiornamenti:
                boolean ok = true;
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    DomandaFragment domanda = new DomandaFragment();
                    domanda.show(getFragmentManager(), "Importazione articoli");
                    //importaArticoli();
                    //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    messaggio("Importazione articoli", "Nessuna connessione disponibile.");
                    ok = false;
                }
                return ok;
            case R.id.menu_settings:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cerca:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                ingrandisci();
                return true;
            case R.id.menu_carrello:
                Toast.makeText(getApplicationContext(), "Articolo aggiunto al carrello.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        //mnuRecordCount= menu.findItem(R.id.menu_record);
        return true;
    }

    private static class TabsListener implements ActionBar.TabListener {

        public TabsListener() {
            //this.fragment = fragment;
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            //Toast.makeText(StartActivity.appContext, "Reselected!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            //ft.replace(R.id.fragment_container, fragment);
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            //ft.remove(fragment);
        }

    }

    private void importaArticoli() {
        String[] listaFiles = new String[]{"fornitori.txt", "maga.txt"};
        Updater upd = new Updater();
        if (upd.scaricaFileFtp(this, listaFiles)) {
            ListaCatalogo lista = (ListaCatalogo) (getFragmentManager().findFragmentById(R.id.fragmentlista));
            if (lista != null)
                lista.caricaFornitori();
        }
    }

    public void ingrandisci() {
        ingrandisci(-1);
    }


    public void ingrandisci(int posizione) {
        if (stato == 0) {
            FragmentManager fm = getFragmentManager();
            //android.app.Fragment frgLista = fm.findFragmentById(R.id.fragmentlista);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            //ft.hide(frgLista);

            //findViewById(R.id.fragmentlista).setVisibility(View.GONE);

            //Fragment frgLista = getFragmentManager() (Fragment)this.findViewById(R.id.fragmentlista)
            //findViewById(R.id.fragmentlista).setVisibility(View.INVISIBLE);

            //Adapters.dimensioneGrigliaGrande= (int)Math.ceil(Math.random()*500);
            stato = 1;
            GrigliaGrande fragment = new GrigliaGrande();
            Bundle args = new Bundle();
            args.putInt("POSIZIONE", posizione);
            fragment.setArguments(args);

            //FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentgriglia, fragment);
            ft.commit();
            //GridView griglia = (GridView) this.findViewById(R.id.griglia);
            //griglia.setAdapter(new Adapters.ImmagineLargeAdapter(this, R.layout.immagine_large, listaArticoli));
        } else {
            stato = 0;
            //findViewById(R.id.fragmentlista).setVisibility(View.VISIBLE);

            GrigliaMedia fragment = new GrigliaMedia();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentgriglia, fragment);
            ft.commit();
            //GridView griglia = (GridView) this.findViewById(R.id.griglia);
            //griglia.setAdapter(new Adapters.ThumbsAdapter(this, R.layout.thumb, listaArticoli));
        }
        if (posizione != -1) {

        }
		/*
		GridView grid = (GridView)this.findViewById(R.id.griglia);
		if (grid!=null)
		{
			grid.setColumnWidth(360);
		}
		ImageView img = (ImageView)this.findViewById(R.id.thumbImmagine);
		if (img!=null)
		{
			img.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 360));
		}
		*/
    }

    public class DomandaFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Confermi l'importazione degli articoli?")
                    .setTitle("Importazione articoli")
                    .setPositiveButton("Importa", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            importaArticoli();
                        }
                    })
                    .setNegativeButton("Abbandona", null);
            return builder.create();
        }
    }

    public void mostraErrore(String titolo, Exception ex) {
        messaggio(titolo, ex.getMessage());
    }


    public void messaggio(String titolo, String testo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //.create();
        builder.setTitle(titolo);
        builder.setMessage(testo);
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.setNeutralButton("OK", null);
        builder.create().show();
    }

    private File getMicroSdPath() {
        File pathSD = null;
        try {
            File[] allDir = getApplicationContext().getExternalFilesDirs(null);
            if ((allDir.length == 1) || Environment.isExternalStorageRemovable()) {
                pathSD = allDir[0];
            } else {
                pathSD = allDir[1];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pathSD;
    }
}
