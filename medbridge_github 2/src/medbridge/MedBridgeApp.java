package medbridge;

import java.util.ArrayList;
import java.util.Scanner;


public class MedBridgeApp {

    // ── Patient name (set at startup) ────────────────────────────────────
    private static String patientName = "Patient";

    // ── Main ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Welcome and patient name entry
        printBanner();
        System.out.print("  Enter patient name (or press ENTER for 'Patient'): ");
        String input = scanner.nextLine().trim();
        if (!input.isEmpty()) {
            patientName = input;
        }

        // Load medication list from file into ArrayList
        ArrayList<Medication> meds = MedicationManager.loadMedications();

        if (meds.isEmpty()) {
            System.out.println("\n  No medications found in medications.txt.");
            System.out.println("  Please add medications to the file and restart.");
            scanner.close();
            return;
        }

        // Main application loop
        boolean running = true;
        while (running) {
            System.out.println();
            printDivider();

            // Check refills on every loop iteration
            boolean hadWarnings = MedicationManager.checkRefills(meds);
            if (hadWarnings) printDivider();

            // Display today's medication menu
            printMedicationMenu(meds);

            // Read user input
            System.out.print("  Enter number: ");
            int choice = readInt(scanner);

            // Handle choice
            if (choice == 0) {
                running = false;

            } else if (choice >= 1 && choice <= meds.size()) {
                // Dose logging — subtract 1 for 0-based ArrayList index
                handleDoseLog(meds, choice - 1, scanner);

            } else if (choice == meds.size() + 1) {
                // View today's log
                DoseLogger.printTodayLog();
                pause(scanner);

            } else if (choice == meds.size() + 2) {
                // Update pill count (after pharmacy refill)
                handlePillCountUpdate(meds, scanner);

            } else {
                System.out.println("  Invalid selection. Please try again.");
            }
        }

        System.out.println();
        System.out.println("  Goodbye, " + patientName + ". Stay healthy!");
        System.out.println();
        scanner.close();
    }

    // ── Display helpers ──────────────────────────────────────────────────

    /** Prints the MedBridge welcome banner. */
    private static void printBanner() {
        System.out.println();
        System.out.println("  =================================================");
        System.out.println("     M E D B R I D G E");
        System.out.println("     Prescription Tracking & Medication Reminder");
        System.out.println("  =================================================");
        System.out.println();
    }

    /** Prints a horizontal divider line. */
    private static void printDivider() {
        System.out.println("  " + "-".repeat(50));
    }

    /**
     * Prints the numbered medication menu.
     * Marks each medication as TAKEN or PENDING based on today's log.
     * Also shows refill warning inline if supply is low.
     *
     * @param meds the current medication ArrayList
     */
    private static void printMedicationMenu(ArrayList<Medication> meds) {
        System.out.println("  TODAY'S MEDICATIONS — " + patientName);
        System.out.println();

        for (int i = 0; i < meds.size(); i++) {
            Medication med = meds.get(i);
            String status = DoseLogger.isAlreadyLoggedToday(med.getName())
                    ? "[  TAKEN  ] \u2713"
                    : "[ PENDING ]  ";
            String refillTag = "";
            int days = med.daysRemaining();
            if      (days < 3) refillTag = "  !! " + days + " days left !!";
            else if (days < 7) refillTag = "  \u26A0 " + days + " days left";

            System.out.printf("  [%d]  %-22s  %s  %s%s%n",
                    i + 1, med.getName(), med.getTime(), status, refillTag);
        }

        System.out.println();
        System.out.printf("  [%d]  View today's dose log%n", meds.size() + 1);
        System.out.printf("  [%d]  Update pill count (after refill pickup)%n", meds.size() + 2);
        System.out.println("  [0]  Exit");
        System.out.println();
    }

    // ── Dose logging ──────────────────────────────────────────────────────

    /**
     * Handles a dose log request for one medication.
     * Checks for duplicates first. If none, logs the dose,
     * decrements pill count, saves to file, and checks refills.
     *
     * @param meds    the medication ArrayList
     * @param index   0-based index of selected medication
     * @param scanner active Scanner for input
     */
    private static void handleDoseLog(ArrayList<Medication> meds, int index, Scanner scanner) {
        Medication med = meds.get(index);

        if (DoseLogger.isAlreadyLoggedToday(med.getName())) {
            // ── Duplicate dose warning ───────────────────────────────────
            System.out.println();
            System.out.println("  !! WARNING !!");
            System.out.println("  " + med.getName() + " was already logged today.");
            System.out.println("  Please do NOT take a second dose.");
            System.out.println("  Contact your doctor if you are unsure.");
        } else {
            // ── Log the dose ────────────────────────────────────────────
            DoseLogger.logDose(med.getName(), patientName);

            // Decrement pill count and persist to file
            int newCount = med.getPillCount() - 1;
            if (newCount < 0) newCount = 0; // safety floor
            med.setPillCount(newCount);
            MedicationManager.saveMedications(meds);

            System.out.println();
            System.out.println("  \u2713 DOSE LOGGED SUCCESSFULLY");
            System.out.println("  " + med.getName() + " recorded for " + patientName + ".");

            // Immediate refill check after logging
            int days = med.daysRemaining();
            if (days < 3) {
                System.out.println("  !! Only " + days + " day(s) remaining — refill immediately!");
            } else if (days < 7) {
                System.out.println("  \u26A0  " + days + " day(s) remaining — consider refilling soon.");
            }
        }
        pause(scanner);
    }

    // ── Pill count update ─────────────────────────────────────────────────

    /**
     * Prompts the user to select a medication and enter a new pill count.
     * Used after picking up a pharmacy refill.
     *
     * @param meds    the medication ArrayList
     * @param scanner active Scanner
     */
    private static void handlePillCountUpdate(ArrayList<Medication> meds, Scanner scanner) {
        System.out.println();
        System.out.println("  UPDATE PILL COUNT — select a medication:");
        for (int i = 0; i < meds.size(); i++) {
            System.out.printf("  [%d]  %s  (currently %d pills)%n",
                    i + 1, meds.get(i).getName(), meds.get(i).getPillCount());
        }
        System.out.println("  [0]  Cancel");
        System.out.print("  Enter number: ");
        int choice = readInt(scanner);

        if (choice >= 1 && choice <= meds.size()) {
            Medication med = meds.get(choice - 1);
            System.out.print("  Enter new pill count for " + med.getName() + ": ");
            int newCount = readInt(scanner);
            if (newCount >= 0) {
                MedicationManager.updatePillCount(meds, choice - 1, newCount);
                System.out.println("  \u2713 Pill count updated to " + newCount + ".");
            } else {
                System.out.println("  Invalid count. No changes made.");
            }
        }
        pause(scanner);
    }

    // ── Utility helpers ───────────────────────────────────────────────────

    /**
     * Safely reads an integer from Scanner.
     * Returns -1 if input is not a valid integer (prevents crash).
     *
     * @param scanner active Scanner
     * @return integer entered by user, or -1 on invalid input
     */
    private static int readInt(Scanner scanner) {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1; // invalid input — handled by caller
        }
    }

    /**
     * Pauses until the user presses ENTER.
     * Gives the user time to read output before the menu redraws.
     *
     * @param scanner active Scanner
     */
    private static void pause(Scanner scanner) {
        System.out.println();
        System.out.print("  Press ENTER to continue...");
        scanner.nextLine();
    }
}
