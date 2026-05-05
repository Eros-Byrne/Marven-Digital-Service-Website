# 🏴󠁧󠁢󠁷󠁬󠁳󠁿 Welsh Public Sector: Digital Capability & Assessment Tool

A practical, interactive digital solution designed to help public services in Wales understand, identify, and assess the capabilities required to meet the **Digital Service Standard for Wales**.

---

## 🎯 Project Overview
Our client, **Peter Thomas** (Head of AI, Cyber Resilience, and Digital Capability), identified a need for a workforce planning tool. This platform allows managers and teams to identify digital skill gaps and provides tailored resources to bridge them, ensuring Welsh public services are modern, user-centric, and secure.

### The Challenge
**How might we help public services in Wales assess their capabilities against the Digital Service Standard?**
*   **Target:** Users with varying levels of IT ability (Ease of use is a priority).
*   **Focus Areas:** Meeting user needs, creating digital teams, and using the right technology.
*   **Compliance:** Fully bilingual (Welsh/English) to follow the Welsh Government "First" movement.
*   **Data Ethics:** Designed with GDPR and the Computer Misuse Act in mind to protect user data and job security.

---

## 🛠 Tech Stack
*   **Backend:** Java (Spring Boot)
*   **Frontend:** HTML5, CSS3, Thymeleaf Fragments
*   **Styling:** Bootstrap 5.2.3 (Local: `bootstrap-5.2.3-dist/css/bootstrap.css`)
*   **Database:** SQL (Dynamic schema for Outcomes and Capabilities)
*   **Testing:** Playwright / Selenium for automated browser testing

---

## 🚀 Key Features

### 1. Interactive Outcomes Dashboard (`outcomes.html`)
*   **Dynamic Rendering:** Uses Thymeleaf fragments to allow outcomes to be added or updated via the database without code changes.
*   **Capability Dropdowns:** Each outcome features a dropdown menu for specific capabilities (e.g., C1, C2) that link directly to `capability/{id}`.
*   **Custom Styling:** Managed via `outcomes_page.css`.

### 2. Digital Skills Self-Assessment
*   A quiz focused on the 6 key outcomes of the Digital Service Standard.
*   Outputs suggested next steps and resources based on specific knowledge gaps.

### 3. Summary & Analytics Page
*   **Outcome Barchart:** Displays results for the 6 outcome questions from the most recent quiz attempt.
*   **Performance Tracking:** Once all quizzes are completed, it calculates an average score highlighting strengths and weaknesses.
*   **Personalization:** Displays the logged-in user’s name directly on the dashboard.
*   **Granular Data:** Includes fragmented line charts for each specific quiz to track improvement over time.

---

## 📊 Database Structure (Outcomes Table)

| Field | Type | Key | Description |
| :--- | :--- | :--- | :--- |
| **OutcomeID** | INT | PK | Unique ID for the outcome |
| **Title** | VARCHAR | - | Name of the digital standard category |
| **Description** | VARCHAR | - | Detailed explanation of requirements |

---

## 🔧 Setup & Workflow

### Repository Instructions
To prevent accidental pushes to the main team repository:
1.  **Fork** the team repository.
2.  **Remove the fork relationship** in GitHub settings.
3.  **Clone** your fork to your local machine.
4.  Verify the project runs on `localhost` before changing team member roles to **Reporter**.

### Running the Project
1.  Run the main Spring Boot application command.
2.  Navigate to `localhost` in your browser.
3.  Use the **Outcomes** button to view standards or the **Quiz** button to start an assessment.

### Terminal Tips (Vim)
If editing files via terminal, use these commands:
*   `:wq` — Save and Exit
*   `:q!` — Quit without saving
*   `:q` — Quit

---

## ⚖️ Legal & Ethical Considerations
This tool is built to assist in **upskilling**, not for disciplinary action. Data collected is used to identify where resources should be focused to help teams improve. Users must handle data legally and ethically according to current Wales laws including GDPR.

---

Here is how the website looks also check out the showcase video in project:

<img width="1504" height="1271" alt="image" src="https://github.com/user-attachments/assets/d7eb3812-61b1-4313-ad95-d48ea58bc928" /> 
