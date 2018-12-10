import java.io.*;

public class Converter {

    public static void main(String[] args) {
        try{
            File f = new File("C:\\Users\\Daniel\\Repositories\\Gliss\\maps\\osu.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br=new BufferedReader(fr);

            FileWriter fw = new FileWriter("notes2.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            String s = br.readLine();
            while(!( s == null || s.isEmpty())){
                String[] str = s.split(",");
                int xPos = Integer.parseInt(str[0]);

                int timePos = Integer.parseInt(str[2]);
                timePos = timePos * 1000;
                xPos = (int) Math.round(1280 * (xPos/ 512.0));

                bw.write("N," + timePos + "," + xPos + ",100");
                bw.newLine();

                s = br.readLine();
            }
            br.close();
            fr.close();
            bw.close();
        } catch (FileNotFoundException e){
            System.out.print("FUCK");
        } catch (IOException e){
            System.out.print("NYET");
        }
    }

}

