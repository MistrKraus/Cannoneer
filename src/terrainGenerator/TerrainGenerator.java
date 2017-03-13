package terrainGenerator;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Pom�cka pro semestr�ln� pr�ci z UPG ke generov�n� jednoduch�ho souboru s ter�nem.
 * <p>
 * T��da TerrainGenerator je pouze ob�lkou n�kolika statick�ch metod.
 * Jeliko� jde o trivi�ln� aplikaci, nem� smysl vytv��et cokoliv slo�it�j��ho.
 * <p>
 * Program na�te parametry p��kazov� ��dky, zkontroluje jejich spr�vnost a vygeneruje 
 * bin�rn� soubor s ter�nem. Mal� ter�ny sou�asn� pro kontrolu vyp�e na obrazovku.
 * <p>
 * Sou�asn� slou�� jako p��klad
 * <ul>
 * <li>jak m� vypadat archiv s odevzdanou semestr�lkou,</li>
 * <li>jak vytvo�it r�zn� d�vky (pro kompilaci, spu�t�n� apod.)</li>
 * <li>jak vypad� okomentovan� zdrojov� k�d,</li>
 * <li>jak �e�it zpracov�n� p��kazov� ��dky,</li>
 * <li>jak manipulovat s bin�rn�m souborem.</li>
 * </ul>
 * 
 * @author KIV/UPG
 * @version 20170210
 * @since 2016
 */
public class TerrainGenerator {

	/**
	 * Program o�ek�v� NUMBER_OF_PARAMS parametr� na p��kazov� ��dce.
	 */
	public static final int NUMBER_OF_PARAMS = 11;
	
	/** 
	 * 2D pole nadmo�sk�ch v��ek; 
	 * indexov�n�: terrain[Y][X]
	 */
	public static int[][] terrain = null;
	
	static int deltaX, deltaY;
	static int shooterX, shooterY;
	static int targetX, targetY;
	static String fName;

	/* pro p�evod mm na m */
	static final double mmToM = 1000.0;
	
	/**
	 * Generuje ploch� ter�n
	 * @param  columns  po�et sloupc� (���ka mapy)
	 * @param  rows     po�et ��dk� (v��ka mapy)
	 * @param  value    (konstantn�) nadmo�sk� v��ka v mm
	 * @return 2D pole  nadmo�sk�ch v��ek
	 */	
	public static int[][] generateFlatTerrain(int columns, int rows, int value)
	{
		int[][] terrain = new int[rows][columns];
		int x, y;
		
		for (y = 0; y < rows; ++y) {
			for (x = 0; x < columns; ++x) {
				terrain[y][x] = value;
			}
		}
		
		return terrain;
	}
	
	/**
	 * Generuje ter�n typu "rovina naklon�n� ve sm�ru osy x"  
	 * @param  columns       po�et sloupc� (���ka mapy)
	 * @param  rows          po�et ��dk� (v��ka mapy)
	 * @param  inclination   sklon roviny (p��r�stek mezi dv�ma sloupci)<br>
	 *                       sklon 0 = zcela ploch� ter�n<br>
	 *                       sklon 1 = mezi dv�ma sloupci je p��r�stek 1 mm<br>
	 * @return 2D pole nadmo�sk�ch v��ek
	 */
	public static int[][] generateSweepTerrain(int columns, int rows, double inclination)
	{
		int[][] terrain = new int[rows][columns];
		int x, y;
		
		for (y = 0; y < rows; y++) {
			for (x = 0; x < columns; x++) {
				terrain[y][x] = (int)(x * inclination);
			}
		}
		
		return terrain;
	}

	/**
	 * Ulo�� ter�n do bin�rn�ho souboru. Specifikace souboru viz zad�n� semestr�ln� pr�ce
	 * nebo zdrojov� k�d metody.
	 */
	public static void saveTerrain() 
	{
		int rows = terrain.length;
		int columns = terrain[0].length;
		
		DataOutputStream fout = null;
		try {
			// Samotn� z�pis dat
			fout = new DataOutputStream(new FileOutputStream(fName));
			
			fout.writeInt(columns);
			fout.writeInt(rows);
			fout.writeInt(deltaX);
			fout.writeInt(deltaY);
			fout.writeInt(shooterX);
			fout.writeInt(shooterY);
			fout.writeInt(targetX);
			fout.writeInt(targetY);
			
			for (int y = 0; y < rows; ++y) {
				for (int x = 0; x < columns; ++x) {
					fout.writeInt(terrain[y][x]);
				}
			}			
		} 
		/*
		 * N�sleduje pouze zav�en� souboru a o�etren� v�jimek
		 */
		catch (FileNotFoundException e) {
			System.err.println("Nepovedlo se otevrit vystupni soubor.");
		} 
		catch (IOException e) {
			System.err.println("Nepovedlo se zapsat vystupni soubor.");
		} 
		finally {
			try {
				if (fout != null) {
					fout.close();		
				}
			}
			catch (IOException e) {
				System.err.println("Nepovedlo se uzavrit vystupni soubor.");
			}
		}
	}
	
	
	/**
	 * Vytiskne metadata ter�nu, st�elce a c�le
	 */	
	public static void printTerrainData() 
	{
		int rowsCount = terrain.length;
		int columnsCount = terrain[0].length;
		
		System.out.println(String.format("Pocet sloupcu: %d, pocet radku: %d", columnsCount, rowsCount));
		System.out.println(String.format("Rozestup mezi sloupci %.3f m, mezi radky %.3f m", 
				deltaX / mmToM, deltaY / mmToM));
		System.out.println(String.format("Rozmery oblasti: sirka %.3f m, vyska %.3f m" , 
				columnsCount * deltaX / mmToM, rowsCount * deltaY / mmToM));
		System.out.println(String.format("Poloha strelce: sloupec %d, radek %d, tj. x = %.3f m, y = %.3f m", 
				shooterX, shooterY, shooterX * deltaX / mmToM, shooterY * deltaY / mmToM));
		
		if (shooterX < 0 || shooterX >= columnsCount ||
     		shooterY < 0 || shooterY >= rowsCount) 
		{
			System.out.println("STRELEC JE MIMO MAPU !");
		} else {
			System.out.println(String.format("   nadmorska vyska strelce %.3f m", terrain[shooterY][shooterX] / mmToM));
		}
		
		
		System.out.println(String.format("Poloha cile: sloupec %d, radek %d, tj. x = %.3f m, y = %.3f m", 
				targetX, targetY, targetX * deltaX / mmToM, targetY * deltaY / mmToM));

		if (targetX < 0 || targetX >= columnsCount ||
	     	targetY < 0 || targetY >= rowsCount) 
			{
				System.out.println("CIL JE MIMO MAPU !");
			} else {
				System.out.println(String.format("   nadmorska vyska cile %.3f m", terrain[targetY][targetX] / mmToM));
			}
	}
	
	
	/**
	 * Vytiskne vygenerovan� ter�n
	 */	
	public static void printTerrainAltitudes() 
	{
		int rowsCount = terrain.length;
		int columnsCount = terrain[0].length;
		final int tableColWidth = 11;
		
		if (rowsCount * columnsCount == 0) {
			System.out.println("TEREN NEOBSAHUJE ZADNA VYSKOVA DATA !");
		} else {
			System.out.println("Vysky jednotlivych bodu terenu v m:");
			System.out.println();
			
			System.out.print(String.format("           |%" + ((tableColWidth * columnsCount) / 2 + 2) + "s", "x [m]"));
			System.out.println();
			
			System.out.print("     y [m] |");
			for (int column = 0; column < columnsCount; column++) {
				System.out.print(String.format("%11.3f", column * deltaX / mmToM));
			}
			System.out.println();
	
			System.out.print("-----------+");
			for (int column = 0; column < columnsCount; column++) {
				System.out.print("-----------");
			}
			System.out.println();
			
			for (int row = 0; row < rowsCount; row++) {
				System.out.print(String.format("%10.3f |", row * deltaY / mmToM));
				for (int column = 0; column < columnsCount; column++) {
					System.out.print(String.format("%11.3f", terrain[row][column] / mmToM));
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * Na�ten� parametr� z p��kazov� ��dky, jejich kontrola
	 * a spu�ten� �innosti.
	 * Jeliko� je ve�ker� �innost jednoduch�, nem� smysl metodu d�le dekomponovat.
	 * Pokud by ale program umo��oval v�ce v�c�, bylo by vhodn� odd�lit
	 * zpracov�n� parametr� a vykon�n� �innosti.
	 * @param args   parametry p��kazov� ��dky
	 */
	public static void main(String args[])
	{
		if (args.length < NUMBER_OF_PARAMS) {
			System.out.println("Generator zkusebniho terenu.");
			System.out.println("Pouziti: java terrainGenerator/TerrainGenerator <typ> <f> <sirka> <vyska> <DeltaX> <DeltaY> <sX> <sY> <tX> <tY> <nazev>");
			System.out.println("<typ>     - typ terenu; r = rovny, s = sikmy");
			System.out.println("<f>       - vyska rovneho terenu [mm] / sklon sikmeho terenu [mm/sloupec]");
			System.out.println("<sirka>   - pocet sloupcu terenu");
			System.out.println("<vyska>   - pocet radek terenu");
			System.out.println("<DeltaX>  - rozestup sloupcu [mm]");
			System.out.println("<DeltaY>  - rozestup radek [mm]");
			System.out.println("<sX> <sY> - sloupec a radek, kde je umisten strelec (shooter)");
			System.out.println("<tX> <tY> - sloupec a radek, kde je umisten cil (target)");
			System.out.println("<nazev>   - nazev vystupniho souboru");
			System.out.println();
			System.out.println("Pro velmi male tereny (max. 5 radku a 5 sloupcu) se teren navic vypise na obrazovku.");
			System.out.println("Priklad: java terrainGenerator/TerrainGenerator r 1   200 100   1000 1000   5 5   100 50   rovny.ter");
			System.exit(0);
		}
		
		int argNo = 0;
		try {
			// �ten� parametr� p��kazov� ��dky
			String terrainType = args[argNo++];
			
			// TODO:
			// Hodnoty parametru <typ> (te� se �etezce "r" a "s" vyskytuj� v n�pov�d� v��e
			// a v testech n�e. P�i roz�i�ov�n� programu to vy�e�it jinak!
			if (!(terrainType.equals("r") || terrainType.equals("s"))) {
				throw new IllegalArgumentException();
			}
			
			double value = Double.parseDouble(args[argNo++]);
			int columns  = Integer.parseInt(args[argNo++]);
			int rows     = Integer.parseInt(args[argNo++]);
			deltaX       = Integer.parseInt(args[argNo++]);
			deltaY       = Integer.parseInt(args[argNo++]);
			shooterX     = Integer.parseInt(args[argNo++]);
			shooterY     = Integer.parseInt(args[argNo++]);
			targetX      = Integer.parseInt(args[argNo++]);
			targetY      = Integer.parseInt(args[argNo++]);
			fName        = args[argNo++];
			
			// Vygenerov�n� a ulo�en� ter�nu
			if (terrainType.equals("r")) {
				terrain = generateFlatTerrain(columns, rows, (int)value);
			} else if (terrainType.equals("s")) {
				terrain = generateSweepTerrain(columns, rows, value);
			}
			
			saveTerrain();
			printTerrainData();
			if (columns <= 5 && rows <= 5) {
				System.out.println();
				printTerrainAltitudes();
			}
		} 
		catch(IllegalArgumentException e) {
			/* v�jimka IllegalArgumentException rovne� pokr�v�
			 * NumberFormatException, kterou m��e generovat parseInt a parseDouble.
			 * Jedin� parametr nepokryt� v�jimkou je fName; na nekorektn� n�zev souboru 
			 * tak zareaguje a� metoda saveTerrain() 
			 */
			System.err.println("Chyba v zadani parametru " + argNo);
		}
	}
}
