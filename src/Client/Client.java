package Client;

import java.lang.reflect.Type;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Server.InjuredPoliceman;
import Server.InputOutput;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Client {
    public static void main(String[] args) {
        int serverPort = 1234;
        String address = "127.0.0.1";
        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            Socket socket = new Socket(ipAddress, serverPort);
            System.out.println("Соединение установлено\nВведите одну из команд в следующих форматах:" +
                    "\nremove_lower номер_элемента, remove id_элемента, clear, add имя год_рождения местонахождение " +
                    "состояние раненая_часть_лица раненая_часть_тела, get_collection, select раненая_часть_лица, exit");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            DataInputStream in = new DataInputStream(inputStream);
            DataOutputStream out = new DataOutputStream(outputStream);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String enterStr = null;


            Gson gson = new Gson();
            String str; // переменная для хранения того, что прислал сервер

            Type type = new TypeToken<ArrayList<InjuredPoliceman>>() {
            }.getType();

            while (true) {
                enterStr = reader.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                if (enterStr.equals("exit")){
                    out.close();
                    break;
                }

                out.writeUTF(enterStr);
                str = in.readUTF();
                if((enterStr.equals("get_collection") || enterStr.equals("select NOSE") || enterStr.equals("select FOREHEAD") || enterStr.equals("select CHEEKS")
                || enterStr.equals("select MOUTH") || enterStr.equals("select LEFT_EYE") || enterStr.equals("select RIGHT_EYE")) == true){
                    if (str.equals("Полицейских с такой раненой частью лица нет")){
                        System.out.println(str);
                    }
                    else{
                        ArrayList<InjuredPoliceman> injuredPolicemen = gson.fromJson(str, type);
                        for (int i = 0; i < injuredPolicemen.size(); i++) {
                            System.out.println("Раненого полицейского зовут: " + injuredPolicemen.get(i).name + ", он родился в " + injuredPolicemen.get(i).yearOfBirth
                                    + " году, он находится " + injuredPolicemen.get(i).location + ", его создали " + injuredPolicemen.get(i).dateOfCreation);
                        }
                    }
                }

                /*else {
                    out.writeUTF(enterStr); //отсылаем введенную строку текста серверу.
                    str = in.readUTF(); // ждем пока сервер пришлет сюда строку текста.
                    if (isSelect(str)==true){//если верно, то выводим коллекцию с выборкой
                        input();//todo вывод коллекции с выборкой, подключить строчку ниже
                    }*/

                    //injuredPolicemen = gson.fromJson(str, type); //только в том случае, если у нас команда выборки эл-тов коллекции


                    //System.out.println(injuredPolicemen);// todo распарсить (сделала ли)
//todo отдельный джисон для коллекции, передаваемой клиенту
                    out.flush(); // заставляем поток закончить передачу данных.
                //}
            }
        } catch (Exception x) {
            System.out.println("пипец");//TODO дает эксепшн здесь, исправить
        }
    }

    public static String input() {
        String contents = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getenv("InjuredPolicemen")));//поменять переменную
            contents = reader.readLine();// окружения на путь у файлу к коллекции с выборкой
        }
        catch (Exception e){
            System.out.println(e);
        }
        System.out.println(contents);
        return contents;
    }


}


// TODO: Коллекцию из ЛР №5 заменить на ее потокобезопасный аналог. +
//Операции обработки объектов коллекции должны быть реализованы с помощью Stream API с использованием лямбда-выражений.+
//Объекты между клиентом и сервером должны передаваться в сериализованном виде.
//Объекты в коллекции, передаваемой клиенту, должны быть отсортированы по названию.
//Получив запрос, сервер должен создавать отдельный поток, который должен формировать и отправлять ответ клиенту.
//Клиент должен корректно обрабатывать временную недоступность сервера.
//Обмен данными между клиентом и сервером должен осуществляться по протоколу TCP.+
//На стороне сервера должен использоваться сетевой канал а на стороне клиента - потоки ввода-вывода.