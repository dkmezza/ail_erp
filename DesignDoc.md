# APECK Digital Operations System

## Phase 1: Cash Management Module

### Design Documentation v1.0

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [System Overview](#system-overview)
3. [User Personas](#user-personas)
4. [User Stories](#user-stories)
5. [Functional Requirements](#functional-requirements)
6. [System Architecture](#system-architecture)
7. [Data Model](#data-model)
8. [User Interface Design](#user-interface-design)
9. [Business Rules](#business-rules)
10. [Security & Access Control](#security--access-control)
11. [Non-Functional Requirements](#non-functional-requirements)
12. [Implementation Plan](#implementation-plan)

---

## 1. Executive Summary

### 1.1 Purpose

This document describes the design and specifications for the APECK Digital Operations System Phase 1, which digitizes the cash requisition and expenditure retirement processes for field staff and headquarters personnel.

### 1.2 Scope

Phase 1 covers:

- Digital cash requisition submission and approval workflow
- Expenditure retirement with itemized expenses and receipt uploads
- Multi-platform access (Android mobile app and web application)
- Role-based access control
- Real-time notifications

### 1.3 Goals

- Eliminate paper-based cash requisition forms
- Reduce approval time from days to hours
- Provide real-time visibility into cash disbursements
- Enable mobile-first experience for field staff
- Create automatic reconciliation between requisitions and retirements
- Establish foundation for future ERP modules

---

## 2. System Overview

### 2.1 Current State

APECK International currently uses:

- Paper-based Cash Requisition Forms (Form #1401)
- Paper-based Expenditure Retirement Forms (Form #2001)
- Manual signature collection for approvals
- Physical receipt attachments
- Manual reconciliation of amounts

**Pain Points:**

- Lost or misplaced forms
- Approval delays when managers travel
- Manual calculation errors
- No audit trail visibility
- Difficult to track outstanding retirements
- Time-consuming reconciliation process

### 2.2 Proposed Solution

A digital system with:

- **Mobile Application (Android):** Primary interface for field staff
- **Web Application:** Full-featured interface for HQ staff and detailed management
- **Cloud Backend:** Centralized API and database
- **Notification System:** Email and SMS alerts for workflow actions

### 2.3 Key Benefits

- **Speed:** Requisition submission in 2 minutes vs 10+ minutes
- **Visibility:** Real-time status tracking
- **Accuracy:** Auto-calculation eliminates math errors
- **Accessibility:** Approve from anywhere via mobile
- **Accountability:** Complete audit trail with timestamps
- **Efficiency:** No double-entry or manual reconciliation

---

## 3. User Personas

### 3.1 Persona: Field Staff Member

**Name:** John Mwanza  
**Role:** Driver / Field Technician  
**Age:** 32  
**Tech Proficiency:** Medium (uses smartphone daily)  
**Location:** Frequently on the road, remote field locations

**Goals:**

- Quickly request cash for field operations
- Submit expense retirements without returning to office
- Track status of requests
- Avoid lost paper forms

**Pain Points:**

- Limited time to fill forms while in field
- Unreliable paper form storage in vehicle
- Delays waiting for manager signatures
- Difficulty attaching physical receipts

**Usage Pattern:**

- Submits 3-5 requisitions per month
- Primarily uses mobile phone
- Often has intermittent internet connectivity

---

### 3.2 Persona: Department Manager (Approver)

**Name:** Sarah Komba  
**Role:** Operations Manager  
**Age:** 42  
**Tech Proficiency:** High  
**Location:** HQ office, frequent travel

**Goals:**

- Review and approve requests quickly
- Maintain visibility of departmental spending
- Approve requests while traveling
- Ensure proper documentation

**Pain Points:**

- Staff waiting for signatures when she's traveling
- Stack of paper forms on desk
- Difficulty tracking what's been approved
- No consolidated view of department spending

**Usage Pattern:**

- Reviews 15-30 requisitions per month
- Uses both mobile and web
- Needs to approve requests outside office hours

---

### 3.3 Persona: Finance Manager

**Name:** Grace Mbwambo  
**Role:** Finance Manager / Accountant  
**Age:** 38  
**Tech Proficiency:** High  
**Location:** HQ office

**Goals:**

- Efficiently disburse approved funds
- Review retirement submissions with receipts
- Track outstanding retirements
- Maintain accurate financial records
- Generate reports for management

**Pain Points:**

- Manual reconciliation of amounts
- Poor quality receipt copies
- Chasing staff for overdue retirements
- Difficult to track who has unreturned advances
- Time-consuming data entry into accounting system

**Usage Pattern:**

- Processes 100-150 transactions per month
- Primarily uses web application
- Needs detailed reporting and export capabilities

---

### 3.4 Persona: Managing Director

**Name:** Dr. Amani Masoud  
**Role:** Managing Director  
**Age:** 55  
**Tech Proficiency:** Medium  
**Location:** HQ office, frequent business trips

**Goals:**

- High-level visibility of company spending
- Quick approval of high-value requisitions
- Monitor departmental budgets
- Access information while traveling

**Pain Points:**

- Delayed approvals when traveling
- Limited visibility into spending patterns
- Difficulty tracking budget vs actual
- Manual report compilation

**Usage Pattern:**

- Reviews 5-10 high-value requisitions per month
- Prefers mobile for quick approvals
- Requires executive dashboard and reports

---

## 4. User Stories

### 4.1 Cash Requisition - Field Staff

#### US-001: Submit Cash Requisition

**As a** field staff member  
**I want to** submit a cash requisition from my mobile phone  
**So that** I can quickly request funds without filling paper forms

**Acceptance Criteria:**

- User can fill requisition form in under 2 minutes
- Form includes: date, amount, department, description
- Amount field validates numeric input > 0
- Description requires minimum 10 characters
- User receives confirmation upon submission
- Requisition number is auto-generated
- User receives notification when status changes

**Priority:** HIGH  
**Story Points:** 5

---

#### US-002: View My Requisition Status

**As a** field staff member  
**I want to** see the status of all my requisitions  
**So that** I know which requests are pending, approved, or rejected

**Acceptance Criteria:**

- Display list of my requisitions with status badges
- Filter by status: All, Pending, Approved, Rejected, Disbursed
- Show key info: requisition number, date, amount, status
- Tap to view full details
- Display timeline of approval workflow
- Show approver names and timestamps
- If rejected, show rejection reason

**Priority:** HIGH  
**Story Points:** 3

---

#### US-003: Receive Approval Notifications

**As a** field staff member  
**I want to** receive notifications when my requisition is approved or rejected  
**So that** I know immediately and can take next action

**Acceptance Criteria:**

- Push notification on mobile app
- SMS notification (optional setting)
- Email notification
- Notification shows requisition number and new status
- Tapping notification opens requisition details
- Notification badge shows unread count

**Priority:** HIGH  
**Story Points:** 3

---

### 4.2 Cash Requisition - Approver

#### US-004: Review Pending Requisitions

**As an** approver  
**I want to** see all requisitions pending my approval  
**So that** I can review and act on them promptly

**Acceptance Criteria:**

- Dedicated "Pending Approvals" screen
- Shows requisitions from my department/responsibility
- Display: requester name, date, amount, description
- Sort by date (oldest first)
- Badge showing count of pending items
- Tap to view full details

**Priority:** HIGH  
**Story Points:** 3

---

#### US-005: Approve Cash Requisition

**As an** approver  
**I want to** approve a requisition with a single tap  
**So that** I can quickly authorize legitimate requests

**Acceptance Criteria:**

- "Approve" button on requisition detail screen
- Option to modify approved amount (if different from requested)
- Confirmation dialog before approving
- Timestamp and approver name recorded
- Requester and finance notified immediately
- Status changes to "Approved"
- Can't be undone once approved

**Priority:** HIGH  
**Story Points:** 3

---

#### US-006: Reject Cash Requisition

**As an** approver  
**I want to** reject a requisition with a reason  
**So that** the requester understands why and can resubmit if needed

**Acceptance Criteria:**

- "Reject" button on requisition detail screen
- Mandatory rejection reason field (minimum 10 characters)
- Confirmation dialog
- Timestamp and approver name recorded
- Requester notified with rejection reason
- Status changes to "Rejected"
- Requester can submit new requisition (not edit rejected one)

**Priority:** HIGH  
**Story Points:** 2

---

### 4.3 Cash Requisition - Finance

#### US-007: Disburse Approved Funds

**As a** finance manager  
**I want to** record disbursement of approved funds  
**So that** I can track which requisitions have been paid

**Acceptance Criteria:**

- View list of approved requisitions pending disbursement
- "Disburse" button on requisition details
- Select payment method: Mobile Money, Bank, Cash, Cheque
- Enter payment reference (optional for mobile money/bank)
- Record disbursement date
- Timestamp and finance user recorded
- Status changes to "Disbursed"
- Requester notified
- Requisition becomes eligible for retirement

**Priority:** HIGH  
**Story Points:** 3

---

#### US-008: Track Outstanding Retirements

**As a** finance manager  
**I want to** see which disbursed requisitions haven't been retired  
**So that** I can follow up with staff

**Acceptance Criteria:**

- Dashboard widget showing count of outstanding retirements
- List view of disbursed requisitions without retirement
- Show: days since disbursement, amount, staff name
- Highlight overdue (>30 days)
- Sortable by amount, date, staff
- Export to Excel

**Priority:** MEDIUM  
**Story Points:** 5

---

### 4.4 Expenditure Retirement - Field Staff

#### US-009: Submit Expenditure Retirement

**As a** field staff member  
**I want to** submit retirement for disbursed funds  
**So that** I can account for how money was spent

**Acceptance Criteria:**

- Select from my disbursed, non-retired requisitions
- Auto-populate: requisition details, amount received
- Add line items: date, description, cost
- Running total shows amount expensed
- Amount remaining calculated automatically
- Warning if over-spent or significant amount remaining
- Upload receipt photos (camera or gallery)
- Accept jpg, jpeg, png, pdf formats
- Max 5MB per file, max 20 files
- Preview uploaded receipts
- Delete receipts before submission
- Can't submit without at least 1 line item and 1 receipt
- Retirement number auto-generated
- Finance notified upon submission

**Priority:** HIGH  
**Story Points:** 8

---

#### US-010: Edit Draft Retirement

**As a** field staff member  
**I want to** save retirement as draft and edit later  
**So that** I can work on it over multiple sessions

**Acceptance Criteria:**

- "Save as Draft" button
- Draft saved locally (offline capable)
- Can edit draft multiple times
- Draft list shows "Draft" status
- Can delete draft
- Can submit when ready

**Priority:** MEDIUM  
**Story Points:** 3

---

#### US-011: View Retirement Status

**As a** field staff member  
**I want to** track status of my retirement submissions  
**So that** I know if finance has approved them

**Acceptance Criteria:**

- List of my retirements with status
- Filter: All, Pending, Approved, Rejected
- Show: retirement number, requisition link, amount, status
- Tap to view details including line items and receipts
- If approved, show approval date and approver
- If rejected, show finance notes/reason
- Notification when status changes

**Priority:** HIGH  
**Story Points:** 3

---

### 4.5 Expenditure Retirement - Finance

#### US-012: Review Retirement Submission

**As a** finance manager  
**I want to** review retirement details and receipts  
**So that** I can verify expenses are legitimate

**Acceptance Criteria:**

- List of pending retirements
- View retirement details: all line items, totals, amounts
- Gallery view of all receipts
- Tap receipt to view full-size
- Zoom and pan on receipt images
- Download PDF receipts
- See linked requisition details
- Calculate: amount received, amount spent, amount remaining
- Highlight discrepancies

**Priority:** HIGH  
**Story Points:** 5

---

#### US-013: Approve Retirement

**As a** finance manager  
**I want to** approve a retirement submission  
**So that** the expense cycle is completed

**Acceptance Criteria:**

- "Approve" button on retirement details
- Optional finance notes field
- Confirmation dialog
- Timestamp and finance user recorded
- Status changes to "Approved"
- Employee notified
- If amount remaining > 0, flag for cash collection
- If amount remaining < 0, flag for reimbursement

**Priority:** HIGH  
**Story Points:** 3

---

#### US-014: Reject Retirement with Feedback

**As a** finance manager  
**I want to** reject a retirement with specific feedback  
**So that** the employee knows what to correct and can resubmit

**Acceptance Criteria:**

- "Reject" button on retirement details
- Mandatory finance notes field (why rejected)
- Common rejection reasons dropdown + custom text
- Confirmation dialog
- Status changes to "Rejected"
- Employee notified with feedback
- Employee can view rejection reason
- Employee can submit corrected retirement

**Priority:** HIGH  
**Story Points:** 3

---

### 4.6 Notifications

#### US-015: Receive Real-Time Notifications

**As a** user  
**I want to** receive notifications for relevant actions  
**So that** I can respond promptly

**Notification Triggers:**

- **Field Staff:**
  - Requisition approved
  - Requisition rejected
  - Funds disbursed
  - Retirement approved
  - Retirement rejected

- **Approver:**
  - New requisition submitted (their department)

- **Finance:**
  - Requisition approved (ready for disbursement)
  - New retirement submitted

**Acceptance Criteria:**

- Push notification on mobile app
- In-app notification center
- Email notification (configurable)
- SMS notification for critical actions (configurable)
- Unread badge on notification icon
- Mark as read/unread
- "Mark all as read" option
- Tap notification navigates to relevant item
- Notification history (last 30 days)

**Priority:** HIGH  
**Story Points:** 5

---

### 4.7 Dashboard & Reporting

#### US-016: View Personal Dashboard

**As a** field staff member  
**I want to** see summary of my requisitions and retirements  
**So that** I have quick overview of my status

**Acceptance Criteria:**

- Cards showing:
  - Pending requisitions (count)
  - Approved awaiting retirement (count, total amount)
  - Pending retirements (count)
  - Recent activity (last 5 actions)
- Quick action buttons: New Requisition, Retire Funds
- Tap card to see details

**Priority:** MEDIUM  
**Story Points:** 3

---

#### US-017: View Approver Dashboard

**As an** approver  
**I want to** see summary of items needing my attention  
**So that** I can prioritize my work

**Acceptance Criteria:**

- Cards showing:
  - Pending approvals (count, total value)
  - Approved this month (count, total)
  - Average approval time
  - Department spending summary
- List of oldest pending approvals
- Quick approve/reject actions

**Priority:** MEDIUM  
**Story Points:** 5

---

#### US-018: View Finance Dashboard

**As a** finance manager  
**I want to** see comprehensive financial overview  
**So that** I can manage cash flow and retirements

**Acceptance Criteria:**

- Cards showing:
  - Pending disbursements (count, total amount)
  - Pending retirements (count)
  - Outstanding retirements >30 days (count, amount)
  - Cash disbursed this month
  - Overdue retirements highlighted
- Charts:
  - Monthly disbursement trend
  - Retirement status breakdown
  - Department spending comparison
- Quick access to pending items

**Priority:** MEDIUM  
**Story Points:** 8

---

#### US-019: Generate Reports

**As a** finance manager or MD  
**I want to** generate reports on requisitions and retirements  
**So that** I can analyze spending patterns

**Acceptance Criteria:**

- Report types:
  - All requisitions (date range)
  - All retirements (date range)
  - Outstanding retirements
  - Department spending summary
  - User activity report
- Filters: date range, department, status, user
- Export formats: Excel, PDF, CSV
- Preview before download
- Email report option

**Priority:** LOW  
**Story Points:** 5

---

### 4.8 User Management (Admin)

#### US-020: Manage Users

**As an** admin  
**I want to** create and manage user accounts  
**So that** staff can access the system with appropriate permissions

**Acceptance Criteria:**

- Create new user: name, email, phone, role, department
- Edit user details
- Reset user password
- Deactivate/activate user
- View user activity log
- Roles: Field Staff, Approver, Finance, Admin
- Bulk user import (CSV)

**Priority:** MEDIUM  
**Story Points:** 5

---

## 5. Functional Requirements

### 5.1 Authentication & Authorization

**FR-001: User Login**

- Users must authenticate with email/phone and password
- Passwords must be hashed (bcrypt, minimum 8 characters)
- JWT token-based authentication
- Token expiry: 7 days
- Refresh token mechanism
- "Remember me" option on mobile

**FR-002: Password Management**

- Forgot password flow via email
- Password reset with token expiry (1 hour)
- Change password (requires current password)
- Password strength validation

**FR-003: Role-Based Access Control**

- Four roles: Field Staff, Approver, Finance, Admin
- Permissions enforced at API level
- UI elements shown/hidden based on role
- Cross-cutting concerns: own data vs all data

---

### 5.2 Cash Requisition Module

**FR-004: Create Requisition**

- Auto-generate unique requisition number: REQ-YYYY-NNNN
- Capture: date, amount, department, description
- Default date: today
- Amount: decimal, 2 decimal places, > 0
- Description: text, minimum 10 characters
- Department: dropdown from predefined list
- Save as draft or submit

**FR-005: Approve/Reject Requisition**

- Only users with Approver or Admin role
- Can modify approved amount
- Must provide rejection reason
- Record approver ID and timestamp
- Trigger notifications

**FR-006: Disburse Funds**

- Only Finance or Admin role
- Only for requisitions with status = Approved
- Select payment method: Mobile Money, Bank, Cash, Cheque
- Optional payment reference
- Record finance user ID and timestamp
- Change status to Disbursed

**FR-007: View Requisitions**

- List view with pagination (20 per page)
- Filter by: status, date range, department, user
- Search by: requisition number, description
- Sort by: date, amount, status
- Field Staff: see only own requisitions
- Approver: see all requisitions
- Finance/Admin: see all requisitions

---

### 5.3 Expenditure Retirement Module

**FR-008: Create Retirement**

- Auto-generate unique retirement number: RET-YYYY-NNNN
- Link to one requisition (status = Disbursed, not yet retired)
- Auto-populate amount received from requisition
- Add multiple line items: date, description, cost
- Auto-calculate total expensed and amount remaining
- Upload multiple receipts (jpg/jpeg/png/pdf, max 5MB each, max 20 files)
- Validate: at least 1 line item, at least 1 receipt
- Save as draft or submit

**FR-009: Review Retirement**

- Finance/Admin can review all pending retirements
- View all details: line items, totals, receipts
- Download receipts
- View linked requisition

**FR-010: Approve/Reject Retirement**

- Only Finance or Admin role
- Optional finance notes
- Record finance user ID and timestamp
- Trigger notifications
- If rejected, employee can resubmit

**FR-011: Retirement Reconciliation**

- System calculates: amount_remaining = amount_received - amount_expensed
- Flag if amount_remaining > threshold (e.g., 10,000 TZS)
- Flag if amount_remaining < 0 (overspent)
- Display clear indicators on UI

---

### 5.4 Notifications

**FR-012: Notification System**

- In-app notifications (stored in database)
- Push notifications (mobile app)
- Email notifications (configurable per user)
- SMS notifications (critical actions only, configurable)
- Notification types with templates
- Mark as read/unread
- Notification center in app
- Badge count on icon

**FR-013: Notification Triggers**
| Event | Recipient | Channels |
|-------|-----------|----------|
| Requisition submitted | Approver | Push, Email |
| Requisition approved | Requester, Finance | Push, Email, SMS |
| Requisition rejected | Requester | Push, Email, SMS |
| Funds disbursed | Requester | Push, Email, SMS |
| Retirement submitted | Finance | Push, Email |
| Retirement approved | Submitter | Push, Email |
| Retirement rejected | Submitter | Push, Email |
| Retirement overdue (30 days) | Submitter, Finance | Email, SMS |

---

### 5.5 File Management

**FR-014: File Upload**

- Accepted formats: .jpg, .jpeg, .png, .pdf
- Max file size: 5MB
- Max files per retirement: 20
- Store with unique filename (UUID)
- Organize in folder structure: /uploads/retirements/YYYY/MM/RET-YYYY-NNNN/
- Validate file type and size server-side
- Virus scan (optional)

**FR-015: File Storage**

- Store on server filesystem or cloud storage (S3/Cloudinary)
- Secure access: only authorized users
- Files tied to retirement ID
- Delete files when retirement deleted
- Automatic backup included in system backup

---

### 5.6 Audit & Logging

**FR-016: Audit Trail**

- Log all significant actions:
  - User login/logout
  - Requisition created/approved/rejected/disbursed
  - Retirement created/approved/rejected
  - User created/modified/deactivated
- Store: timestamp, user, action, entity ID, old/new values
- Immutable logs
- Retention: minimum 3 years

**FR-017: Activity History**

- Each requisition/retirement has activity timeline
- Show: who did what, when
- Display on detail view

---

## 6. System Architecture

### 6.1 High-Level Architecture

```
┌─────────────────────────────────────────────────────┐
│                   Client Layer                       │
├──────────────────────┬──────────────────────────────┤
│  Flutter Mobile App  │     Next.js Web App          │
│     (Android)        │        (Browser)             │
└──────────────┬───────┴──────────────┬───────────────┘
               │                      │
               │    HTTPS/REST API    │
               │                      │
┌──────────────┴──────────────────────┴───────────────┐
│              Application Layer                       │
│         Spring Boot Backend (Java)                   │
│  ┌────────────────────────────────────────────┐    │
│  │  Controllers │ Services │ Repositories      │    │
│  │  Security │ Validation │ Business Logic     │    │
│  └────────────────────────────────────────────┘    │
└──────────────┬──────────────────────┬───────────────┘
               │                      │
               │                      │
┌──────────────┴──────────────────────┴───────────────┐
│              Data Layer                              │
├──────────────────────┬──────────────────────────────┤
│  PostgreSQL Database │   File Storage (Filesystem)  │
│  (Structured Data)   │   (Receipt Images/PDFs)      │
└──────────────────────┴──────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│            External Services                         │
├──────────────────────┬──────────────────────────────┤
│   Email Service      │    SMS Service               │
│   (SMTP)             │    (Africa's Talking)        │
└──────────────────────┴──────────────────────────────┘
```

### 6.2 Technology Stack

**Backend:**

- **Framework:** Spring Boot 3.x (Java 17+)
- **Database:** PostgreSQL 15+
- **Authentication:** JWT (JSON Web Tokens)
- **API Documentation:** Swagger/OpenAPI 3
- **Build Tool:** Maven or Gradle

**Web Frontend:**

- **Framework:** Next.js 14+ (React)
- **Language:** TypeScript
- **Styling:** Tailwind CSS
- **State Management:** React Context / Redux
- **HTTP Client:** Axios

**Mobile App:**

- **Framework:** Flutter 3.x
- **Language:** Dart
- **State Management:** Provider / Riverpod
- **Local Storage:** SQLite (for offline caching)
- **HTTP Client:** Dio

**Infrastructure:**

- **Hosting:** Cloud VPS (DigitalOcean / AWS / Azure)
- **Web Server:** Nginx (reverse proxy)
- **SSL:** Let's Encrypt
- **Monitoring:** Application logs + error tracking

**Third-Party Services:**

- **Email:** SMTP (Gmail / SendGrid)
- **SMS:** Africa's Talking API
- **File Storage:** Local filesystem (Phase 1), optional S3 later

---

### 6.3 API Design Principles

**RESTful API:**

- Resource-based URLs
- HTTP methods: GET, POST, PUT, DELETE
- Standard HTTP status codes
- JSON request/response format

**Authentication:**

- Bearer token in Authorization header
- Token validation on every protected endpoint

**Response Format:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2024-01-26T10:30:00Z"
}
```

**Error Format:**

```json
{
  "success": false,
  "message": "Validation error",
  "errors": [
    {
      "field": "amount",
      "message": "Amount must be greater than 0"
    }
  ],
  "timestamp": "2024-01-26T10:30:00Z"
}
```

**Pagination:**

```json
{
  "data": [...],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalPages": 5,
    "totalItems": 94
  }
}
```

---

## 7. Data Model

### 7.1 Entity Relationship Diagram

```
┌─────────────┐
│    Users    │
│─────────────│
│ id (PK)     │
│ name        │
│ email       │◄──────────┐
│ phone       │           │
│ role        │           │ requested_by
│ department  │           │
│ password    │           │
└─────────────┘           │
       │                  │
       │ approved_by      │
       │ disbursed_by     │
       │ finance_approved │
       │                  │
       │         ┌────────┴───────────┐
       │         │ Cash_Requisitions  │
       │         │────────────────────│
       │         │ id (PK)            │
       │         │ requisition_number │
       ├─────────┤ date               │
       │         │ amount_requested   │
       │         │ department         │
       │         │ description        │
       │         │ status             │
       │         │ approved_amount    │
       │         │ payment_method     │
       │         └────────┬───────────┘
       │                  │
       │                  │ requisition_id
       │                  │
       │         ┌────────┴──────────────────┐
       │         │ Expenditure_Retirements   │
       │         │───────────────────────────│
       │         │ id (PK)                   │
       │         │ retirement_number         │
       ├─────────┤ employee_name             │
       │         │ employee_title            │
       │         │ amount_received           │
       │         │ amount_expensed           │
       │         │ amount_remaining          │
       │         │ status                    │
       │         └────┬──────────────┬───────┘
       │              │              │
       │              │              │
       │    ┌─────────┴─────┐  ┌────┴──────────────┐
       │    │ Line_Items    │  │ Attachments       │
       │    │───────────────│  │───────────────────│
       │    │ id (PK)       │  │ id (PK)           │
       │    │ retirement_id │  │ retirement_id     │
       │    │ date          │  │ file_name         │
       │    │ description   │  │ file_path         │
       │    │ cost          │  │ file_type         │
       │    └───────────────┘  │ file_size         │
       │                       └───────────────────┘
       │
       │ user_id
       │
┌──────┴──────────┐
│ Notifications   │
│─────────────────│
│ id (PK)         │
│ type            │
│ message         │
│ related_id      │
│ related_type    │
│ read            │
└─────────────────┘
```

### 7.2 Database Tables (Detailed)

_[Refer to Section 4 of previous response for complete SQL DDL]_

Key relationships:

- User 1:N Cash_Requisitions (as requester)
- User 1:N Cash_Requisitions (as approver)
- User 1:N Cash_Requisitions (as disburser)
- Cash_Requisition 1:1 Expenditure_Retirement
- Expenditure_Retirement 1:N Retirement_Line_Items
- Expenditure_Retirement 1:N Retirement_Attachments
- User 1:N Notifications

---

## 8. User Interface Design

### 8.1 Design Principles

**Simplicity:**

- Minimal clicks to complete tasks
- Clear call-to-action buttons
- Progressive disclosure (show advanced options only when needed)

**Consistency:**

- Uniform color scheme: Company branding
- Standard components (buttons, forms, cards)
- Consistent navigation patterns

**Responsiveness:**

- Mobile-first design
- Adapts to different screen sizes
- Touch-friendly controls (minimum 44x44px)

**Accessibility:**

- High contrast text
- Clear labels and instructions
- Error messages in plain language
- Support for screen readers (web)

**Performance:**

- Fast loading (<2 seconds)
- Optimistic UI updates
- Loading indicators for long operations
- Offline indicators

---

### 8.2 Color Scheme & Branding

**Primary Colors:**

- Primary Blue: #1E40AF (APECK brand color)
- Success Green: #10B981
- Warning Yellow: #F59E0B
- Error Red: #EF4444
- Neutral Gray: #6B7280

**Status Colors:**

- Pending: #F59E0B (Yellow/Orange)
- Approved: #10B981 (Green)
- Rejected: #EF4444 (Red)
- Disbursed: #3B82F

6 (Blue)

**Text:**

- Primary: #1F2937 (Dark gray, almost black)
- Secondary: #6B7280 (Medium gray)
- Light: #9CA3AF (Light gray)

---

### 8.3 Mobile App Screens (Wireframes)

#### Screen 1: Login

```
┌──────────────────────────────┐
│        [APECK Logo]          │
│                              │
│    Digital Operations        │
│                              │
│  ┌────────────────────────┐ │
│  │ Email/Phone            │ │
│  └────────────────────────┘ │
│                              │
│  ┌────────────────────────┐ │
│  │ Password        [👁]   │ │
│  └────────────────────────┘ │
│                              │
│  ☐ Remember me               │
│                              │
│  ┌────────────────────────┐ │
│  │      LOGIN            │ │
│  └────────────────────────┘ │
│                              │
│     Forgot password?         │
│                              │
└──────────────────────────────┘
```

#### Screen 2: Home Dashboard (Field Staff)

```
┌──────────────────────────────┐
│  APECK Digital    [🔔3] [☰] │
├──────────────────────────────┤
│  Welcome, John Mwanza        │
│                              │
│  ┌────────────┬────────────┐│
│  │ Pending    │ Approved   ││
│  │ Requests   │ to Retire  ││
│  │     2      │     1      ││
│  │            │ TZS 50,000 ││
│  └────────────┴────────────┘│
│                              │
│  ┌──────────────────────────┐│
│  │ ➕ NEW REQUISITION       ││
│  └──────────────────────────┘│
│                              │
│  ┌──────────────────────────┐│
│  │ 💰 RETIRE FUNDS          ││
│  └──────────────────────────┘│
│                              │
│  Recent Activity             │
│  ┌──────────────────────────┐│
│  │ REQ-2024-0123    Approved││
│  │ TZS 75,000      Yesterday││
│  └──────────────────────────┘│
│  ┌──────────────────────────┐│
│  │ RET-2024-0089  Pending   ││
│  │ TZS 50,000      2 days ago│
│  └──────────────────────────┘│
│                              │
└──────────────────────────────┘
```

#### Screen 3: New Requisition Form

```
┌──────────────────────────────┐
│  ← New Requisition           │
├──────────────────────────────┤
│                              │
│  Date                        │
│  ┌────────────────────────┐ │
│  │ 26/01/2024    [📅]     │ │
│  └────────────────────────┘ │
│                              │
│  Amount (TZS)                │
│  ┌────────────────────────┐ │
│  │ 75,000                 │ │
│  └────────────────────────┘ │
│                              │
│  Department                  │
│  ┌────────────────────────┐ │
│  │ Operations        [▼]  │ │
│  └────────────────────────┘ │
│                              │
│  Description                 │
│  ┌────────────────────────┐ │
│  │ Fuel for field visit   │ │
│  │ to Dodoma clients...   │ │
│  │                        │ │
│  └────────────────────────┘ │
│                              │
│  ┌────────────────────────┐ │
│  │      SUBMIT           │ │
│  └────────────────────────┘ │
│                              │
└──────────────────────────────┘
```

#### Screen 4: My Requisitions List

```
┌──────────────────────────────┐
│  ← My Requisitions      [+]  │
├──────────────────────────────┤
│                              │
│ [All] [Pending] [Approved]   │
│                              │
│  ┌──────────────────────────┐│
│  │ REQ-2024-0125  [Pending] ││
│  │ 26 Jan 2024              ││
│  │ TZS 75,000               ││
│  │ Fuel for field visit     ││
│  └──────────────────────────┘│
│                              │
│  ┌──────────────────────────┐│
│  │ REQ-2024-0123 [Approved] ││
│  │ 24 Jan 2024              ││
│  │ TZS 50,000               ││
│  │ Lab supplies purchase    ││
│  └──────────────────────────┘│
│                              │
│  ┌──────────────────────────┐│
│  │ REQ-2024-0120 [Disbursed]││
│  │ 20 Jan 2024              ││
│  │ TZS 100,000              ││
│  │ Client visit expenses    ││
│  │ [RETIRE NOW] ────────────││
│  └──────────────────────────┘│
│                              │
└──────────────────────────────┘
```

#### Screen 5: Requisition Details

```
┌──────────────────────────────┐
│  ← REQ-2024-0125             │
├──────────────────────────────┤
│                              │
│  Status: [Pending]           │
│                              │
│  Date: 26 Jan 2024           │
│  Amount: TZS 75,000          │
│  Department: Operations      │
│                              │
│  Description:                │
│  Fuel for field visit to     │
│  Dodoma clients and sample   │
│  collection from farms.      │
│                              │
│  ────────────────────────    │
│                              │
│  Timeline                    │
│  ● Submitted                 │
│    26 Jan 2024, 09:15        │
│    By: John Mwanza           │
│                              │
│  ○ Pending Approval          │
│    Awaiting: Sarah Komba     │
│                              │
│                              │
└──────────────────────────────┘
```

#### Screen 6: New Retirement Form

```
┌──────────────────────────────┐
│  ← New Retirement            │
├──────────────────────────────┤
│                              │
│  Select Requisition          │
│  ┌────────────────────────┐ │
│  │ REQ-2024-0120       [▼]│ │
│  │ TZS 100,000            │ │
│  └────────────────────────┘ │
│                              │
│  Amount Received: 100,000    │
│                              │
│  Expense Items               │
│  ┌────────────────────────┐ │
│  │ 20/01  Fuel    45,000  │ │
│  │ 21/01  Meals    5,000  │ │
│  │ 21/01  Lodging 25,000  │ │
│  └────────────────────────┘ │
│  [+ Add Item]                │
│                              │
│  Total Expensed:  75,000     │
│  Amount Remaining: 25,000 ⚠️ │
│                              │
│  Receipts (3)                │
│  [📷][🖼️][🖼️]               │
│  [+ Add Receipt]             │
│                              │
│  ┌────────────────────────┐ │
│  │      SUBMIT           │ │
│  └────────────────────────┘ │
└──────────────────────────────┘
```

_(More screens for approvals, finance dashboard, notifications, etc. follow similar patterns)_

---

### 8.4 Web Application Layout

**Header:**

- Logo + App name (left)
- Navigation menu (center)
- Notifications icon (right)
- User profile dropdown (right)

**Sidebar (Collapsible):**

- Dashboard
- My Requisitions
- My Retirements
- Pending Approvals (if approver)
- Finance (if finance role)
- Reports
- Settings

**Main Content Area:**

- Page title
- Filters/search bar
- Data table or cards
- Pagination

**Responsive:**

- Sidebar collapses on mobile
- Tables become card view on mobile
- Forms stack vertically on mobile

---

## 9. Business Rules

### 9.1 Cash Requisition Rules

**BR-001: Requisition Submission**

- User can only submit requisition for themselves
- Amount must be > 0 and <= 10,000,000 TZS (configurable limit)
- Description minimum 10 characters
- Department must be selected
- Date cannot be in future

**BR-002: Requisition Approval**

- Only users with Approver or Admin role can approve
- Approvers can only approve requisitions from their department (unless Admin)
- Approved amount can be different from requested amount
- Approved amount must be > 0
- Once approved, requisition cannot be edited or deleted
- Rejection requires reason (minimum 10 characters)

**BR-003: Fund Disbursement**

- Only Finance or Admin role can disburse
- Can only disburse requisitions with status = Approved
- Payment method must be selected
- Once disbursed, requisition becomes eligible for retirement
- Cannot be undone (would require creating credit entry)

**BR-004: Requisition Lifecycle**

- Pending → Approved → Disbursed (happy path)
- Pending → Rejected (end state)
- Cannot go from Disbursed back to earlier states
- Deleted requisitions (if allowed) must be in Pending or Rejected status only

---

### 9.2 Expenditure Retirement Rules

**BR-005: Retirement Submission**

- Can only retire requisitions with status = Disbursed
- One requisition can have only one retirement (1:1 relationship)
- Must have at least 1 line item
- Must have at least 1 receipt attachment
- Amount received must match requisition approved amount
- Amount expensed = sum of all line item costs
- Line item cost must be > 0

**BR-006: Receipt Attachments**

- Accepted formats: jpg, jpeg, png, pdf only
- Max file size: 5MB per file
- Max files: 20 per retirement
- Files must be validated server-side
- Receipts can be added/removed before submission
- Once submitted, receipts cannot be modified

**BR-007: Retirement Approval**

- Only Finance or Admin role can approve/reject
- Finance can add notes/feedback
- If rejected, employee can correct and resubmit
- Once approved, retirement cannot be edited or deleted
- Approval completes the expense cycle

**BR-008: Amount Reconciliation**

- If amount_remaining > 0 (e.g., > 1,000 TZS): Employee must return cash
- If amount_remaining < 0: Employee overspent, may need reimbursement (requires approval)
- If amount_remaining = 0: Perfect reconciliation
- System highlights discrepancies

---

### 9.3 Notification Rules

**BR-009: Notification Delivery**

- Push notifications: sent immediately (mobile app only)
- Email notifications: sent within 5 minutes (batch)
- SMS notifications: sent immediately (critical actions only)
- In-app notifications: stored for 30 days
- Users can configure notification preferences

**BR-010: Notification Triggers**

- Requisition submitted → notify approver(s)
- Requisition approved → notify requester and finance
- Requisition rejected → notify requester
- Funds disbursed → notify requester
- Retirement submitted → notify finance
- Retirement approved → notify submitter
- Retirement rejected → notify submitter
- Retirement overdue (>30 days) → notify submitter and finance (weekly)

---

### 9.4 Security Rules

**BR-011: Data Access**

- Field Staff: can only view/edit own requisitions and retirements
- Approver: can view all requisitions in their department, own retirements
- Finance: can view all requisitions and retirements
- Admin: can view and manage all data

**BR-012: File Access**

- Receipt files can only be accessed by:
  - The employee who submitted the retirement
  - Finance users
  - Admin users
- File URLs are signed/tokenized, not publicly accessible
- File downloads are logged

**BR-013: Audit Logging**

- All create/update/delete operations are logged
- Logs include: timestamp, user, action, entity ID, changes
- Logs are immutable (append-only)
- Login/logout events are logged

---

## 10. Security & Access Control

### 10.1 Authentication

**Password Requirements:**

- Minimum 8 characters
- Must contain: uppercase, lowercase, number
- Special characters recommended but not required
- Stored as bcrypt hash (cost factor 12)

**Session Management:**

- JWT tokens with 7-day expiry
- Refresh token mechanism for mobile
- Token includes: user ID, role, email
- Token validated on every API request
- Logout invalidates token (server-side blacklist)

**Password Reset:**

- Email verification required
- Reset token expires in 1 hour
- Token single-use only
- Old password cannot be reused

---

### 10.2 Authorization Matrix

| Resource         | Field Staff | Approver    | Finance     | Admin |
| ---------------- | ----------- | ----------- | ----------- | ----- |
| **Requisitions** |
| Create           | Own only    | Own only    | Own only    | Yes   |
| View             | Own only    | Department  | All         | All   |
| Approve          | No          | Department  | No          | All   |
| Reject           | No          | Department  | No          | All   |
| Disburse         | No          | No          | All         | All   |
| **Retirements**  |
| Create           | Own only    | Own only    | Own only    | Yes   |
| View             | Own only    | Own only    | All         | All   |
| Approve          | No          | No          | All         | All   |
| Reject           | No          | No          | All         | All   |
| **Users**        |
| Create           | No          | No          | No          | Yes   |
| Edit             | Own profile | Own profile | Own profile | All   |
| Deactivate       | No          | No          | No          | Yes   |
| **Reports**      |
| View             | Own data    | Department  | All         | All   |
| Export           | No          | Department  | All         | All   |

---

### 10.3 Data Security

**Encryption:**

- All data in transit: HTTPS/TLS 1.3
- Passwords: bcrypt hashing
- Sensitive fields (optional): AES-256 encryption
- Database connection: SSL enabled

**Input Validation:**

- Server-side validation on all endpoints
- Sanitize inputs to prevent SQL injection
- File upload validation (type, size, content)
- Rate limiting on authentication endpoints

**API Security:**

- CORS configuration (whitelist origins)
- CSRF protection (web app)
- API rate limiting (100 req/min per user)
- SQL injection prevention (parameterized queries)
- XSS prevention (output encoding)

---

### 10.4 Backup & Recovery

**Database Backup:**

- Daily automated backups (retain 30 days)
- Weekly full backups (retain 12 weeks)
- Stored in separate location (cloud storage)
- Backup restoration tested monthly

**File Backup:**

- Receipt files backed up daily
- Synced to cloud storage (S3/similar)
- Retention: as long as retirement record exists + 3 years

**Disaster Recovery:**

- Recovery Time Objective (RTO): 4 hours
- Recovery Point Objective (RPO): 24 hours
- Documented recovery procedures
- Annual DR drill

---

## 11. Non-Functional Requirements

### 11.1 Performance

**NFR-001: Response Time**

- API response time: < 500ms (95th percentile)
- Page load time: < 2 seconds (web)
- App launch time: < 3 seconds (mobile)
- File upload: support files up to 5MB

**NFR-002: Scalability**

- Support 50 concurrent users (Phase 1)
- Database: handle 100,000 transactions/year
- File storage: up to 50GB (Phase 1)
- Horizontal scaling ready (stateless API)

**NFR-003: Availability**

- Uptime: 99% (8.76 hours downtime/year)
- Planned maintenance: weekends, announced 48h prior
- Monitoring: 24/7 automated alerts

---

### 11.2 Usability

**NFR-004: User Experience**

- Mobile app: intuitive, no training required for basic tasks
- Web app: consistent with modern web standards
- Error messages: clear, actionable, in plain language
- Forms: inline validation with helpful hints

**NFR-005: Accessibility**

- Web app: WCAG 2.1 Level AA compliance (target)
- High contrast mode support
- Keyboard navigation support
- Screen reader friendly (web)

**NFR-006: Localization**

- Primary language: English
- Date format: DD/MM/YYYY
- Currency: TZS (Tanzanian Shilling)
- Number format: 1,000.00
- Future: Swahili language support

---

### 11.3 Compatibility

**NFR-007: Mobile App**

- Android: 8.0 (API level 26) and above
- Target: Android 13/14
- Screen sizes: 4.5" to 7" phones
- Orientation: portrait (primary), landscape (support)

**NFR-008: Web App**

- Browsers: Chrome, Firefox, Safari, Edge (latest 2 versions)
- Responsive: desktop (1920x1080), tablet (1024x768), mobile (375x667)
- No IE support

**NFR-009: Backend**

- Java: 17 LTS or higher
- PostgreSQL: 15 or higher
- OS: Linux (Ubuntu 22.04 LTS recommended)

---

### 11.4 Maintainability

**NFR-010: Code Quality**

- Code coverage: minimum 70% (unit tests)
- Code review: all changes peer-reviewed
- Documentation: inline comments, README, API docs
- Version control: Git (feature branch workflow)

**NFR-011: Monitoring**

- Application logs: centralized, searchable
- Error tracking: automated alerts for exceptions
- Performance monitoring: response times, resource usage
- Uptime monitoring: external service (UptimeRobot)

---

### 11.5 Reliability

**NFR-012: Data Integrity**

- Database transactions: ACID compliance
- No data loss in case of server failure
- Automatic retry for failed notifications
- Data validation at multiple layers

**NFR-013: Error Handling**

- Graceful degradation (offline mode for mobile)
- User-friendly error messages
- Automatic error reporting to developers
- Fallback mechanisms for external services

---

## 12. Implementation Plan

### 12.1 Development Phases

#### **Phase 1.1: Backend Foundation (Weeks 1-2)**

**Week 1:**

- Set up development environment
- Initialize Spring Boot project
- Configure PostgreSQL database
- Create database schema
- Set up Git repository

**Week 2:**

- Implement user authentication (JWT)
- Create User entity and repository
- Implement login/logout endpoints
- Implement password reset flow
- Create base service and controller classes

**Deliverables:**

- Working authentication system
- Database schema deployed
- API documentation (Swagger) basic setup

---

#### **Phase 1.2: Cash Requisition Module (Weeks 3-4)**

**Week 3:**

- Create Requisition entity, repository, service
- Implement CRUD endpoints
- Auto-generate requisition numbers
- Implement approval workflow logic
- Create notification service (email)

**Week 4:**

- Implement disbursement logic
- Add filters and search
- Add pagination
- Implement role-based access control
- Unit tests for requisition module

**Deliverables:**

- Complete requisition API endpoints
- Working approval workflow
- Email notifications

---

#### **Phase 1.3: Retirement Module (Weeks 5-6)**

**Week 5:**

- Create Retirement, LineItem, Attachment entities
- Implement retirement CRUD endpoints
- Implement file upload handling
- Create file storage directory structure
- Implement auto-calculations (amount remaining)

**Week 6:**

- Implement retirement approval/rejection
- Link requisitions to retirements
- Add validations (line items, receipts)
- SMS notification integration (Africa's Talking)
- Unit tests for retirement module

**Deliverables:**

- Complete retirement API endpoints
- File upload working
- SMS notifications integrated

---

#### **Phase 1.4: Mobile App Core (Weeks 7-8)**

**Week 7:**

- Initialize Flutter project
- Create app structure (screens, navigation)
- Implement authentication screens (login)
- Implement API service layer
- Create home dashboard

**Week 8:**

- Implement requisition screens (list, create, details)
- Implement approval screens
- Integrate with backend API
- Handle loading states and errors
- Basic offline caching (view cached data)

**Deliverables:**

- Working mobile app (requisition module)
- Authentication flow
- API integration

---

#### **Phase 1.5: Mobile App Complete (Weeks 9-10)**

**Week 9:**

- Implement retirement screens (create, list, details)
- Camera integration for receipt photos
- Image picker for gallery
- File upload to backend
- Implement notifications

**Week 10:**

- Polish UI/UX
- Add loading indicators and animations
- Implement pull-to-refresh
- Error handling and retry logic
- Offline mode improvements

**Deliverables:**

- Complete mobile app (all features)
- Receipt upload working
- Push notifications

---

#### **Phase 1.6: Testing & Deployment (Weeks 11-12)**

**Week 11:**

- Integration testing (end-to-end flows)
- Fix bugs from testing
- Performance testing and optimization
- Security testing (basic penetration testing)
- Prepare deployment environment (VPS setup, domain, SSL)

**Week 12:**

- Deploy backend to production
- Deploy mobile app (internal testing via APK)
- Pilot user onboarding and training
- Monitor and fix issues
- Collect user feedback

**Deliverables:**

- Production deployment
- Pilot testing with 5-10 users
- Documentation (user guide, admin guide)

---

### 12.2 Development Environment Setup

**Required Tools:**

- **IDE:** IntelliJ IDEA / VS Code
- **Database:** PostgreSQL (local + production)
- **API Testing:** Postman / Insomnia
- **Version Control:** Git + GitHub/GitLab
- **Mobile Development:** Android Studio (for Flutter)
- **Design:** Figma (for UI mockups, optional)

**Local Development:**

- Backend runs on localhost:8080
- Database on localhost:5432
- Mobile app connects to local backend (or staging)

**Staging Environment:**

- Separate server for testing
- Staging database (copy of production data)
- Used for pre-production testing

**Production Environment:**

- Cloud VPS (DigitalOcean Droplet, 2GB RAM minimum)
- PostgreSQL database
- Nginx reverse proxy
- SSL certificate (Let's Encrypt)
- Domain: api.apeckdigital.co.tz

---

### 12.3 Testing Strategy

**Unit Testing:**

- Backend: JUnit 5, Mockito
- Coverage target: 70%
- Test all business logic in services
- Test validations and calculations

**Integration Testing:**

- Test API endpoints with real database
- Test authentication and authorization
- Test file upload/download
- Test notification sending

**User Acceptance Testing (UAT):**

- Pilot users test with real scenarios
- Test on actual devices (various Android phones)
- Collect feedback on usability
- Identify edge cases and bugs

**Performance Testing:**

- Load testing with JMeter (simulate 50 concurrent users)
- Test response times under load
- Test file upload with large files
- Optimize slow queries

---

### 12.4 Deployment Strategy

**Backend Deployment:**

1. Build JAR file: `mvn clean package`
2. Copy to server: `scp target/app.jar user@server:/app/`
3. Run with systemd service
4. Configure Nginx reverse proxy
5. Set up SSL with Let's Encrypt

**Database Migration:**

- Use Flyway or Liquibase for schema versioning
- Test migrations on staging first
- Backup before running migrations in production

**Mobile App Deployment:**

- Build APK: `flutter build apk --release`
- Distribute via internal link (Google Drive, Dropbox)
- For pilot: share APK directly
- Future: publish to Google Play Store (internal testing track)

**Rollback Plan:**

- Keep previous JAR version
- Database backup before deployment
- Ability to roll back within 1 hour if issues

---

### 12.5 Post-Deployment

**Monitoring:**

- Set up application logging (log files + console)
- Set up error tracking (Sentry or similar)
- Monitor server resources (CPU, RAM, disk)
- Set up uptime monitoring

**User Support:**

- Create user guide (PDF + video tutorials)
- Admin guide for finance/approvers
- Support channel: WhatsApp group for pilot users
- Response time: within 4 hours for critical issues

**Feedback Collection:**

- Weekly check-ins with pilot users (first month)
- In-app feedback form
- Track metrics: adoption rate, time saved, error rate
- Iterate based on feedback

**Performance Tuning:**

- Monitor slow queries and optimize
- Add database indexes where needed
- Optimize image compression for uploads
- Cache frequently accessed data

---

### 12.6 Success Metrics (Phase 1)

**Adoption Metrics:**

- 80% of field staff using app within 1 month
- 90% of requisitions submitted digitally within 2 months
- 100% retirement submissions digital within 2 months

**Efficiency Metrics:**

- Average requisition submission time: < 3 minutes
- Average approval time: < 24 hours (down from 2-3 days)
- Zero lost forms
- 100% reconciliation accuracy

**User Satisfaction:**

- User satisfaction score: > 8/10
- Net Promoter Score: > 50
- Less than 5% of transactions still on paper after 2 months

**Technical Metrics:**

- API uptime: > 99%
- API response time: < 500ms (p95)
- App crash rate: < 1%
- Zero critical security incidents

---

## 13. Risks & Mitigation

### 13.1 Technical Risks

**Risk 1: Poor Internet Connectivity in Field**

- **Impact:** HIGH
- **Probability:** MEDIUM
- **Mitigation:**
  - Implement offline mode (cache data, queue submissions)
  - SMS notifications as fallback
  - Optimize app size and data usage
  - Provide option to use web on HQ WiFi

**Risk 2: User Adoption Resistance**

- **Impact:** HIGH
- **Probability:** MEDIUM
- **Mitigation:**
  - Thorough training sessions
  - Simple, intuitive UI
  - Run parallel with paper for transition period
  - Identify and train "champions" in each department
  - Collect and act on feedback quickly

**Risk 3: Data Loss or Corruption**

- **Impact:** CRITICAL
- **Probability:** LOW
- **Mitigation:**
  - Daily automated backups
  - Database transaction integrity (ACID)
  - Extensive testing before production
  - Quick rollback capability

**Risk 4: Security Breach**

- **Impact:** CRITICAL
- **Probability:** LOW
- **Mitigation:**
  - HTTPS for all communication
  - JWT authentication, bcrypt passwords
  - Regular security updates
  - Role-based access control
  - Audit logging

---

### 13.2 Business Risks

**Risk 5: Development Delays**

- **Impact:** MEDIUM
- **Probability:** MEDIUM
- **Mitigation:**
  - Buffer time in schedule (12 weeks, assume 14)
  - Focus on MVP features first
  - Weekly progress reviews
  - Clear priorities and scope

**Risk 6: Insufficient Budget for Infrastructure**

- **Impact:** MEDIUM
- **Probability:** LOW
- **Mitigation:**
  - Start with minimal infrastructure (~$30/month)
  - Scale up as user base grows
  - Negotiate development compensation upfront
  - Clearly communicate ongoing costs

---

## 14. Future Enhancements (Post-Phase 1)

**Short-term (Phase 2):**

- iOS app version
- Advanced reporting and analytics
- Budget tracking and alerts
- Multiple approval levels (if needed)
- Department-specific workflows

**Medium-term (Phase 3-5):**

- Inventory management (Kitabu cha Leja)
- Logistics (Delivery Notes)
- Purchase management
- Sales invoicing
- Integration with accounting system

**Long-term (Phase 6+):**

- Full ERP system
- HR and payroll module
- Asset management
- Project management and job costing
- Business intelligence dashboards
- API for third-party integrations

---

## 15. Appendices

### Appendix A: Glossary

- **Cash Requisition:** Request for cash advance from employee
- **Expenditure Retirement:** Accounting for how cash advance was spent
- **Disbursement:** Act of giving money to requester
- **Line Item:** Individual expense entry in retirement
- **Receipt:** Proof of purchase (photo or PDF)
- **JWT:** JSON Web Token (authentication method)
- **API:** Application Programming Interface
- **CRUD:** Create, Read, Update, Delete

### Appendix B: API Endpoint Summary

_(Full API documentation in Swagger after implementation)_

**Authentication:**

- POST /api/auth/login
- POST /api/auth/logout
- POST /api/auth/forgot-password
- POST /api/auth/reset-password

**Requisitions:**

- GET /api/requisitions
- GET /api/requisitions/{id}
- POST /api/requisitions
- PUT /api/requisitions/{id}/approve
- PUT /api/requisitions/{id}/reject
- PUT /api/requisitions/{id}/disburse

**Retirements:**

- GET /api/retirements
- GET /api/retirements/{id}
- POST /api/retirements
- PUT /api/retirements/{id}/approve
- PUT /api/retirements/{id}/reject
- POST /api/retirements/{id}/attachments
- DELETE /api/retirements/attachments/{id}

**Notifications:**

- GET /api/notifications
- PUT /api/notifications/{id}/read
- PUT /api/notifications/read-all

**Users (Admin):**

- GET /api/users
- GET /api/users/{id}
- POST /api/users
- PUT /api/users/{id}
- DELETE /api/users/{id}

### Appendix C: Database Backup Script

```bash
#!/bin/bash
# Daily backup script
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/backups/postgres"
DB_NAME="apeck_digital"

pg_dump -U postgres $DB_NAME | gzip > $BACKUP_DIR/backup_$DATE.sql.gz

# Keep only last 30 days
find $BACKUP_DIR -name "backup_*.sql.gz" -mtime +30 -delete
```

### Appendix D: Contact Information

**Project Owner:** APECK International Limited  
**Developer:** [Your Name]  
**Email:** [your-email@apeckinternational.com]  
**Phone:** [your-phone]  
**Project Start:** [Date]  
**Expected Completion:** [Date + 12 weeks]

---

## Document Control

**Version:** 1.0  
**Date:** January 26, 2024  
**Author:** APECK IT Department  
**Status:** Draft / For Review  
**Next Review:** After Phase 1 Completion

**Change Log:**
| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 26-Jan-2024 | IT Dept | Initial design document |

---

**END OF DESIGN DOCUMENTATION**

---

This comprehensive design document should serve as my complete blueprint for Phase 1. I can:

1. Share this with management as part of my proposal
2. I will use it to guide development
3. Reference it during user training
4. Update it as I build and learn
