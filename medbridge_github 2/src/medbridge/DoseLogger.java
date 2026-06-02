package medbridge;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DoseLogger.java
 * Handles all reading and writing of the dose log file (dose_log.txt).
 * Each log entry is one line in the format:
 *   DATE | TIME | MEDICATION_NAME | PATIENT_NAME
 *
 */
public class DoseLogger {

    // ── Constants ────────────────────────────────────────────────────────
    private static final String LOG_FILE     = "dose_log.txt";
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("hh:mm a");

    // ── Log a dose ───────────────────────────────────────────────────────
    /**
     * Appends a timestamped dose entry to dose_log.txt.
     * Uses FileWriter in append mode (true) so existing entries are preserved.
     *
     * @param medName     the medication name being logged
     * @param patientName the patient's name
     */
    public static void logDose(String medName, String patientName) {
        String date = LocalDate.now().format(DATE_FMT);
        String time = LocalDateTime.now().format(TIME_FMT);
        String entry = date + " | " + time + " | " + medName + " | " + patientName;

        // append=true means existing log entries are NOT deleted
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(entry);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not write to dose log: " + e.getMessage());
        }
    }

    // ── Duplicate detection ──────────────────────────────────────────────
    /**
     * Checks whether a medication has already been logged today.
     * Reads dose_log.txt line by line and looks for an entry containing
     * both today's date AND the medication name.
     *
     * @param medName the medication name to check
     * @return true if already logged today, false otherwise
     */
    public static boolean isAlreadyLoggedToday(String medName) {
        String today = LocalDate.now().format(DATE_FMT);

        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Each line: "yyyy-MM-dd | hh:mm AM | medName | patient"
                if (line.contains(today) && line.contains(medName)) {
                    return true; // duplicate found
                }
            }
        } catch (FileNotFoundException e) {
            // Log file doesn't exist yet — that's fine, no doses logged
            return false;
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not read dose log: " + e.getMessage());
        }
        return false;
    }

    // ── Print today's log ────────────────────────────────────────────────
    /**
     * Reads and prints all dose entries from today.
     * Used by the "View today's log" menu option.
     */
    public static void printTodayLog() {
        String today = LocalDate.now().format(DATE_FMT);
        boolean found = false;

        System.out.println();
        System.out.println("  TODAY'S DOSE LOG (" + today + "):");
        System.out.println("  " + "-".repeat(50));

        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(today)) {
                    System.out.println("  " + line);
                    found = true;
                }
            }
        } catch (FileNotFoundException e) {
            // no log yet
        } catch (IOException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }

        if (!found) {
            System.out.println("  No doses logged yet today.");
        }
        System.out.println("  " + "-".repeat(50));
    }
}
