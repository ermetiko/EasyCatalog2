package com.elfosoftware.easycatalog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

import org.apache.commons.net.ftp.FTPClient;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;


public class Updater {

	private MainActivity main;
	private String[] listaFiles;
	
	
	public boolean scaricaFileFtp(MainActivity m, String[] files) {
		boolean ok=true;
		main= m;
		listaFiles= files;
		scaricaFile sca = new scaricaFile();
		ok= !sca.execute(listaFiles).isCancelled();
		return ok;
	}

	public boolean salvaFileInDb()
	{
		boolean ok=true;
		salvaFile   salva = new salvaFile();
		ok = !salva.execute(listaFiles).isCancelled();
		return ok;
	}
	
	
	private class scaricaFile extends AsyncTask<String, Integer, String> {

		private ProgressDialog pb;
		private Exception ex=null;
		public boolean ok=true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pb= creaProgressBar("Download file (operazione 1/2)...", listaFiles.length);
		}

		@Override
		protected String doInBackground(String... listaFiles) {

			FTPClient ftpClient = new FTPClient();
			int numeroFile=0;
			//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			//StrictMode.setThreadPolicy(policy);
			ok = true;
			try
			{
				Resources res = main.getResources();
				String ftpUrl = res.getString(R.string.ftpUrl);
				String ftpFolder = res.getString(R.string.ftpFolder);
				String ftpUser = res.getString(R.string.ftpUser);
				String ftpPassword = res.getString(R.string.ftpPassword);

				ftpClient.connect(ftpUrl);
				if (ftpClient.login(ftpUser, ftpPassword))
				{
					//client.setFileType(FTP.ASCII_FILE_TYPE);
					ftpClient.enterLocalPassiveMode(); // important!
					ftpClient.cwd(ftpFolder);
					String cartella = main.getFilesDir().getAbsolutePath() +"/cartesio";                
					File fileOutput = new File(cartella);
					if(!fileOutput.exists())
						fileOutput.mkdirs();
					fileOutput=null;

					for(int i=0; i< listaFiles.length; i++)
					{
						fileOutput = new File(cartella + "/" + listaFiles[i]);
						fileOutput.createNewFile();

						/*
                    InputStream in = ftpClient.retrieveFileStream(listaFiles[i]); //
                    InputStreamReader ins=new InputStreamReader(in);
                    BufferedReader br= new BufferedReader(ins);
                    StringBuilder sb= new StringBuilder();
                    String linea=br.readLine();
                    while(linea!=null)
                    {
                    	sb.append(linea);
                    	linea=br.readLine();
                    	Log.d("FTP", linea);
                    }
                    br.close();
                    ins.close();
                    in.close();
						 */
						//ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
						FileOutputStream objFos=new FileOutputStream(fileOutput);
						ok = ftpClient.retrieveFile(listaFiles[i] , objFos);
						publishProgress(++numeroFile); //Integer.toString(passoIntero));
						objFos.flush();
						objFos.close();
						if (!ok)
							throw new Exception("Errore durante il download del file "+  listaFiles[i]);
					}  
					// boolean result = con.storeFile("/QIA/Response/test/Responses.xml", in);
					//if (blnresult) dlgAlert.setMessage("Questions Are Successfully Downloaded").create().show();
				}
			}
			catch (Exception e)
			{
				ex=e;
				cancel(true);
			}
			try
			{
				ftpClient.logout();
				ftpClient.disconnect();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			pb.setProgress(progress[0]); //Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String msg) {
			pb.dismiss();
			if (ok)
				salvaFileInDb();
		}
		
		@Override
	    protected void onCancelled() {
	        super.onCancelled();
	        ok=false;
	        pb.dismiss();
			main.mostraErrore("Download file", ex);
			ex.printStackTrace();
			
	    }		
	}		

	

	private ProgressDialog creaProgressBar(String titolo, int MaxValue)
	{
        ProgressDialog progressBar = new ProgressDialog(main);
        progressBar.setCancelable(false);
        progressBar.setMessage(titolo);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(MaxValue);
        progressBar.show();
        return progressBar;
	}
	
	
	private class salvaFile extends AsyncTask<String, Integer, String> {
		
		private ProgressDialog pb;
		int cntFornitori=0;
		int cntArticoli=0;				
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb= creaProgressBar("Salvataggio file (operazione 2/2)...", 100);
		}
	
		@Override
		protected String doInBackground(String... listaFiles) {
			SQLiteDatabase db = null;
			
			int passoIntero=0;
			long lunghezza=0;
			for (String nome : listaFiles) {
				String nomeFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cartesio/" + nome; //getFilesDir().getAbsolutePath() +"/cartesio/" + nome;
				File file = new File(nomeFile);
				if (file.exists())
					lunghezza= file.length();
			}
			
			for (String nome : listaFiles) {
				db = main.databaseHelper.getWritableDatabase();
				boolean fornitori = false;
				boolean magazzino = false;
				boolean sezioneCategorie = false;
				boolean sezioneArticoli = false;
				boolean sezioneOfferte = false;

				if (nome.equals("fornitori.txt"))
				{
					fornitori=true;
					FornitoriDB.deleteAll(db);
				}
				else if (nome.equals("maga.txt"))
				{
					magazzino=true;
					CategorieArticoliDB.deleteAll(db);
					ArticoliDB.deleteAll(db);
				}
				db.close();

				String nomeFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cartesio/" + nome; //getFilesDir().getAbsolutePath() +"/cartesio/" + nome;
				File file = new File(nomeFile);
				if (file.exists())
				{
					long letti=0;
					float percentuale=0;
					FileInputStream fis = null;
					try 
					{
						fis = new FileInputStream(file);
						InputStreamReader isr = new InputStreamReader(fis);
						BufferedReader buffreader = new BufferedReader(isr);
						String line = null;
						Date dataVuota = new Date(0);
						db = main.databaseHelper.getWritableDatabase();
						db.beginTransaction();
						try 
						{
							while ((line= buffreader.readLine()) != null) {
								if (!line.isEmpty())
								{
									letti += line.length();
									percentuale = ((float)letti/lunghezza) * 100F;
									//percentuale *= 100;
									if (passoIntero != (int)percentuale)
									{
										passoIntero= (int)percentuale;
										publishProgress(passoIntero); //Integer.toString(passoIntero));
									}
									
									if (fornitori)
									{
										String[] parms = line.split("\\|"); //, 0);
										FornitoriDB.insertFornitore(db, Integer.decode(parms[0]), parms[1]);
										cntFornitori++;
									}
									else if (magazzino)
									{
										if (line.startsWith(":"))
										{
											sezioneCategorie = (line.equals(":famiglie"));
											sezioneArticoli = (line.equals(":articoli"));
											sezioneOfferte = (line.equals(":offerte"));
										}
										else
										{
											if (sezioneCategorie)
											{
												String[] parms = line.split("\\|");
												CategorieArticoliDB.insertCategoria(db, Integer.decode(parms[0]), parms[2], Integer.decode(parms[1]));
											}
											else if (sezioneArticoli)
											{
												cntArticoli++;
												String[] parms = line.split("\\|");
												ArticoliDB.insertArticolo(db, Integer.decode(parms[0]), parms[1], Integer.decode(parms[28]), Integer.decode(parms[29]), false, dataVuota, Integer.decode(parms[30]));
											}
											if (sezioneOfferte)
											{
												//String[] parms = line.split("\\|");
												//CategorieArticoliDB.insertCategoria(db, Integer.decode(params[0]), params[2], Integer.decode(params[1]));
											}
										}
									}
								}
								//Log.d("leggi", line);
								//line = buffreader.readLine();
							}
							buffreader.close();
							isr.close();
							db.setTransactionSuccessful();
						} 
						catch (IOException e) 
						{
							main.mostraErrore("Importazione articoli", e);	
							e.printStackTrace();
						}
						finally {
							db.endTransaction();
						}                	
						if (fornitori)
							;//caricaFornitori((Spinner) findViewById(R.id.spnMarche));
						else if (magazzino)
							; //caricaCategorie((Spinner) findViewById(R.id.spnFamiglia));
					} catch (FileNotFoundException e) 
					{
						main.mostraErrore("Importazione articoli", e); 
						e.printStackTrace();
					}
				}
			}
			db.close();
			return null;
		}
		
		
		protected void onProgressUpdate(Integer... progress) {
			pb.setProgress(progress[0]); //Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String msg) {
			pb.dismiss();
			main.messaggio("Importazione terminata.","Importati " + Integer.toString(cntArticoli)+ " articoli e " + Integer.toString(cntFornitori)+ " fornitori.");
			//Toast.makeText(main, "Importati " + Integer.toString(cntArticoli)+ " articoli e " + Integer.toString(cntFornitori)+ " fornitori.", Toast.LENGTH_SHORT).show();		}
		}
	}
	
}
