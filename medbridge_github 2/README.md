
MedBridge — Prescription Tracking & Medication Reminder App

AP Computer Science A · Entrepreneurship Project · 2025–2026

Overview

MedBridge is a free, offline, console-based Java application that helps elderly patients manage their daily medication schedules safely and independently. It solves two documented problems: dose confusion, where patients cannot remember whether they already took a medication, and prescription runout, where patients run out of critical medications before their refill. No internet, no smartphone, no subscription. Runs on any computer with Java.

How to compile and run

javac -d out src/medbridge/*.java
java -cp out medbridge.MedBridgeApp

Project structure

src/medbridge/MedBridgeApp.java — main entry point, menu loop, user input
src/medbridge/Medication.java — data class: name, time, pillCount, dosesPerDay
src/medbridge/MedicationManager.java — loads and saves ArrayList, refill check logic
src/medbridge/DoseLogger.java — logs doses to file, duplicate detection
medications.txt — auto-created on first run, edit with real medications
dose_log.txt — auto-created, stores all timestamped dose entries
compile_and_run.sh — Mac/Linux one-click compile and run
compile_and_run.bat — Windows one-click compile and run

 Concepts demonstrated

Classes and encapsulation: Medication.java uses private fields, getters, setters, and toString()
ArrayList: MedicationManager loads and iterates the medication list using ArrayList of Medication
File I/O: FileWriter in append and overwrite modes, BufferedReader, FileReader
Loops: while loop for the main menu, for-each loop over the ArrayList
Conditionals: if and else-if for refill thresholds, duplicate detection, and menu routing
Static methods: DoseLogger and MedicationManager use the utility class pattern
String methods: split(), contains(), trim(), Integer.parseInt()
LocalDate and LocalDateTime: timestamps every dose log entry
Exception handling: try and catch around all file I/O operations
Method decomposition: each feature is its own clearly named method with a Javadoc comment

medications.txt format

One medication per line. Lines starting with # are comments and are ignored.
Format: name|time|dosesPerDay|pillCount
Example: Metformin 500mg|8:00 AM|2|30

dose_log.txt format

One line per logged dose, auto-generated. Safe to print and bring to a doctor appointment.
Example: 2025-06-02 | 08:07 AM | Metformin 500mg | Dorothy M.

The real-world problem

125,000 preventable U.S. deaths per year from medication non-adherence (CDC MMWR, 2017). Up to 69% of all rehospitalizations are linked to non-adherence (CDC MMWR, 2017). Between $100 billion and $300 billion in avoidable healthcare costs annually. Approximately 50% of chronic disease prescriptions are not taken correctly. No existing free, offline, PC-based medication tracker is designed for elderly patients with low technology literacy. MedBridge fills that gap.

References

Neiman, A. B., Ruppar, T., Ho, M., Garber, L., Weidle, P. J., Hong, Y., George, M. G., & Thorpe, P. G. (2017). CDC grand rounds: Improving medication adherence for chronic disease management. Morbidity and Mortality Weekly Report, 66(45), 1248–1251. https://doi.org/10.15585/mmwr.mm6645a2

Centers for Disease Control and Prevention. (2024, March). Pharmacy-based interventions to improve medication adherence. https://www.cdc.gov/cardiovascular-resources/php/medication-adherence/index.html

Fischer, M. A., Stedman, M. R., Lii, J., Vogeli, C., Shrank, W. H., Brookhart, M. A., & Weissman, J. S. (2010). Primary medication non-adherence: Analysis of 195,930 electronic prescriptions. Journal of General Internal Medicine, 25(4), 284–290. https://doi.org/10.1007/s11606-010-1253-9

Author

[Your Name] · AP Computer Science A · [School Name] · 2025–2026
