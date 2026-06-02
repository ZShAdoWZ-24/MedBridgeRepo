package medbridge;

/**
 * Medication.java
 * Represents a single medication in a patient's regimen.
 * Stores all fields needed for dose tracking and refill warnings.
 *
 * AP CSA concepts: encapsulation, constructors, getters/setters, toString()
 */
public class Medication {

    // ── Fields ──────────────────────────────────────────────────────────
    private String name;        // e.g. "Metformin 500mg"
    private String time;        // scheduled dose time, e.g. "8:00 AM"
    private int    dosesPerDay; // how many times per day this is taken
    private int    pillCount;   // current number of pills remaining

    // ── Constructor ──────────────────────────────────────────────────────
    /**
     * Creates a Medication with all required fields.
     *
     * @param name        display name including dosage
     * @param time        scheduled time for this dose
     * @param dosesPerDay number of doses taken per day
     * @param pillCount   current pill supply remaining
     */
    public Medication(String name, String time, int dosesPerDay, int pillCount) {
        this.name        = name;
        this.time        = time;
        this.dosesPerDay = dosesPerDay;
        this.pillCount   = pillCount;
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public String getName()        { return name; }
    public String getTime()        { return time; }
    public int    getDosesPerDay() { return dosesPerDay; }
    public int    getPillCount()   { return pillCount; }

    // ── Setters ──────────────────────────────────────────────────────────
    public void setPillCount(int pillCount) { this.pillCount = pillCount; }

    /**
     * Calculates how many days of supply remain.
     * Uses integer division — always rounds down for safety.
     *
     * @return days of supply remaining
     */
    public int daysRemaining() {
        if (dosesPerDay == 0) return 999; // guard against divide-by-zero
        return pillCount / dosesPerDay;
    }

    /**
     * Returns a file-safe string for saving to medications.txt
     * Format: name|time|dosesPerDay|pillCount
     */
    public String toFileString() {
        return name + "|" + time + "|" + dosesPerDay + "|" + pillCount;
    }

    /**
     * Human-readable representation for debugging.
     */
    @Override
    public String toString() {
        return name + " @ " + time + " (" + pillCount + " pills left)";
    }
}
