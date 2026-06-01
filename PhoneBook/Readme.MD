# 📱 PhoneBook CLI — Java Console Application

A simple, clean phone contact manager built with pure `java.io` — no databases, no external libraries.

---

## ✅ Features

| Feature            | Description                                      |
|--------------------|--------------------------------------------------|
| Add Contact        | Save name & phone number to `phone.txt`          |
| View Contacts      | Display all contacts in a neat numbered table    |
| Search Contact     | Search by name OR phone number (case-insensitive)|
| Delete Contact     | Remove a contact with confirmation prompt        |
| Duplicate Guard    | Prevents saving the same phone number twice      |
| Input Validation   | Rejects empty fields and non-numeric phone input |

---

## 🗂 File Structure

```
PhoneBook/
├── PhoneBook.java   ← Main source file (single class, all logic)
├── phone.txt        ← Contact storage file (auto-created on first run)
└── README.md        ← This file
```

---

## ▶️ How to Run

### 1. Compile
```bash
javac PhoneBook.java
```

### 2. Run
```bash
java PhoneBook
```

---

## 📄 phone.txt Format

Each line stores one contact in this format:
```
Name - PhoneNumber
```
Example:
```
Alice Johnson - 08111234567
Bob Martinez - 08229876543
```

---

## 🔑 Key java.io Classes Used

| Class            | Purpose                                         |
|------------------|-------------------------------------------------|
| `File`           | Check if `phone.txt` exists; create if missing  |
| `FileWriter`     | Open file for writing (append or overwrite)     |
| `BufferedWriter` | Efficient line-by-line writing                  |
| `FileReader`     | Open file for reading                           |
| `BufferedReader` | Efficient line-by-line reading                  |

---

## 💡 How I Used AI

I used Claude (AI) to help me plan the structure of the program and review my logic. I then wrote the code myself, line by line, making sure I understood what each part does — especially how `BufferedWriter` appends data vs. overwrites, and why we need to rewrite the entire file when deleting a contact (since text files don't support in-place line deletion).

---

## 🧪 Sample Session

```
╔══════════════════════════════════╗
║   📱  Welcome to PhoneBook CLI   ║
║   Contacts stored in phone.txt   ║
╚══════════════════════════════════╝

┌─────────────────────────────┐
│         PHONE BOOK          │
├─────────────────────────────┤
│  1. Add Contact             │
│  2. View All Contacts       │
│  3. Search Contact          │
│  4. Delete Contact          │
│  5. Exit                    │
└─────────────────────────────┘
  Choose an option (1-5): 2

── All Contacts ─────────────────────────
  No.  Name                      Phone Number   
  ────────────────────────────────────────────────
  1    Alice Johnson              08111234567    
  2    Bob Martinez               08229876543    
  ────────────────────────────────────────────────
  Total: 2 contact(s)
```