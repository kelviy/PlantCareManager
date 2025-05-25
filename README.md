# Plant Care Manager

A desktop Java Swing application for tracking houseplants, to-do tasks, and encrypted notes — all in one place.  
Provides filtering, sorting, “due soon” reminders, PDF export and seamless UI powered by FlatLaf. A High School Project(PAT) at St Benedict's College. 
Additional information in pdf documents are available in the `documentation` folder. 

---

## Table of Contents

- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Installation & Setup](#installation--setup)  
- [Running the App](#running-the-app)  
- [Usage](#usage)  

---

## Features

- **Plants**: add, edit, delete, label and view your plant collection  
- **Tasks**: create due‐date tasks, mark complete, filter by label, view “due soon”  
- **Encrypted Notes**: password-protected notes using AES-GCM (PBKDF2WithHmacSHA256)  
- **Sorting & Filtering**: sort ascending/descending, by label or due date  
- **PDF Export**: generate a styled PDF report of selected plants or tasks via iText  
- **Modern UI**: Flat IntelliJ Light look-and-feel (FlatLaf) with NetBeans‐designed forms  

### Images of UI
<img width="799" alt="image" src="https://github.com/user-attachments/assets/cef1a8f7-353d-4286-b94c-72347684acc8" />
<img width="794" alt="image" src="https://github.com/user-attachments/assets/81a8fa56-8223-452b-9315-8ede435ba8d2" />
<img width="796" alt="image" src="https://github.com/user-attachments/assets/969cb4ea-573d-48a1-8c7a-7f5299cb56f0" />
<img width="799" alt="image" src="https://github.com/user-attachments/assets/66e45af3-1671-4830-8a68-0761467fd84c" />


---

## Prerequisites

- **Java 11** (or newer) JDK  
- **IntelliJ IDEA** (recommended) or any IDE / build tool that supports Swing forms  
- **Dependencies** (add to your classpath or project library):  
  - [FlatLaf 1.2+](https://www.formdev.com/flatlaf/)  
  - [iText 5.x](https://github.com/itext/itextpdf)
  - LGoodDatePicker-11.2.1

---

## Installation & Setup

1. **Clone** this repository:  
   ` bash
   git clone https://github.com/your-user/PlantCareManager.git
  `
2. Open the Folder as a project in IntelliJ IDEA
3. The nessessary jar files should already be configured due to .idea folder

## Running the App
1. Navigate to Main.java in `src/plantcaremanager/UI/` and run the file in Intellij IDEA

## Usage
- Home Page
Switch between Plants, Tasks, and Notes tabs.
- Add / Edit / Delete
Click “+” or “✎” icons to add or edit entries.
- Labeling & Filtering
Select labels/filters from respective drop down menus.
- Tasks Due Soon
Tasks within the next 7 days appear in the Due Soon list.
- PDF Export
Select Download Plant List Menu option from menu drop down. A secondary pop-up will appear to allow selection of desired items to export to PDF
- Secure Notes
Enter password to decrypt; edit and re-lock via UI.

