package my_project;

import java.util.Scanner;

class Specializations {
    static final String KNEE_STR = "knee";
    static final String ELBOW_STR = "elbow";
    static final String HIP_STR = "hip";

    private boolean knee;
    private boolean elbow;
    private boolean hip;
    private boolean initialized;

    boolean isKnee() {
        return knee;
    }

    boolean isElbow() {
        return elbow;
    }

    boolean isHip() {
        return hip;
    }

    boolean areInitialized() {
        return initialized;
    }

    Specializations(String specializationsForParsing) {
        String knee = "knee";
        String elbow = "elbow";
        String hip = "hip";
        String regex = knee + "|" + elbow + "|" + hip;
        Scanner scanner = new Scanner(specializationsForParsing);
        if (scanner.nextLine().matches(hip + "\\s" + elbow + "|" + elbow + "\\s" + hip)) {
            this.elbow = true;
            this.hip = true;
            this.knee = false;
            this.initialized = true;
        } else if (scanner.nextLine().matches(hip + "\\s" + knee + "|" + knee + "\\s" + hip)) {
            this.hip = true;
            this.knee = true;
            this.elbow = false;
            this.initialized = true;
        } else if (scanner.nextLine().matches(elbow + "\\s" + knee + "|" + knee + "\\s" + elbow)) {
            this.knee = true;
            this.elbow = true;
            this.hip = false;
            this.initialized = true;
        } else {
            this.initialized = false;
            System.out.println("Wrong parameters");
        }

    }
}
