package Oops;

/**
 * COMPOSITION DEMO
 * ----------------
 * Definition: Constructing complex classes by combining instance references of other classes ('has-a' relationship)
 * rather than inheriting from a base class ('is-a' relationship).
 *
 * Utilities & Benefits:
 * - Dynamic Behavior: Components can be swapped at runtime (Strategy Pattern).
 * - Avoids Inheritance Rigidity: Solves the fragile base class problem and prevents class explosion.
 * - Preferred Principle: "Favor composition over inheritance".
 */
public class CompositionDemo {

    public static class Engine {
        private final String type;

        public Engine(String type) {
            this.type = type;
        }

        public void start() {
            System.out.println(type + " engine ignited and running.");
        }
    }

    public static class GpsNavigator {
        public void navigateTo(String destination) {
            System.out.println("Calculating optimal route to: " + destination);
        }
    }

    public static class AudioSystem {
        public void playMusic(String track) {
            System.out.println("Playing track: " + track);
        }
    }

    // SmartCar HAS-AN Engine, HAS-A GpsNavigator, and HAS-AN AudioSystem
    public static class SmartCar {
        private final Engine engine;
        private final GpsNavigator gps;
        private final AudioSystem audio;

        public SmartCar(Engine engine, GpsNavigator gps, AudioSystem audio) {
            this.engine = engine;
            this.gps = gps;
            this.audio = audio;
        }

        public void driveTo(String destination) {
            engine.start();
            gps.navigateTo(destination);
            audio.playMusic("RoadTrip Playlist");
            System.out.println("SmartCar autonomously driving towards destination...");
        }
    }

    public static void runDemo() {
        System.out.println("=========================================");
        System.out.println("          5. COMPOSITION DEMO            ");
        System.out.println("=========================================");

        Engine v8Engine = new Engine("V8 Dual-Motor Electric");
        GpsNavigator gps = new GpsNavigator();
        AudioSystem audio = new AudioSystem();

        SmartCar car = new SmartCar(v8Engine, gps, audio);
        car.driveTo("Silicon Valley HQ");
    }

    public static void main(String[] args) {
        runDemo();
    }
}
