package es.ubu.lsi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataReader {

	private BufferedReader br;
	
	/**
	 * 
	 * @param filePath
	 */
	public void openFile(String filePath) {
		
		File file = new File (filePath);
		FileReader fr;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 */
	public void closeFile() {
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean ready() {
		try {
			return br.ready();
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int readDatum (){
		
		String line = null;

		try {

			do{
				line = br.readLine();
			}while(line.startsWith("#"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] separados = line.split(",");
		return Integer.valueOf(separados[1]).intValue();
	}
	
	public String[] readLine(){
		
		String line = null;
		String[] separados = null;
		
		try {
			
			do{
				line = br.readLine();
			}while(line.startsWith("#"));
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		separados = line.split(",");
		return separados;
	}

	
	/**
	 * 
	 * @param filas
	 * @param columnas
	 * @return
	 */
	public int[][] readMatrix(int filas, int columnas) {

		String line = null;
		int leidas = 0;
		int [][] datos = new int[filas][columnas];
		
		try {
			
			while(br.ready() && leidas < filas)
			{
				line = br.readLine();

				// Si es un comentario, no se lee
				if(line.startsWith("#"))
					continue;

				String[] separados = line.split(",");
				for(int j=0; j<separados.length; j++){
					datos[leidas][j] = Integer.valueOf(separados[j]).intValue(); // X Value of the interesting point
				}
				leidas++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return datos;
		
	}

}
