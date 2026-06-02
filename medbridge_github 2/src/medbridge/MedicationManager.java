package medbridge;

import java.io.*;
import java.util.ArrayList;

/**
 * MedicationManager.java
 * Loads, saves, and manages the ArrayList of Medication objects.
 * Reads/writes medications.txt in pipe-delimited format.
 *
 * AP CSA concepts: ArrayList<E>, file I/O, String.split(),
 *                  Integer.parseInt(), loops
 */
public class MedicationManager {

    // ── Constants ────────────────────────────────────────────────────────
    private static final String MED_FILE = "medications.txt";

    // ── Load medications ─────────────────────────────────────────────────
    /**
     * Reads medications.txt and returns an ArrayList of Medication objects.
     * Each line format: name|time|dosesPerDay|pillCount
     * If the file does not exist, creates a sample file and loads it.
     *
     * @return ArrayList<Medication> populated from file
     */
    public static ArrayList<Medication> loadMedications() {
        ArrayList<Medication> meds = new ArrayList<>();
        File file = new File(MED_FILE);

        // First run: create a sample medications file
        if (!file.exists()) {
            createSampleFile();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(MED_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; // skip comments/blanks
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String name        = parts[0].trim();
                    String time        = parts[1].trim();
                    int    dosesPerDay = Integer.parseInt(parts[2].trim());
                    int    pillCount   = Integer.parseInt(parts[3].trim());
                    meds.add(new Medication(name, time, dosesPerDay, pillCount));
                }
            }
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not load medications: " + e.getMessage());
        }
        return meds;
    }

    // ── Save medications ─────────────────────────────────────────────────
    /**
     * Writes the current ArrayList back to medications.txt.
     * Called after every pill count decrement so data persists.
     *
     * @param meds the current ArrayList to save
     */
    public static void saveMedications(ArrayList<Medication> meds) {
        try (FileWriter fw = new FileWriter(MED_FILE, false); // overwrite mode
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("# MedBridge medication file — one medication per line");
            bw.newLine();
            bw.write("# Format: name|time|dosesPerDay|pillCount");
            bw.newLine();
            for (Medication med : meds) {
                bw.write(med.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not save medications: " + e.getMessage());
        }
    }

    // ── Refill check ─────────────────────────────────────────────────────
    /**
     * Scans the ArrayList and prints a warning for any medication
     * running low. Two thresholds:
     *   < 3 days  → URGENT alert
     *   < 7 days  → standard reminder
     *
     * @param meds the medication list to check
     * @return true if any warnings were printed
     */
    public static boolean checkRefills(ArrayList<Medication> meds) {
        boolean warned = false;
        for (Medication med : meds) {
            int days = med.daysRemaining();
            if (days < 3) {
                System.out.println();
                System.out.println("  !! URGENT: REFILL IMMEDIATELY !!");
                System.out.println("  " + med.getName() + " — only " + days + " day(s) remaining.");
                System.out.println("  Call your pharmacy TODAY.");
                warned = true;
            } else if (days < 7) {
                System.out.println();
                System.out.println("  \u26A0  REFILL REMINDER:");
                System.out.println("  " + med.getName() + " — approx. " + days + " day(s) remaining.");
                System.out.println("  Please schedule a refill this week.");
                warned = true;
            }
        }
        return warned;
    }

    // ── Update pill count ────────────────────────────────────────────────
    /**
     * Sets a new pill count for a medication by index.
     * Called after a patient picks up a pharmacy refill.
     *
     * @param meds     the medication list
     * @param index    0-based index of the medication to update
     * @param newCount new pill count to set
     */
    public static void updatePillCount(ArrayList<Medication> meds, int index, int newCount) {
        meds.get(index).setPillCount(newCount);
        saveMedications(meds);
    }

    // ── Sample file creator ──────────────────────────────────────────────
    /**
     * Creates a default medications.txt on first run.
     * Patient/caregiver should edit this file to match real prescriptions.
     */
    private static void createSampleFile() {
        try (FileWriter fw = new FileWriter(MED_FILE);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("# MedBridge medication file — one medication per line");
            bw.newLine();
            bw.write("# Format: name|time|dosesPerDay|pillCount");
            bw.newLine();
            bw.write("# Edit this file to match your real prescriptions");
            bw.newLine();
            bw.write("Metformin 500mg|8:00 AM|2|30");
            bw.newLine();
            bw.write("Lisinopril 10mg|8:00 AM|1|5");   // intentionally low to demo warning
            bw.newLine();
            bw.write("Atorvastatin 20mg|9:00 AM|1|25");
            bw.newLine();
            bw.write("Aspirin 81mg|9:00 AM|1|60");
            bw.newLine();
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not create sample file: " + e.getMessage());
        }
    }
}
