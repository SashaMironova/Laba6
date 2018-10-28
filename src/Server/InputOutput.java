package Server;

import java.io.*;

public class InputOutput {//только для сервера


    public String input() {
        String contents = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getenv("InjuredPolicemen")));
            contents = reader.readLine();
        }
        catch (Exception e){
            System.out.println(e);
        }
        System.out.println(contents);
        return contents;
    }

    public void output(String str){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getenv("InjuredPolicemen"), false)))
        {writer.write(str);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}