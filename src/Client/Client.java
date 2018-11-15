package Client;

import java.lang.reflect.Type;
import java.net.*;
import java.io.*;
import java.util.*;

import Server.InjuredPoliceman;
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
            DataOutputStream out = new DataOutputStream(outputStream);//todo если коллекция пуста, вызвана команда гет коллекшн, что происходит

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String enterStr = null;


            Gson gson = new Gson();
            String str; // переменная для хранения того, что прислал сервер

            Type type = new TypeToken<ArrayList<InjuredPoliceman>>() {//todo сделать обработку временной недоступности сервера
            }.getType();

            while (true) {
                enterStr = reader.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                if (enterStr.equals("exit")){
                    out.close();
                    in.close();
                    socket.close();
                    reader.close();
                    System.exit(0);
                    break;
                }

                out.writeUTF(enterStr);
                str = in.readUTF();
                if (str.equals("OK") || str.equals("Полицейских с такой раненой частью лица нет") || str.equals("Неправильная команда")) {
                    System.out.println(str);
                }
                else{
                    ArrayList<InjuredPoliceman> injuredPolicemen = gson.fromJson(str, type);
                    for (int i = 0; i < injuredPolicemen.size(); i++) {
                        System.out.println("Раненого полицейского зовут: " + injuredPolicemen.get(i).name + ", он родился в " + injuredPolicemen.get(i).yearOfBirth
                                + " году, он находится " + injuredPolicemen.get(i).location + ", его создали " + injuredPolicemen.get(i).dateOfCreation);
                    }
                }
                out.flush(); // заставляем поток закончить передачу данных.
            }
        } catch (Exception x) {
            System.out.println("Сервер недоступен");
        }
    }
}


// TODO: Коллекцию из ЛР №5 заменить на ее потокобезопасный аналог. +
//Операции обработки объектов коллекции должны быть реализованы с помощью Stream API с использованием лямбда-выражений.+
//Объекты между клиентом и сервером должны передаваться в сериализованном виде.+
//Объекты в коллекции, передаваемой клиенту, должны быть отсортированы по названию.+
//Получив запрос, сервер должен создавать отдельный поток, который должен формировать и отправлять ответ клиенту.+
//Клиент должен корректно обрабатывать временную недоступность сервера.
//Обмен данными между клиентом и сервером должен осуществляться по протоколу TCP.+
//На стороне сервера должен использоваться сетевой канал а на стороне клиента - потоки ввода-вывода.+