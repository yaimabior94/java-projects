import java.io.*;
import java.util.*;

/**
 * PhoneBook - A simple console-based phone contact manager
 * Uses java.io for persistent file storage in phone.txt
 *
 * Author: [Your Name]
 * Description: This program lets users add, view, search, and delete
 *              phone contacts stored in a local text file.
 */
public class PhoneBook {

    // The file where all contacts are stored
    private static final String FILE_NAME = "phone.txt";
    private static final String SEPARATOR = " - ";

    // ─────────────────────────────────────────────────────────────
    //  ENTRY POINT
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        ensureFileExists();      // Create phone.txt if it doesn't exist yet
        showWelcomeBanner();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addContact(scanner);
                case "2" -> viewContacts();
                case "3" -> searchContact(scanner);
                case "4" -> deleteContact(scanner);
                case "5" -> {
                    System.out.println("\n  👋  Goodbye! Your contacts are safely saved.\n");
                    running = false;
                }
                default  -> System.out.println("  ⚠  Invalid option. Please enter 1–5.\n");
            }
        }

        scanner.close();
    }

    // ─────────────────────────────────────────────────────────────
    //  1. ADD CONTACT
    //     Reads name and phone number, checks for duplicates,
    //     then appends the new contact to phone.txt.
    // ─────────────────────────────────────────────────────────────
    private static void addContact(Scanner scanner) {
        System.out.println("\n── Add New Contact ──────────────────────");

        // Collect and validate name
        String name;
        while (true) {
            System.out.print("  Name        : ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("  ⚠  Name cannot be empty.");
        }

        // Collect and validate phone number (digits, spaces, +, - allowed)
        String phone;
        while (true) {
            System.out.print("  Phone Number: ");
            phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                System.out.println("  ⚠  Phone number cannot be empty.");
            } else if (!phone.matches("[0-9+\\-\\s]+")) {
                System.out.println("  ⚠  Phone number contains invalid characters.");
            } else {
                break;
            }
        }

        // Bonus: Check for duplicate phone number before saving
        if (isDuplicatePhone(phone)) {
            System.out.println("  ⚠  That phone number already exists. Contact not added.\n");
            return;
        }

        // Append contact to file using FileWriter in append mode (true)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(formatContact(name, phone));
            writer.newLine();
            System.out.println("  ✔  Contact saved successfully!\n");
        } catch (IOException e) {
            System.out.println("  ✖  Error saving contact: " + e.getMessage() + "\n");
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  2. VIEW ALL CONTACTS
    //     Reads every line from phone.txt and prints them in a
    //     numbered, neatly formatted table.
    // ─────────────────────────────────────────────────────────────
    private static void viewContacts() {
        List<String> contacts = loadContacts();

        System.out.println("\n── All Contacts ─────────────────────────");
        if (contacts.isEmpty()) {
            System.out.println("  (No contacts found. Add one first!)\n");
            return;
        }

        // Print table header
        System.out.printf("  %-4s %-25s %-15s%n", "No.", "Name", "Phone Number");
        System.out.println("  " + "─".repeat(48));

        // Print each contact as a numbered row
        for (int i = 0; i < contacts.size(); i++) {
            String[] parts = contacts.get(i).split(SEPARATOR, 2);
            if (parts.length == 2) {
                System.out.printf("  %-4d %-25s %-15s%n", i + 1, parts[0].trim(), parts[1].trim());
            }
        }
        System.out.println("  " + "─".repeat(48));
        System.out.printf("  Total: %d contact(s)%n%n", contacts.size());
    }

    // ─────────────────────────────────────────────────────────────
    //  3. SEARCH CONTACT
    //     Searches all contacts for a keyword (case-insensitive).
    //     Matches on both name and phone number fields.
    // ─────────────────────────────────────────────────────────────
    private static void searchContact(Scanner scanner) {
        System.out.println("\n── Search Contact ───────────────────────");
        System.out.print("  Enter name or phone to search: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("  ⚠  Search keyword cannot be empty.\n");
            return;
        }

        List<String> contacts = loadContacts();
        List<String> results = new ArrayList<>();

        // Filter contacts that contain the keyword anywhere in the line
        for (String contact : contacts) {
            if (contact.toLowerCase().contains(keyword)) {
                results.add(contact);
            }
        }

        // Display results
        if (results.isEmpty()) {
            System.out.println("  No contacts matched \"" + keyword + "\".\n");
        } else {
            System.out.println("  Found " + results.size() + " result(s):\n");
            System.out.printf("  %-4s %-25s %-15s%n", "No.", "Name", "Phone Number");
            System.out.println("  " + "─".repeat(48));
            for (int i = 0; i < results.size(); i++) {
                String[] parts = results.get(i).split(SEPARATOR, 2);
                if (parts.length == 2) {
                    System.out.printf("  %-4d %-25s %-15s%n", i + 1, parts[0].trim(), parts[1].trim());
                }
            }
            System.out.println();
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  4. DELETE CONTACT
    //     Shows all contacts, asks for a number, removes that entry,
    //     then rewrites phone.txt with the remaining contacts.
    // ─────────────────────────────────────────────────────────────
    private static void deleteContact(Scanner scanner) {
        List<String> contacts = loadContacts();

        System.out.println("\n── Delete Contact ───────────────────────");
        if (contacts.isEmpty()) {
            System.out.println("  (No contacts to delete.)\n");
            return;
        }

        // Show the current list so the user can pick a number
        viewContacts();

        System.out.print("  Enter the contact number to delete (0 to cancel): ");
        String input = scanner.nextLine().trim();

        int index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("  ⚠  Invalid number entered.\n");
            return;
        }

        if (index == 0) {
            System.out.println("  Deletion cancelled.\n");
            return;
        }

        if (index < 1 || index > contacts.size()) {
            System.out.println("  ⚠  Number out of range.\n");
            return;
        }

        // Confirm before deleting
        String target = contacts.get(index - 1);
        System.out.println("  About to delete: " + target);
        System.out.print("  Are you sure? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("  Deletion cancelled.\n");
            return;
        }

        // Remove the selected contact and rewrite the file
        contacts.remove(index - 1);
        saveAllContacts(contacts);
        System.out.println("  ✔  Contact deleted successfully!\n");
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPER: loadContacts
    //     Reads phone.txt line by line and returns a List of strings.
    //     Skips empty or blank lines automatically.
    // ─────────────────────────────────────────────────────────────
    private static List<String> loadContacts() {
        List<String> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    contacts.add(line.trim());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("  ⚠  phone.txt not found. Starting fresh.\n");
        } catch (IOException e) {
            System.out.println("  ✖  Error reading file: " + e.getMessage() + "\n");
        }

        return contacts;
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPER: saveAllContacts
    //     Overwrites phone.txt with the given list of contacts.
    //     Used after a deletion to refresh the file.
    // ─────────────────────────────────────────────────────────────
    private static void saveAllContacts(List<String> contacts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (String contact : contacts) {
                writer.write(contact);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("  ✖  Error writing to file: " + e.getMessage() + "\n");
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPER: isDuplicatePhone
    //     Scans all saved contacts and returns true if the given
    //     phone number already exists (ignoring spaces for safety).
    // ─────────────────────────────────────────────────────────────
    private static boolean isDuplicatePhone(String phone) {
        String normalised = phone.replaceAll("\\s", "");
        for (String contact : loadContacts()) {
            String[] parts = contact.split(SEPARATOR, 2);
            if (parts.length == 2) {
                String existing = parts[1].trim().replaceAll("\\s", "");
                if (existing.equals(normalised)) return true;
            }
        }
        return false;
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPER: formatContact
    //     Combines name and phone into the standard storage format:
    //     "John Doe - 08123456789"
    // ─────────────────────────────────────────────────────────────
    private static String formatContact(String name, String phone) {
        return name + SEPARATOR + phone;
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPER: ensureFileExists
    //     Creates phone.txt if it doesn't already exist,
    //     preventing FileNotFoundException on first run.
    // ─────────────────────────────────────────────────────────────
    private static void ensureFileExists() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("  ✖  Could not create phone.txt: " + e.getMessage());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  UI: showMenu  –  prints the main menu options
    // ─────────────────────────────────────────────────────────────
    private static void showMenu() {
        System.out.println("┌─────────────────────────────┐");
        System.out.println("│         PHONE BOOK          │");
        System.out.println("├─────────────────────────────┤");
        System.out.println("│  1. Add Contact             │");
        System.out.println("│  2. View All Contacts       │");
        System.out.println("│  3. Search Contact          │");
        System.out.println("│  4. Delete Contact          │");
        System.out.println("│  5. Exit                    │");
        System.out.println("└─────────────────────────────┘");
        System.out.print("  Choose an option (1-5): ");
    }

    // ─────────────────────────────────────────────────────────────
    //  UI: showWelcomeBanner  –  first thing the user sees
    // ─────────────────────────────────────────────────────────────
    private static void showWelcomeBanner() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║   📱  Welcome to PhoneBook CLI   ║");
        System.out.println("║   Contacts stored in phone.txt   ║");
        System.out.println("╚══════════════════════════════════╝\n");
    }
}


























