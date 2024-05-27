import java.io.*;
import java.util.*;

public class Quiz {

public static String[][] csv2Array(String csvDatei) {
    int zeilen = 0;
    int elemente = 0;
    String zeile;
    BufferedReader csvZeilen = null;
    String[][] csv2Array = null;
    String fs = System.getProperty("file.separator");

    try {
        csvZeilen = new BufferedReader(new java.io.FileReader(new File(System.getProperty("user.dir") + fs + "quizprojekt" + fs + "data" + fs + csvDatei + ".csv")));
        while ((zeile = csvZeilen.readLine()) != null) {
            if (zeilen == 2) {
                elemente = (zeile.split("; ")).length;
            }
            zeilen++;
        }
        csvZeilen.close();

        csv2Array = new String[zeilen - 2][elemente];

        int zeilennr = 0;

        csvZeilen = new BufferedReader(new java.io.FileReader(System.getProperty("user.dir") + fs + "quizprojekt" + fs + "data" + fs + csvDatei + ".csv"));
        while ((zeile = csvZeilen.readLine()) != null) {
            if (zeilennr > 1) {
                String[] splitarr = zeile.split("; ");
                for (int i = 0; i < splitarr.length; i++) {
                    csv2Array[zeilennr - 2][i] = splitarr[i];
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

public static String[][] antwortenZurFrage(String idkat, String idfrage) {
    final String[][] csvAntworten = csv2Array("antworten");
    final String[][] antworten = new String[4][6];

    // Initialisiere das antworten Array
    for (int i = 0; i < antworten.length; i++) {
        antworten[i] = new String[6];
    }

    // Kopiere die Antworten zur gegebenen Frage in das Array
    int index = 0; // id-antwort
    Random rnd = new Random();
    for (String[] antwort : csvAntworten) {
        if (antwort[0].equals(idkat) && antwort[1].equals(idfrage)) {
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

public static String[][] fragenRandom(String kat) {
    final String[][] csvFragen = csv2Array("fragen");
    final List<String[]> fragenList = new ArrayList<>();
    Random rnd = new Random();

    for (String[] frage : csvFragen) {
        if (frage[0].equals(kat)) {
            String[] neueFrage = new String[4];
            for (int j = 0; j < 3; j++) {
                neueFrage[j] = frage[j];
            }
            neueFrage[3] = String.valueOf(rnd.nextInt(100));
            fragenList.add(neueFrage);
        }
    }

    String[][] fragen = new String[fragenList.size()][4];
    for (int i = 0; i < fragen.length; i++) {
        fragen[i] = fragenList.get(i);
    }

    sort2DArrayByColumn(fragen, 3);
    return fragen;
}

public static void termQuizStarten() {
    String[][] kategorien = kategorien();
    int richtige=0, falsche=0;
    Scanner sc = new Scanner(System.in);

    System.out.println("Aus welcher Kategorie sollen die Fragen im Quiz kommen?");
    for (int l = 1; l <= kategorien.length; l++) {
        System.out.println(l + ": " + kategorien[l - 1][1]);
    }

    System.out.print("? ");
    String kateingabe = sc.next();

    String[][] fragen = fragenRandom(kateingabe);
    String[][] antworten;
    String richtig = "";

    for (int i = 0; i < (fragen.length/2); i++) {
        System.out.println("");
        System.out.println(fragen[i][2] + "?");

        String idkat = fragen[i][0];
        String idfrage = fragen[i][1];
        antworten = antwortenZurFrage(idkat, idfrage);

        for (int k = 1; k <= 4; k++) {
            if (antworten[k - 1][4].equals("1")) {
                richtig = String.valueOf(k);
            }
            System.out.println(k + ": " + antworten[k - 1][3]);
        }

        System.out.print("? ");
        String eingabe = sc.next();
        if (!eingabe.matches("[1-4]")) {
            System.out.println("Ungültige Eingabe!");
        } else if (eingabe.equals(richtig)) {
            richtige++;
            System.out.println("Das ist richtig, ;-)");
        } else {
            falsche++;
            System.out.println("Leider falsch, :-(");
        }
    }
    System.out.println("");
    System.out.println("Der Quiz ist zu Ende, "+richtige+" von "+(richtige+falsche)+" richtig...");
    sc.close();
}

public static void sort2DArrayByColumn(String[][] array, final int column) {
    Arrays.sort(array, new Comparator<String[]>() {
        @Override
        public int compare(String[] row1, String[] row2) {
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

