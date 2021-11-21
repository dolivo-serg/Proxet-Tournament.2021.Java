package proxet.tournament.generator;

import proxet.tournament.generator.dto.Player;
import proxet.tournament.generator.dto.TeamGeneratorResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TeamGenerator {

    public TeamGeneratorResult generateTeams(String filePath) {
        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();
        List<String> waitingPlayers = new ArrayList<>();

        //читаем из файла и заполняем список с ожидающими игроками(в формате строки "время ожидания    Ник  тип")
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            while (reader.ready()){
                String[] line = reader.readLine().split("\t");
                waitingPlayers.add(String.format("%s\t%s\t%s", line[1], line[0], line[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Incorrect file name");
        }

        //сортируем список игроков по времени ожидания от большего к меньшему
        waitingPlayers.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int waitingTime1 = Integer.parseInt(o1.substring(0, o1.indexOf('\t')));
                int waitingTime2 = Integer.parseInt(o2.substring(0, o2.indexOf('\t')));
                return waitingTime2 - waitingTime1;
            }
        });

        //заполнение команд team1, team2
        int counterVehicleType1 = 0;
        int counterVehicleType2 = 0;
        int counterVehicleType3 = 0;

        for (String waitingPlayer : waitingPlayers) {
            String[] player = waitingPlayer.split("\t");

            if (player[2].equals("1") && counterVehicleType1 < 3) {
                team1.add(new Player(player[1], Integer.parseInt(player[2])));
                counterVehicleType1++;
            } else if (player[2].equals("1") && counterVehicleType1 < 6) {
                team2.add(new Player(player[1], Integer.parseInt(player[2])));
                counterVehicleType1++;
            }

            if (player[2].equals("2") && counterVehicleType2 < 3) {
                team1.add(new Player(player[1], Integer.parseInt(player[2])));
                counterVehicleType2++;
            } else if (player[2].equals("2") && counterVehicleType2 < 6) {
                team2.add(new Player(player[1], Integer.parseInt(player[2])));
                counterVehicleType2++;
            }

            if (player[2].equals("3") && counterVehicleType3 < 3) {
                team1.add(new Player(player[1], Integer.parseInt(player[2])));
                counterVehicleType3++;
            } else if (player[2].equals("3") && counterVehicleType3 < 6) {
                team2.add(new Player(player[1], Integer.parseInt(player[2])));
                counterVehicleType3++;
            }

            if (counterVehicleType1 == 6 && counterVehicleType2 == 6 && counterVehicleType3 == 6) {
                break;
            }
        }

        //если в файле было недостаточно игроков для создания команд бросаем исключение
        if (team1.size() != 9 && team2.size() != 9){
            try {
                throw new NotEnoughPlayersException();
            } catch (NotEnoughPlayersException e) {
                System.out.println("There are not enough players of the right type in the queue to create two teams");
            }
        }

        return new TeamGeneratorResult(team1, team2);
    }

    private class NotEnoughPlayersException extends Exception {
    }
}
