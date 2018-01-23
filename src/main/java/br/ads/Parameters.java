package br.ads;

import br.ads.utils.AdsUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parameters {

    public static int context = 1;
    public static long randomSeed = 2015L;
    public static double attentionDistance = 3;
    public static double attentionLevel = 0.5;
    public static int cyclesPerSecond = 30;
    public static int totalVehicles = 140;
    public static int maxSpeed = 30;
    public static double maxSpeedObstacle = 7d;
    public static double entranceDistance = 2.5d;

    public enum Type {

        CYCLES_PER_SECOND("Cycles per Second"),
        TOTAL_VEHICLES("Total Vehicles"),
        MAX_SPEED("Max Speed"),
        ATTENTION_LEVEL("Attention Level"),
        ATTENTION_DISTANCE("Attention Distance"),
        RANDOM_SEED("Seed"),
        CONTEXT("Context"),
        ENTRANCE_DISTANCE("Entrance Distance"),
        MAX_SPEED_OBSTACLE("Max Speed Obstacle");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static List<Type> orderedList() {
            ArrayList<Type> list = new ArrayList<Type>();
            list.add(MAX_SPEED);
            list.add(TOTAL_VEHICLES);
            list.add(RANDOM_SEED);
            list.add(CONTEXT);
            list.add(ATTENTION_DISTANCE);
            list.add(ATTENTION_LEVEL);
            list.add(CYCLES_PER_SECOND);
            list.add(MAX_SPEED_OBSTACLE);
            list.add(ENTRANCE_DISTANCE);
            return list;
        }

    }

    public static HashMap<Type, String> getListParameters() {
        HashMap<Type, String> map = new HashMap<>();

        // primary
        map.put(Type.MAX_SPEED, String.valueOf(maxSpeed));
        map.put(Type.TOTAL_VEHICLES, String.valueOf(totalVehicles));
        map.put(Type.RANDOM_SEED, String.valueOf(randomSeed));
        map.put(Type.CONTEXT, String.valueOf(context));

        // secondary
        map.put(Type.ATTENTION_DISTANCE, String.valueOf(attentionDistance));
        map.put(Type.ATTENTION_LEVEL, String.valueOf(attentionLevel));
        map.put(Type.CYCLES_PER_SECOND, String.valueOf(cyclesPerSecond));
        map.put(Type.MAX_SPEED_OBSTACLE, String.valueOf(maxSpeedObstacle));
        map.put(Type.ENTRANCE_DISTANCE, String.valueOf(entranceDistance));

        return map;
    }

    public static boolean putListParameters(HashMap<Type, String> values) {
        for (Type tp : values.keySet()) {
            String valor = values.get(tp);
            try {
                switch (tp) {
                    case CYCLES_PER_SECOND:
                        cyclesPerSecond = Integer.parseInt(valor);
                        break;

                    case ATTENTION_DISTANCE:
                        attentionDistance = Double.parseDouble(valor);
                        break;

                    case ATTENTION_LEVEL:
                        attentionLevel = Double.parseDouble(valor);
                        break;

                    case MAX_SPEED:
                        maxSpeed = Integer.parseInt(valor);
                        break;

                    case TOTAL_VEHICLES:
                        totalVehicles = Integer.parseInt(valor);
                        break;

                    case RANDOM_SEED:
                        randomSeed = Long.parseLong(valor);
                        AdsUtils.setRandomSeed(randomSeed);
                        break;

                    case CONTEXT:
                        context = Integer.parseInt(valor);
                        break;

                    case MAX_SPEED_OBSTACLE:
                        maxSpeedObstacle = Double.parseDouble(valor);
                        break;

                    case ENTRANCE_DISTANCE:
                        entranceDistance = Double.parseDouble(valor);
                        break;
                }
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }

}
