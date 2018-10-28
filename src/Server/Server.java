package Server;

import java.net.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class Server {
    public static void main(String[] args){

        Gson gson = new Gson();
        String json = "";

        InputOutput inputOutput = new InputOutput();

        String str = inputOutput.input();
        Type type = new TypeToken<CopyOnWriteArrayList<InjuredPoliceman>>() {
        }.getType();
        CopyOnWriteArrayList<InjuredPoliceman> injuredPolicemen = gson.fromJson(str, type);

        if (injuredPolicemen == null)
            injuredPolicemen = new CopyOnWriteArrayList<InjuredPoliceman>();


        /*injuredPolicemen.add(new InjuredPoliceman());
        injuredPolicemen.get(0).bodyPartsEnum.add(BodyPartEnum.LEFT_FEET);
        injuredPolicemen.get(0).injuredFacePart = FacePartEnum.RIGHT_EYE;
        injuredPolicemen.get(0).stateEnum = StateEnum.NAUSEA;
        injuredPolicemen.get(0).name = "Вася";
        injuredPolicemen.get(0).yearOfBirth = 1966;
        injuredPolicemen.get(0).location = "У двери";*/
        //injuredPolicemen.add(new InjuredPoliceman());//FacePartEnum.CHEEKS, BodyPartEnum.LEFT_HAND));
        //injuredPolicemen.add(new InjuredPoliceman());//FacePartEnum.NOSE, BodyPartEnum.RIGHT_HAND));
        //injuredPolicemen.add(new InjuredPoliceman());//FacePartEnum.LEFT_EYE, BodyPartEnum.RIGHT_LEG));

        Collections.sort(injuredPolicemen, new InjuredPolicemanComparator());

        int port = 1234;
        try {
            ServerSocket ss = new ServerSocket(port);
            Socket socket = ss.accept();

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            DataInputStream in = new DataInputStream(inputStream);
            DataOutputStream out = new DataOutputStream(outputStream);

            String message;
            String error = "Неверная команда";

            while (true) {
                String line = null;
                line = in.readUTF();//в лайне лежит строка, полученная от клиента
                String[] fewWords = line.split(" ");
                int number = 0;
                switch (fewWords[0]) {

                    case "remove_lower":
                        number = Integer.parseInt(fewWords[1]);
                        for (int i = 0; i < number; i++)
                            injuredPolicemen.remove(0);
                        break;

                    case "remove":
                        number = Integer.parseInt(fewWords[1]);
                        if (number != 0)
                            for (int j = 0; j < injuredPolicemen.size(); j++) {
                                if (injuredPolicemen.get(j).id == number) {
                                    injuredPolicemen.remove(j);
                                }
                            }
                        break;

                    case "clear":
                        injuredPolicemen = new CopyOnWriteArrayList<InjuredPoliceman>();
                        break;

                    case "get_collection"://out.writeUTF(line)
                        json = gson.toJson(injuredPolicemen);
                        System.out.println(json);
                        out.writeUTF(json);
                        break;

                    case "select":
                        final AtomicReference<FacePartEnum> selectedFacePart = new AtomicReference<>(); //первая часть работующего стрим апи
                        switch (fewWords[1]) {
                            case "FOREHEAD":
                                selectedFacePart.set(FacePartEnum.FOREHEAD);
                                break;
                            case "NOSE":
                                selectedFacePart.set(FacePartEnum.NOSE);
                                break;
                            case "RIGHT_EYE":
                                selectedFacePart.set(FacePartEnum.RIGHT_EYE);
                                break;
                            case "LEFT_EYE":
                                selectedFacePart.set(FacePartEnum.LEFT_EYE);
                                break;
                            case "MOUTH":
                                selectedFacePart.set(FacePartEnum.MOUTH);
                                break;
                            case "CHEEKS":
                                selectedFacePart.set(FacePartEnum.CHEEKS);
                                break;
                        }
                        List<InjuredPoliceman> policemenList = injuredPolicemen.stream().filter(o ->
                        {return o.injuredFacePart == selectedFacePart.get();}
                        ).collect(Collectors.toList());
                        if (policemenList.size() != 0) {
                            json = gson.toJson(policemenList);
                        }
                        else {
                            json = "Полицейских с такой раненой частью лица нет";
                        }
                        out.writeUTF(json);
                        break;

                    case "add":
                        InjuredPoliceman injuredPoliceman = new InjuredPoliceman();
                        injuredPoliceman.name = fewWords[1];
                        injuredPoliceman.yearOfBirth = Integer.parseInt(fewWords[2]);
                        injuredPoliceman.location = fewWords[3];
                        fewWords[4] = fewWords[4].toUpperCase();
                        switch (fewWords[4]) {
                            case "NAUSEA":
                                injuredPoliceman.stateEnum = StateEnum.NAUSEA;
                                break;
                            case "SMOOTH":
                                injuredPoliceman.stateEnum = StateEnum.SMOOTH;
                                break;
                            case "GOOD":
                                injuredPoliceman.stateEnum = StateEnum.GOOD;
                                break;
                            case "BAD":
                                injuredPoliceman.stateEnum = StateEnum.BAD;
                                break;
                            default:
                                out.writeUTF(error);
                                break;
                        }
                        injuredPolicemen.add(injuredPoliceman);
                        fewWords[5] = fewWords[5].toUpperCase();
                        switch (fewWords[5]) {
                            case "FOREHEAD":
                                injuredPoliceman.injuredFacePart = FacePartEnum.FOREHEAD;
                                break;
                            case "NOSE":
                                injuredPoliceman.injuredFacePart = FacePartEnum.NOSE;
                                break;
                            case "RIGHT_EYE":
                                injuredPoliceman.injuredFacePart = FacePartEnum.RIGHT_EYE;
                                break;
                            case "LEFT_EYE":
                                injuredPoliceman.injuredFacePart = FacePartEnum.LEFT_EYE;
                                break;
                            case "MOUTH":
                                injuredPoliceman.injuredFacePart = FacePartEnum.MOUTH;
                                break;
                            case "CHEEKS":
                                injuredPoliceman.injuredFacePart = FacePartEnum.CHEEKS;
                                break;
                            case "":
                                break;
                            /*default:
                                System.out.println("Неправильная команда");
                                break;*/// TODO: 12-Sep-18 и другие дефолты в кейсах
                        }
                        fewWords[6] = fewWords[6].toUpperCase();
                        switch (fewWords[6]) {
                            case "LEFT_HAND":
                                injuredPoliceman.bodyPartsEnum.add(BodyPartEnum.LEFT_HAND);
                                break;
                            case "RIGHT_HAND":
                                injuredPoliceman.bodyPartsEnum.add(BodyPartEnum.RIGHT_HAND);
                                break;
                            case "LEFT_LEG":
                                injuredPoliceman.bodyPartsEnum.add(BodyPartEnum.LEFT_LEG);
                                break;
                            case "RIGHT_LEG":
                                injuredPoliceman.bodyPartsEnum.add(BodyPartEnum.RIGHT_LEG);
                                break;
                            case "HEAD":
                                injuredPoliceman.bodyPartsEnum.add(BodyPartEnum.HEAD);
                                break;
                            case "RIGHT_FEET":
                                injuredPoliceman.bodyPartsEnum.add(BodyPartEnum.RIGHT_FEET);
                                break;
                            case "LEFT_FEET":
                                injuredPoliceman.bodyPartsEnum.add(BodyPartEnum.LEFT_FEET);
                                break;
                            /*default:
                                System.out.println("Неправильная команда");
                                break;*/
                        }
                        break;
                    /*case "exit":
                        System.out.println("in server");
                        String json = "";
                        json = gson.toJson(injuredPolicemen);
                        inputOutput.output(json);
                        System.exit(0);
                        break;*/
                    default:
                        System.out.println("Неправильная команда");//todo как-то реализовать "неправильная команда" мб всегда отправлять строичкуи выводить ее только если не нулл
                        break;
                }
                out.flush();
                //out.close();todo куда-то засунуть close
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        public static void 
    }
}
