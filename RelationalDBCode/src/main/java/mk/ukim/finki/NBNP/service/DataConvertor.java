package mk.ukim.finki.NBNP.service;

public class DataConvertor {

    public static String convert (String[] row){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<row.length-1; i++){
            row[i] = row[i].replace("\"", "");
            sb.append(row[i]);
        }
        return sb.toString();
    }

}
