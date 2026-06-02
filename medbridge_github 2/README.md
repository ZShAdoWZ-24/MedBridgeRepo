# MedBridge — Prescription Tracking & Medication Reminder App

> AP Computer Science A Entrepreneurship Project

---

## What it does

MedBridge is a **console-based Java application** that helps elderly patients manage complex medication schedules. It solves two critical real-world problems:

1. **P7 — Dose confusion**: Patients cannot remember whether they already took a dose. MedBridge logs every dose with a timestamp and warns the user if they try to log the same medication twice in one day — preventing dangerous double-dosing.

2. **P8 — Prescription runout**: Patients run out of medication before their refill. MedBridge tracks pill counts, decrements them with every logged dose, and warns the patient 7 days out (standard) and 3 days out (urgent) so they never go without medication.

---

## How to compile and run

```bash
# 1. Compile all source files
javac -d out src/medbridge/*.java

# 2. Run the app
java -cp out medbridge.MedBridgeApp
```

On first run, `medications.txt` is automatically created with sample data.  
Edit `medications.txt` to match real prescriptions before use.

---

## Project structure

```
medbridge/
├── README.md
├── medications.txt         ← auto-created on first run; edit with real meds
├── dose_log.txt            ← auto-created; stores all dose entries
└── src/
    └── medbridge/
        ├── MedBridgeApp.java       ← main entry point; menu loop
        ├── Medication.java         ← data class: name, time, pillCount, dosesPerDay
        ├── MedicationManager.java  ← loads/saves ArrayList; refill checks
        └── DoseLogger.java         ← logs doses to file; duplicate detection
```

---

## AP CSA concepts demonstrated

| Concept | Where used |
|---------|-----------|
| Classes & encapsulation | `Medication.java` — private fields, getters, setters |
| `ArrayList<E>` | `MedicationManager.loadMedications()` — stores medication list |
| File I/O | `FileWriter` (append + overwrite), `BufferedReader`, `FileReader` |
| Loops | `while` main loop, `for-each` over ArrayList |
| Conditionals | `if/else-if` for thresholds, duplicate check, menu routing |
| Static methods | `DoseLogger`, `MedicationManager` — utility class pattern |
| `String` methods | `split()`, `contains()`, `trim()`, `parseInt()` |
| `LocalDate` / `LocalDateTime` | Timestamping dose log entries |
| Exception handling | `try/catch` around all file I/O |
| `toString()` override | `Medication.toString()` for debugging |

---

## medications.txt format

One medication per line:

```
# Comments start with #
# Format: name|time|dosesPerDay|pillCount
Metformin 500mg|8:00 AM|2|30
Lisinopril 10mg|8:00 AM|1|5
Atorvastatin 20mg|9:00 AM|1|25
Aspirin 81mg|9:00 AM|1|60
```

---

## dose_log.txt format

Auto-generated. One entry per logged dose:

```
2025-06-02 | 08:07 AM | Metformin 500mg | Dorothy M.
2025-06-02 | 09:02 AM | Atorvastatin 20mg | Dorothy M.
```

---

## Real-world impact

- **125,000** preventable U.S. deaths annually from medication non-adherence (CDC MMWR, 2017)
- **$100–$300 billion** in avoidable healthcare costs per year
- **No free, offline, PC-based** medication tracker currently exists for low-tech elderly patients
- MedBridge runs on **any computer with Java** — no internet, no smartphone, no subscription

---

## References

Neiman, A. B., et al. (2017). CDC grand rounds: Improving medication adherence. *MMWR, 66*(45), 1248–1251. https://doi.org/10.15585/mmwr.mm6645a2

Centers for Disease Control and Prevention. (2024). *Pharmacy-based interventions to improve medication adherence.* https://www.cdc.gov/cardiovascular-resources/php/medication-adherence/index.html
