import java.io.*;
import java.util.*;

public class Quiz {

public static String[][] csv2Array(String csvDatei) {
  int zeilen = 0;
  int elemente = 0;
  String zeile;
  BufferedReader csvZeilen = null;
  String[][] csv2Array = null;
  String fs = System.getProperty("file.separator"); // das Slash-problem elemenieren...

  try {
      csvZeilen = new BufferedReader(new java.io.FileReader(new File(System.getProperty("user.dir")+fs+"quizprojekt"+fs+"data"+fs+csvDatei+".csv")));
      while ((zeile = csvZeilen.readLine()) != null) {
          if (zeilen == 2) {
              elemente = (zeile.split("; ")).length; 
          }
          zeilen++;
      }
      csvZeilen.close();

      csv2Array = new String[zeilen - 2][elemente]; // deklariern des Arrays
      
      int zeilennr = 0; // Wenn zeilennr 0 ist befindet man sich in der Datei in der 1. Zeile (juri)

      csvZeilen = new BufferedReader(new java.io.FileReader(System.getProperty("user.dir")+fs+"quizprojekt"+fs+"data"+fs+csvDatei+".csv")); // Auslesen von csvDatei
      while ((zeile = csvZeilen.readLine()) != null) {
          if (zeilennr>1) {
              String[] splitarr = zeile.split("; "); // Splitten der Zeilen
              csv2Array[zeilennr-2][0] = splitarr[0]; // erstes Elementobjekt über index 0
              //System.out.print(splitarr[0]);
              csv2Array[zeilennr-2][1] = splitarr[1]; // zweites Element über index 1 
              if (splitarr.length > 2) {
                  csv2Array[zeilennr-2][2] = splitarr[2]; // drittes Element über index 2 
              }
              if (splitarr.length > 3) {
                  csv2Array[zeilennr-2][3] = splitarr[3]; // viertes Element über index 3 
              }
          }
          zeilennr++;
      }
  } catch (IOException ex) {
      ex.printStackTrace();
  } finally {
      try {
          if (csvZeilen != null) {
              csvZeilen.close();
          }
      } catch (IOException ex) {
          ex.printStackTrace();
      }
  }
  
  return csv2Array;  
}

// csvAusgabe gibt den Inhalt der verschieden csv-Dateien im Terminal aus (datei ohne ".csv")
public static void csvAusgabe(String datei) {
    System.out.println(datei);
    String[][] csv2Array_=csv2Array(datei);
    for (int i=0; i<csv2Array_.length; i++){
        String ausgabe="";
        //System.out.println(i);
        for (int j=0; j<(csv2Array_[i].length); j++) {
            //System.out.println(j);
            ausgabe = ausgabe + csv2Array_[i][j];
            if ((j<(csv2Array_[i].length)-1)) ausgabe = ausgabe +" | ";
        }
        System.out.println(ausgabe);
    }
}

public static String[][] antwortenZurFrage(int idkat, int idfrage) {
  final String[][] csvAntworten = csv2Array("antworten");
  final String[][] antworten = new String[4][6];
  
  // Kopiere die Antworten zur gegebenen Frage in das Array
  int index = 0; //id-antwort
  Random rnd = new Random();
  for (String[] antwort : csvAntworten) {
      if ((Integer.parseInt(antwort[0])==idkat) && (Integer.parseInt(antwort[1]) == idfrage)) {
          for (int j = 0; j < 5; j++) {
              antworten[index][j] = antwort[j];
          }
          antworten[index][5] = String.valueOf(rnd.nextInt(100));
          index++;
      }
  }
  
  // Sortiere das Array basierend auf der letzten Spalte
  sort2DArrayByColumn(antworten, 5);
  
  return antworten;
}

public static String[][] kategorien(){
    final String[][] csvKategorien = csv2Array("kategorien");

    return csvKategorien;
}

public static String[][] fragenRandom() {
    final String[][] csvFragen = csv2Array("fragen");
    final String[][] fragen = new String[csvFragen.length][4];
    int index = 0;
    Random rnd = new Random();

    for (String[] antwort : csvFragen) {
            for (int j = 0; j < 3; j++) {
                fragen[index][j] = antwort[j];
            }
            fragen[index][3] = String.valueOf(rnd.nextInt(100));
            index++;
    }
    sort2DArrayByColumn(fragen, 3);
    return fragen;
}

public static void termQuizStarten() {
    //fragenRandom();
    String[][] fragen = fragenRandom();
    String[][] antworten;
    String[][] kategorien=kategorien();
    String richtig = "";
    Scanner sc = new Scanner(System.in);
    //int idfrage;
    
    System.out.println("");
    System.out.println("Aus welcher Katgorie sollen die Fragen im Quiz kommen?");
    System.out.println("");

    for (int l=1; l<(kategorien.length+1); l++) {
        System.out.println(l+": "+kategorien[(l-1)][1]);
    }

    System.out.print("? ");
    String kat = sc.next();
    
    for (int i=1; i<fragen.length; i++) {
        System.out.println("");
        System.out.println(fragen[i][2]+"?");
        System.out.println("");
        
        String idkat = fragen[i][0];
        String idfrage = fragen[i][1];
        antworten = antwortenZurFrage(Integer.parseInt(idkat), Integer.parseInt(idfrage));
        //sort2DArrayByColumn(antworten, 4);
        
        for (int k=1; k<5; k++) {
            if (antworten[(k-1)][4].equals("1")) {
                richtig = String.valueOf(k);
            }
            System.out.println(k+": "+antworten[(k-1)][3]);
        }

        //kann noch verbessert werden
        System.out.print("? ");
        String eingabe = sc.next();
        if (!eingabe.matches("[1-4]")) {
            System.out.println("");
            System.out.println("Ungültige Eingabe!");
            System.out.print("? ");
            eingabe = sc.next();
        }
        else if (eingabe.equals(richtig)) {
            System.out.println("");
            System.out.print("Das ist richtig, ;-)");
            System.out.println("");
        }
        else {
            System.out.println("");
            System.out.print("Leider falsch, :-(");
            System.out.println("");
        }
    }       
    System.out.println("Quiz zu Ende...");
    System.out.println("");
    sc.close(); 
}

  public static void sort2DArrayByColumn(String[][] array, final int column) {
    Arrays.sort(array, new Comparator<String[]>() {
        @Override
        public int compare(String[] row1, String[] row2) {
            // Vergleiche die Werte in der angegebenen Spalte
            return row1[column].compareTo(row2[column]);
        }
    });
  }

  public static void main(String args[]) {
    //csvAusgabe("antworten");
    termQuizStarten();
    //System.out.println(System.getProperty("user.dir"));
  }
}

