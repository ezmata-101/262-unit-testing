# Assignment Announcement: Unit Testing — Parking Slot Booking (With Intentional Defects)

> **Our goal is not only to find bugs, but also to build a reusable, well-structured test suite**

* **Team Members:** Individual or a group of maximum 4 students. You may or may not work in your previous assignment team.
* **Deadline:** **One week after the assignment is announced**

---

## Objective

You will read the provided Java codebase and design comprehensive **JUnit tests** for each class. Your tests must both validate intended behaviors and reveal defects. You will also submit a short report documenting **why** each test exists, its **verdict**, and any **comments/observations**, plus a **separate defects list**.

> You will find the documentation in `documentation.md` file and the codebase in the `src/` directory.

**No need to fix the code.** Your job is to test it, document bugs.

---

## Evaluation

| Component                                          |            | Marks  |
|----------------------------------------------------|------------|--------|
| Finding bugs (defects list, quality & correctness) | Team       | 2      |
| Killing mutants (mutation score via PITest)        | Team       | 2      |
| Report + submission guideline compliance           | Team       | 2      |
| Viva on individual contribution                    | Individual | 4      |
| **Total**                                          |            | **10** |

---
## Finding Bugs
A Google Form will be provided to submit the bugs you find. You will need to provide the following information for each bug::
* **Defect ID** (e.g. D1, D2, …) [You may reuse the same ID later to update the bug status]
* **Class.Method** where the defect was found.
* **Description** of the defect.

## Mutation Testing

Your tests will be evaluated with **PITest**, a mutation testing tool. 
PIT introduces small artificial bugs ("mutants") into the code and checks whether your tests catch them. This means:

> A high mutation score will not guarantee full marks — you must also explain **why** your tests kill or fail to kill each mutant in your report.

**You should run and verify your tests locally before submitting.**
This can be done either in your IDE or via the command line. If you are using Maven, run:

```
mvn org.pitest:pitest-maven:mutationCoverage
```

The report (`target/pit-reports/`) shows exactly which lines have surviving mutants — i.e. where your tests are too weak to notice a bug. Use it to strengthen your test suite, and **include the generated report in your submission** (see ZIP structure below).

**Some students may be selected for a short **live walkthrough**: you'll be asked to explain 2–3 of your own tests — what they check and why, and what would happen if a specific line of code were subtly changed. This is straightforward if you wrote and understood your own tests.**

---

## What to Submit

Submit a **single ZIP** named exactly:

```
<student_id>_unit_test.zip      e.g. 011223445_011223446.zip
```

**Inside the ZIP:**

```
test/
  VehicleTest.java
  WalletTest.java
  ParkingSlotTest.java
  BookingTest.java
  ParkingSystemTest.java
pit-reports/
  (your generated target/pit-reports/ folder, as-is)
<student_id>_unit_test_report.md
```

* Put **only your test files** in `test/`. **Do not include** the source code, class files or build files under test.
* Include your **PIT report folder** exactly as generated — do not edit or trim it.
* The **Markdown** report format is described below.
* **Ensure the ZIP structure is exactly as shown**. An automated script will run your tests against our reference build and cross-check against your submitted mutation report.

**Group submissions:** submit **one ZIP per team** — one `test/` directory, one `pit-reports/` folder, and one report, shared jointly, **not one contribution appended after another**. The test suite should read as a single coherent suite, and the report should read as one team's analysis, not four separate write-ups pasted together (see report structure below — this applies section by section, not just the contribution list at the end).

---

## Report Requirements (`011223445_011223446_unit_test_report.md`)
> **DO NOT WRITE YOUR REPORT USING AI.** You may use AI tools to check grammar, spelling, or formatting, but **all content must be your own work**. Any violation will result in a score of 0 for the assignment. Share your AI conversations(prompts) if you used it to correct grammar or spelling or formatting.


### 0) Team members
* Student Id and Name of team members.

### A) Test Case List (table)

For every test you wrote, include a row with the following columns:

* **Test ID**.
* **Class.Method** under test.
* **Why this test?** should state the specific rule/edge case.
* **Verdict** is the test outcome against the current code.
* **Comments/Observations** on behavior, exceptions, edge cases, etc.

### B) Defects List (A separate table / list)

List **each discovered defect** with a suggested fix:
* **Defect ID**.
* **Class.Method** where the defect was found.
* **Description** of the defect.

### C) Mutant Analysis (One per each member)

* Report your **overall mutation score** (from your PIT report).
* Name **one mutant your tests kill**, and explain in 2–3 sentences what behavior your test verifies that catches it.
* Name **one surviving mutant** (if any, after your best effort), and explain why it's hard to kill or why it may be an equivalent mutant.

### D) Individual Contribution
* A concise (NOT verbose) breakdown of the tasks performed by each team member

---

## Running Tests

* Your tests should compile & run in a standard JUnit 5 + Maven/Gradle setup, with PIT configured to produce a mutation report as described above.
* Do not modify the source code under `src/`.
* Design your tests to be **order-independent** — the codebase has global/static state; call any reset method provided before each test if needed.

**Setting up the project:** You may set up your own Maven/Gradle project and PIT configuration if you're comfortable doing so. If you'd rather not deal with the setup, a working boilerplate will be shared separately — you can just write your tests inside it. Either way, your final submission must produce a mutation report when `mvn org.pitest:pitest-maven:mutationCoverage` (or your Gradle equivalent) is run.

---

## Academic Integrity

* **Any use of AI tools (ChatGPT, Claude, Copilot, or similar) at any stage of this assignment — writing tests, writing the report, drafting explanations, or anything else — is strictly prohibited and will be penalized, even if disclosed.** This applies to every part of the submission, including the report.
* **Any other form of unfair means** (plagiarism, sharing code between teams, etc.) is also strictly prohibited.
* **Penalty:** score **0** on this assignment **and** deduction from other assessments.

---

## Reminders

* **Design tests to be order-independent** and resilient to the system's static global state.
* Document current behavior even when you believe it's wrong; reflect that in verdict and defects sections.
* **A high mutation score with a vague or incorrect mutant analysis will not receive full credit** — the goal is understanding, not just a number.

---
Best of luck!