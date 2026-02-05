# Database Schema Diagram - APECK Digital Operations System

## Phase 1: Cash Management Module

---

## Entity Relationship Diagram (ERD)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                                                                 │
│                            APECK DIGITAL OPERATIONS                             │
│                          DATABASE SCHEMA - PHASE 1                              │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘


┌────────────────────────────────────┐
│            USERS                   │
├────────────────────────────────────┤
│ PK  id                BIGSERIAL    │
│     name              VARCHAR(255) │
│ UK  email             VARCHAR(255) │
│     phone             VARCHAR(20)  │
│     role              VARCHAR(50)  │
│     department        VARCHAR(100) │
│     password_hash     VARCHAR(255) │
│     is_active         BOOLEAN      │
│     created_at        TIMESTAMP    │
│     updated_at        TIMESTAMP    │
└────────────┬───────────────────────┘
             │
             │ requested_by (FK)
             │ approved_by (FK)
             │ disbursed_by (FK)
             │
             ├──────────────────────────────────────────────────────┐
             │                                                      │
             │                                                      │
┌────────────▼──────────────────────────────┐                     │
│      CASH_REQUISITIONS                    │                     │
├───────────────────────────────────────────┤                     │
│ PK  id                     BIGSERIAL      │                     │
│ UK  requisition_number     VARCHAR(50)    │                     │
│     date                   DATE           │                     │
│     amount_requested       DECIMAL(15,2)  │                     │
│     department             VARCHAR(100)   │                     │
│     description            TEXT           │                     │
│ FK  requested_by           BIGINT         │─────────────────────┘
│     status                 VARCHAR(20)    │
│ FK  approved_by            BIGINT         │─────────────────────┐
│     approved_at            TIMESTAMP      │                     │
│     approved_amount        DECIMAL(15,2)  │                     │
│ FK  disbursed_by           BIGINT         │───────────┐         │
│     disbursed_at           TIMESTAMP      │           │         │
│     payment_method         VARCHAR(20)    │           │         │
│     payment_reference      VARCHAR(100)   │           │         │
│     rejection_reason       TEXT           │           │         │
│     created_at             TIMESTAMP      │           │         │
│     updated_at             TIMESTAMP      │           │         │
└────────────┬──────────────────────────────┘           │         │
             │                                           │         │
             │ requisition_id (FK)                       │         │
             │                                           │         │
             │                                           │         │
┌────────────▼──────────────────────────────────┐       │         │
│      EXPENDITURE_RETIREMENTS                  │       │         │
├───────────────────────────────────────────────┤       │         │
│ PK  id                     BIGSERIAL          │       │         │
│ UK  retirement_number      VARCHAR(50)        │       │         │
│ FK  requisition_id         BIGINT             │───────┘         │
│     employee_name          VARCHAR(255)       │                 │
│     employee_title         VARCHAR(100)       │                 │
│     amount_received        DECIMAL(15,2)      │                 │
│     amount_expensed        DECIMAL(15,2)      │                 │
│     amount_remaining       DECIMAL(15,2)      │ ◄─ COMPUTED     │
│ FK  submitted_by           BIGINT             │─────────────────┤
│     submitted_at           TIMESTAMP          │                 │
│     status                 VARCHAR(20)        │                 │
│ FK  finance_approved_by    BIGINT             │─────────────────┘
│     finance_approved_at    TIMESTAMP          │
│     finance_notes          TEXT               │
│     created_at             TIMESTAMP          │
│     updated_at             TIMESTAMP          │
└────────────┬───────────────┬──────────────────┘
             │               │
             │               │
             │               └────────────────────────────────────────┐
             │                                                        │
             │ retirement_id (FK)                                    │
             │                                                        │
             │                                                        │
┌────────────▼────────────────────────────┐    ┌────────────────────▼──────────┐
│    RETIREMENT_LINE_ITEMS                │    │   RETIREMENT_ATTACHMENTS      │
├─────────────────────────────────────────┤    ├───────────────────────────────┤
│ PK  id               BIGSERIAL          │    │ PK  id            BIGSERIAL   │
│ FK  retirement_id    BIGINT             │    │ FK  retirement_id BIGINT      │
│     date             DATE                │    │     file_name     VARCHAR(255)│
│     description      TEXT                │    │     file_path     VARCHAR(500)│
│     cost             DECIMAL(15,2)       │    │     file_type     VARCHAR(10) │
│     created_at       TIMESTAMP           │    │     file_size     BIGINT      │
└─────────────────────────────────────────┘    │     uploaded_at   TIMESTAMP   │
                                                └───────────────────────────────┘


             ┌──────────────────────────────────────────┐
             │                                          │
             │ user_id (FK)                             │
             │                                          │
┌────────────▼────────────────────────────────┐        │
│         NOTIFICATIONS                       │        │
├─────────────────────────────────────────────┤        │
│ PK  id              BIGSERIAL               │        │
│ FK  user_id         BIGINT                  │────────┘
│     type            VARCHAR(50)             │
│     message         TEXT                    │
│     related_id      BIGINT                  │
│     related_type    VARCHAR(20)             │
│     read            BOOLEAN                 │
│     created_at      TIMESTAMP               │
└─────────────────────────────────────────────┘


┌────────────────────────────────────────────────────────────────────┐
│                          LEGEND                                    │
├────────────────────────────────────────────────────────────────────┤
│  PK  = Primary Key                                                 │
│  FK  = Foreign Key                                                 │
│  UK  = Unique Key                                                  │
│  ─── = One-to-Many Relationship                                    │
│  ──► = Foreign Key Reference                                       │
└────────────────────────────────────────────────────────────────────┘
```

---

## Relationship Details

### 1. **USERS → CASH_REQUISITIONS**

**Type:** One-to-Many (1:N)

**Relationships:**

- **requested_by:** One user can create multiple requisitions
- **approved_by:** One user (approver) can approve multiple requisitions
- **disbursed_by:** One user (finance) can disburse multiple requisitions

```sql
-- Foreign Key Constraints
ALTER TABLE cash_requisitions
  ADD CONSTRAINT fk_requisitions_requested_by
  FOREIGN KEY (requested_by) REFERENCES users(id);

ALTER TABLE cash_requisitions
  ADD CONSTRAINT fk_requisitions_approved_by
  FOREIGN KEY (approved_by) REFERENCES users(id);

ALTER TABLE cash_requisitions
  ADD CONSTRAINT fk_requisitions_disbursed_by
  FOREIGN KEY (disbursed_by) REFERENCES users(id);
```

---

### 2. **CASH_REQUISITIONS → EXPENDITURE_RETIREMENTS**

**Type:** One-to-One (1:1)

**Relationship:**

- One requisition can have exactly one retirement
- One retirement links to exactly one requisition

```sql
-- Foreign Key Constraint
ALTER TABLE expenditure_retirements
  ADD CONSTRAINT fk_retirements_requisition
  FOREIGN KEY (requisition_id) REFERENCES cash_requisitions(id);

-- Unique constraint ensures 1:1 relationship
ALTER TABLE expenditure_retirements
  ADD CONSTRAINT uk_retirements_requisition
  UNIQUE (requisition_id);
```

---

### 3. **USERS → EXPENDITURE_RETIREMENTS**

**Type:** One-to-Many (1:N)

**Relationships:**

- **submitted_by:** One user can submit multiple retirements
- **finance_approved_by:** One finance user can approve multiple retirements

```sql
-- Foreign Key Constraints
ALTER TABLE expenditure_retirements
  ADD CONSTRAINT fk_retirements_submitted_by
  FOREIGN KEY (submitted_by) REFERENCES users(id);

ALTER TABLE expenditure_retirements
  ADD CONSTRAINT fk_retirements_approved_by
  FOREIGN KEY (finance_approved_by) REFERENCES users(id);
```

---

### 4. **EXPENDITURE_RETIREMENTS → RETIREMENT_LINE_ITEMS**

**Type:** One-to-Many (1:N)

**Relationship:**

- One retirement can have multiple line items (expenses)
- Each line item belongs to exactly one retirement

```sql
-- Foreign Key Constraint with CASCADE DELETE
ALTER TABLE retirement_line_items
  ADD CONSTRAINT fk_line_items_retirement
  FOREIGN KEY (retirement_id) REFERENCES expenditure_retirements(id)
  ON DELETE CASCADE;
```

**Note:** CASCADE DELETE means if a retirement is deleted, all its line items are automatically deleted.

---

### 5. **EXPENDITURE_RETIREMENTS → RETIREMENT_ATTACHMENTS**

**Type:** One-to-Many (1:N)

**Relationship:**

- One retirement can have multiple attachments (receipts)
- Each attachment belongs to exactly one retirement

```sql
-- Foreign Key Constraint with CASCADE DELETE
ALTER TABLE retirement_attachments
  ADD CONSTRAINT fk_attachments_retirement
  FOREIGN KEY (retirement_id) REFERENCES expenditure_retirements(id)
  ON DELETE CASCADE;
```

---

### 6. **USERS → NOTIFICATIONS**

**Type:** One-to-Many (1:N)

**Relationship:**

- One user can receive multiple notifications
- Each notification belongs to exactly one user

```sql
-- Foreign Key Constraint
ALTER TABLE notifications
  ADD CONSTRAINT fk_notifications_user
  FOREIGN KEY (user_id) REFERENCES users(id);
```

---

## Visual Cardinality Diagram

```
                    1                            N
        USERS ─────────────────────────► CASH_REQUISITIONS
         │                                      │ 1
         │ 1                                    │
         │                                      │
         │                                      │ 1
         │                              EXPENDITURE_RETIREMENTS
         │ 1                                    │
         │                                      │
         │                             ┌────────┴────────┐
         │                             │                 │
         │                             │ 1               │ 1
         │                             │                 │
         │                             ▼ N               ▼ N
         │                    RETIREMENT_LINE_ITEMS    RETIREMENT_ATTACHMENTS
         │
         │ 1
         │
         ▼ N
    NOTIFICATIONS
```

**Legend:**

- **1** = One (parent side)
- **N** = Many (child side)

---

## Detailed Table Structures

### TABLE: users

```
┌──────────────────┬──────────────┬──────────────┬──────────────┐
│ Column Name      │ Data Type    │ Constraints  │ Description  │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ id               │ BIGSERIAL    │ PRIMARY KEY  │ Auto-increment ID │
│ name             │ VARCHAR(255) │ NOT NULL     │ Full name │
│ email            │ VARCHAR(255) │ UNIQUE, NOT NULL │ Login email │
│ phone            │ VARCHAR(20)  │              │ Phone number │
│ role             │ VARCHAR(50)  │ NOT NULL     │ User role │
│ department       │ VARCHAR(100) │              │ Department name │
│ password_hash    │ VARCHAR(255) │ NOT NULL     │ Bcrypt hash │
│ is_active        │ BOOLEAN      │ DEFAULT true │ Account status │
│ created_at       │ TIMESTAMP    │ DEFAULT now()│ Creation date │
│ updated_at       │ TIMESTAMP    │ DEFAULT now()│ Last update │
└──────────────────┴──────────────┴──────────────┴──────────────┘

INDEXES:
  - idx_users_email ON users(email)
  - idx_users_role ON users(role)

ROLES (ENUM):
  - field_staff
  - approver
  - finance
  - admin
```

---

### TABLE: cash_requisitions

```
┌──────────────────────┬──────────────┬──────────────┬──────────────┐
│ Column Name          │ Data Type    │ Constraints  │ Description  │
├──────────────────────┼──────────────┼──────────────┼──────────────┤
│ id                   │ BIGSERIAL    │ PRIMARY KEY  │ Auto-increment │
│ requisition_number   │ VARCHAR(50)  │ UNIQUE, NOT NULL │ REQ-YYYY-NNNN │
│ date                 │ DATE         │ NOT NULL     │ Request date │
│ amount_requested     │ DECIMAL(15,2)│ NOT NULL     │ Amount in TZS │
│ department           │ VARCHAR(100) │              │ Department │
│ description          │ TEXT         │ NOT NULL     │ Purpose │
│ requested_by         │ BIGINT       │ FK→users.id  │ Requester │
│ status               │ VARCHAR(20)  │ NOT NULL     │ Workflow status │
│ approved_by          │ BIGINT       │ FK→users.id  │ Approver │
│ approved_at          │ TIMESTAMP    │              │ Approval time │
│ approved_amount      │ DECIMAL(15,2)│              │ Approved TZS │
│ disbursed_by         │ BIGINT       │ FK→users.id  │ Finance user │
│ disbursed_at         │ TIMESTAMP    │              │ Disbursement time │
│ payment_method       │ VARCHAR(20)  │              │ Payment type │
│ payment_reference    │ VARCHAR(100) │              │ Reference # │
│ rejection_reason     │ TEXT         │              │ Why rejected │
│ created_at           │ TIMESTAMP    │ DEFAULT now()│ Created time │
│ updated_at           │ TIMESTAMP    │ DEFAULT now()│ Updated time │
└──────────────────────┴──────────────┴──────────────┴──────────────┘

INDEXES:
  - idx_requisitions_status ON cash_requisitions(status)
  - idx_requisitions_requested_by ON cash_requisitions(requested_by)
  - idx_requisitions_date ON cash_requisitions(date)

STATUS (ENUM):
  - pending
  - approved
  - rejected
  - disbursed

PAYMENT_METHOD (ENUM):
  - mobile_money
  - bank
  - cash
  - cheque
```

---

### TABLE: expenditure_retirements

```
┌──────────────────────┬──────────────┬──────────────┬──────────────┐
│ Column Name          │ Data Type    │ Constraints  │ Description  │
├──────────────────────┼──────────────┼──────────────┼──────────────┤
│ id                   │ BIGSERIAL    │ PRIMARY KEY  │ Auto-increment │
│ retirement_number    │ VARCHAR(50)  │ UNIQUE, NOT NULL │ RET-YYYY-NNNN │
│ requisition_id       │ BIGINT       │ FK→requisitions.id, UNIQUE │ Linked req │
│ employee_name        │ VARCHAR(255) │ NOT NULL     │ Employee name │
│ employee_title       │ VARCHAR(100) │              │ Job title │
│ amount_received      │ DECIMAL(15,2)│ NOT NULL     │ Cash received │
│ amount_expensed      │ DECIMAL(15,2)│ NOT NULL     │ Total spent │
│ amount_remaining     │ DECIMAL(15,2)│ COMPUTED     │ Auto-calculated │
│ submitted_by         │ BIGINT       │ FK→users.id  │ Submitter │
│ submitted_at         │ TIMESTAMP    │ DEFAULT now()│ Submit time │
│ status               │ VARCHAR(20)  │ NOT NULL     │ Review status │
│ finance_approved_by  │ BIGINT       │ FK→users.id  │ Finance user │
│ finance_approved_at  │ TIMESTAMP    │              │ Approval time │
│ finance_notes        │ TEXT         │              │ Feedback │
│ created_at           │ TIMESTAMP    │ DEFAULT now()│ Created time │
│ updated_at           │ TIMESTAMP    │ DEFAULT now()│ Updated time │
└──────────────────────┴──────────────┴──────────────┴──────────────┘

INDEXES:
  - idx_retirements_status ON expenditure_retirements(status)
  - idx_retirements_requisition ON expenditure_retirements(requisition_id)
  - idx_retirements_submitted_by ON expenditure_retirements(submitted_by)

STATUS (ENUM):
  - pending
  - approved
  - rejected

COMPUTED COLUMN:
  amount_remaining = amount_received - amount_expensed
```

---

### TABLE: retirement_line_items

```
┌──────────────────┬──────────────┬──────────────┬──────────────┐
│ Column Name      │ Data Type    │ Constraints  │ Description  │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ id               │ BIGSERIAL    │ PRIMARY KEY  │ Auto-increment │
│ retirement_id    │ BIGINT       │ FK→retirements.id │ Parent retirement │
│ date             │ DATE         │ NOT NULL     │ Expense date │
│ description      │ TEXT         │ NOT NULL     │ What was bought │
│ cost             │ DECIMAL(15,2)│ NOT NULL     │ Cost in TZS │
│ created_at       │ TIMESTAMP    │ DEFAULT now()│ Created time │
└──────────────────┴──────────────┴──────────────┴──────────────┘

INDEXES:
  - idx_line_items_retirement ON retirement_line_items(retirement_id)

CASCADE:
  - ON DELETE CASCADE (deleted when parent retirement deleted)
```

---

### TABLE: retirement_attachments

```
┌──────────────────┬──────────────┬──────────────┬──────────────┐
│ Column Name      │ Data Type    │ Constraints  │ Description  │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ id               │ BIGSERIAL    │ PRIMARY KEY  │ Auto-increment │
│ retirement_id    │ BIGINT       │ FK→retirements.id │ Parent retirement │
│ file_name        │ VARCHAR(255) │ NOT NULL     │ Original name │
│ file_path        │ VARCHAR(500) │ NOT NULL     │ Server path │
│ file_type        │ VARCHAR(10)  │ NOT NULL     │ jpg/png/pdf │
│ file_size        │ BIGINT       │ NOT NULL     │ Bytes │
│ uploaded_at      │ TIMESTAMP    │ DEFAULT now()│ Upload time │
└──────────────────┴──────────────┴──────────────┴──────────────┘

INDEXES:
  - idx_attachments_retirement ON retirement_attachments(retirement_id)

FILE_TYPE (ENUM):
  - jpg
  - jpeg
  - png
  - pdf

CASCADE:
  - ON DELETE CASCADE (deleted when parent retirement deleted)
```

---

### TABLE: notifications

```
┌──────────────────┬──────────────┬──────────────┬──────────────┐
│ Column Name      │ Data Type    │ Constraints  │ Description  │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ id               │ BIGSERIAL    │ PRIMARY KEY  │ Auto-increment │
│ user_id          │ BIGINT       │ FK→users.id  │ Recipient │
│ type             │ VARCHAR(50)  │ NOT NULL     │ Notification type │
│ message          │ TEXT         │ NOT NULL     │ Message content │
│ related_id       │ BIGINT       │ NOT NULL     │ Req/Ret ID │
│ related_type     │ VARCHAR(20)  │ NOT NULL     │ 'requisition' or 'retirement' │
│ read             │ BOOLEAN      │ DEFAULT false│ Read status │
│ created_at       │ TIMESTAMP    │ DEFAULT now()│ Created time │
└──────────────────┴──────────────┴──────────────┴──────────────┘

INDEXES:
  - idx_notifications_user ON notifications(user_id)
  - idx_notifications_read ON notifications(read)
  - idx_notifications_created ON notifications(created_at DESC)

TYPE (ENUM):
  - requisition_submitted
  - requisition_approved
  - requisition_rejected
  - funds_disbursed
  - retirement_submitted
  - retirement_approved
  - retirement_rejected
```

---

## Data Flow Diagram

### Flow 1: Cash Requisition Workflow

```
┌─────────────┐
│  Field Staff│
│   (User)    │
└──────┬──────┘
       │
       │ 1. Creates requisition
       │
       ▼
┌─────────────────────────┐
│  CASH_REQUISITIONS      │
│  status: 'pending'      │
│  requested_by: user_id  │
└──────┬──────────────────┘
       │
       │ 2. Notifies approver
       │
       ▼
┌─────────────┐
│  NOTIFICATIONS │
│  type: 'req_submitted' │
└─────────────┘
       │
       │ 3. Approver reviews
       │
       ▼
┌─────────────────────────┐
│  CASH_REQUISITIONS      │
│  status: 'approved'     │
│  approved_by: approver_id│
│  approved_at: timestamp │
└──────┬──────────────────┘
       │
       │ 4. Finance disburses
       │
       ▼
┌─────────────────────────┐
│  CASH_REQUISITIONS      │
│  status: 'disbursed'    │
│  disbursed_by: finance_id│
│  payment_method: 'mobile'│
└─────────────────────────┘
```

---

### Flow 2: Expenditure Retirement Workflow

```
┌─────────────────────────┐
│  CASH_REQUISITIONS      │
│  status: 'disbursed'    │
│  id: 123                │
└──────┬──────────────────┘
       │
       │ 1. Field staff retires
       │
       ▼
┌────────────────────────────────┐
│  EXPENDITURE_RETIREMENTS       │
│  requisition_id: 123           │
│  amount_received: from req     │
│  status: 'pending'             │
└──────┬───────────────┬─────────┘
       │               │
       │               │ 2. Add expenses
       │               │
       ▼               ▼
┌──────────────┐  ┌──────────────────┐
│ LINE_ITEMS   │  │  ATTACHMENTS     │
│ - date       │  │  - file_name     │
│ - description│  │  - file_path     │
│ - cost       │  │  - file_type     │
└──────────────┘  └──────────────────┘
       │
       │ 3. Finance reviews
       │
       ▼
┌────────────────────────────────┐
│  EXPENDITURE_RETIREMENTS       │
│  status: 'approved'            │
│  finance_approved_by: user_id  │
│  finance_approved_at: timestamp│
└────────────────────────────────┘
```

---

## Sample Data Relationships

### Example: Complete Transaction Flow

```
USER: John Mwanza (id: 5, role: field_staff)
  │
  ├─► CASH_REQUISITION (id: 101)
  │   ├─ requisition_number: REQ-2024-0101
  │   ├─ date: 2024-01-20
  │   ├─ amount_requested: 100,000
  │   ├─ department: Operations
  │   ├─ requested_by: 5 (John)
  │   ├─ status: disbursed
  │   ├─ approved_by: 3 (Sarah - Manager)
  │   ├─ approved_amount: 100,000
  │   └─ disbursed_by: 2 (Grace - Finance)
  │
  └─► EXPENDITURE_RETIREMENT (id: 51)
      ├─ retirement_number: RET-2024-0051
      ├─ requisition_id: 101
      ├─ amount_received: 100,000
      ├─ amount_expensed: 85,000
      ├─ amount_remaining: 15,000
      ├─ submitted_by: 5 (John)
      ├─ status: approved
      ├─ finance_approved_by: 2 (Grace)
      │
      ├─► LINE_ITEMS:
      │   ├─ Item 1: date: 2024-01-21, desc: "Fuel", cost: 45,000
      │   ├─ Item 2: date: 2024-01-21, desc: "Meals", cost: 15,000
      │   └─ Item 3: date: 2024-01-22, desc: "Lodging", cost: 25,000
      │
      └─► ATTACHMENTS:
          ├─ File 1: fuel_receipt.jpg (1.2MB)
          ├─ File 2: meal_receipt.jpg (850KB)
          └─ File 3: hotel_invoice.pdf (2.1MB)

NOTIFICATIONS for John (user_id: 5):
  ├─ "Your requisition REQ-2024-0101 has been approved"
  ├─ "Funds of TZS 100,000 have been disbursed"
  └─ "Your retirement RET-2024-0051 has been approved"
```

---

## Database Constraints Summary

### Primary Keys (PK)

- All tables have auto-increment `id` as primary key
- Uses BIGSERIAL for large-scale growth

### Unique Constraints (UK)

- `users.email` - No duplicate emails
- `cash_requisitions.requisition_number` - Unique req numbers
- `expenditure_retirements.retirement_number` - Unique ret numbers
- `expenditure_retirements.requisition_id` - One retirement per requisition

### Foreign Keys (FK)

```
cash_requisitions.requested_by    → users.id
cash_requisitions.approved_by     → users.id
cash_requisitions.disbursed_by    → users.id

expenditure_retirements.requisition_id      → cash_requisitions.id
expenditure_retirements.submitted_by        → users.id
expenditure_retirements.finance_approved_by → users.id

retirement_line_items.retirement_id    → expenditure_retirements.id (CASCADE)
retirement_attachments.retirement_id   → expenditure_retirements.id (CASCADE)

notifications.user_id → users.id
```

### Check Constraints

```sql
ALTER TABLE cash_requisitions
  ADD CONSTRAINT chk_amount_positive
  CHECK (amount_requested > 0);

ALTER TABLE cash_requisitions
  ADD CONSTRAINT chk_approved_amount_positive
  CHECK (approved_amount IS NULL OR approved_amount > 0);

ALTER TABLE retirement_line_items
  ADD CONSTRAINT chk_cost_positive
  CHECK (cost > 0);

ALTER TABLE retirement_attachments
  ADD CONSTRAINT chk_file_size
  CHECK (file_size > 0 AND file_size <= 5242880); -- 5MB in bytes
```

---

## Indexes for Performance

### Essential Indexes

```sql
-- Users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(is_active) WHERE is_active = true;

-- Cash Requisitions
CREATE INDEX idx_requisitions_status ON cash_requisitions(status);
CREATE INDEX idx_requisitions_requested_by ON cash_requisitions(requested_by);
CREATE INDEX idx_requisitions_date ON cash_requisitions(date DESC);
CREATE INDEX idx_requisitions_department ON cash_requisitions(department);

-- Expenditure Retirements
CREATE INDEX idx_retirements_status ON expenditure_retirements(status);
CREATE INDEX idx_retirements_requisition ON expenditure_retirements(requisition_id);
CREATE INDEX idx_retirements_submitted_by ON expenditure_retirements(submitted_by);

-- Line Items
CREATE INDEX idx_line_items_retirement ON retirement_line_items(retirement_id);

-- Attachments
CREATE INDEX idx_attachments_retirement ON retirement_attachments(retirement_id);

-- Notifications
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_read ON notifications(read) WHERE read = false;
CREATE INDEX idx_notifications_created ON notifications(created_at DESC);
```

---

## Complete SQL Schema Script

```sql
-- ================================================
-- APECK DIGITAL OPERATIONS - DATABASE SCHEMA
-- Phase 1: Cash Management Module
-- Version: 1.0
-- Date: January 26, 2024
-- ================================================

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS retirement_attachments CASCADE;
DROP TABLE IF EXISTS retirement_line_items CASCADE;
DROP TABLE IF EXISTS expenditure_retirements CASCADE;
DROP TABLE IF EXISTS cash_requisitions CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ================================================
-- TABLE: users
-- ================================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(50) NOT NULL CHECK (role IN ('field_staff', 'approver', 'finance', 'admin')),
    department VARCHAR(100),
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(is_active) WHERE is_active = true;

-- ================================================
-- TABLE: cash_requisitions
-- ================================================
CREATE TABLE cash_requisitions (
    id BIGSERIAL PRIMARY KEY,
    requisition_number VARCHAR(50) UNIQUE NOT NULL,
    date DATE NOT NULL,
    amount_requested DECIMAL(15,2) NOT NULL CHECK (amount_requested > 0),
    department VARCHAR(100),
    description TEXT NOT NULL,
    requested_by BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected', 'disbursed')),
    approved_by BIGINT REFERENCES users(id),
    approved_at TIMESTAMP,
    approved_amount DECIMAL(15,2) CHECK (approved_amount IS NULL OR approved_amount > 0),
    disbursed_by BIGINT REFERENCES users(id),
    disbursed_at TIMESTAMP,
    payment_method VARCHAR(20) CHECK (payment_method IN ('mobile_money', 'bank', 'cash', 'cheque')),
    payment_reference VARCHAR(100),
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_requisitions_status ON cash_requisitions(status);
CREATE INDEX idx_requisitions_requested_by ON cash_requisitions(requested_by);
CREATE INDEX idx_requisitions_date ON cash_requisitions(date DESC);
CREATE INDEX idx_requisitions_department ON cash_requisitions(department);

-- ================================================
-- TABLE: expenditure_retirements
-- ================================================
CREATE TABLE expenditure_retirements (
    id BIGSERIAL PRIMARY KEY,
    retirement_number VARCHAR(50) UNIQUE NOT NULL,
    requisition_id BIGINT NOT NULL UNIQUE REFERENCES cash_requisitions(id),
    employee_name VARCHAR(255) NOT NULL,
    employee_title VARCHAR(100),
    amount_received DECIMAL(15,2) NOT NULL CHECK (amount_received > 0),
    amount_expensed DECIMAL(15,2) NOT NULL CHECK (amount_expensed >= 0),
    amount_remaining DECIMAL(15,2) GENERATED ALWAYS AS (amount_received - amount_expensed) STORED,
    submitted_by BIGINT NOT NULL REFERENCES users(id),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected')),
    finance_approved_by BIGINT REFERENCES users(id),
    finance_approved_at TIMESTAMP,
    finance_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_retirements_status ON expenditure_retirements(status);
CREATE INDEX idx_retirements_requisition ON expenditure_retirements(requisition_id);
CREATE INDEX idx_retirements_submitted_by ON expenditure_retirements(submitted_by);

-- ================================================
-- TABLE: retirement_line_items
-- ================================================
CREATE TABLE retirement_line_items (
    id BIGSERIAL PRIMARY KEY,
    retirement_id BIGINT NOT NULL REFERENCES expenditure_retirements(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    description TEXT NOT NULL,
    cost DECIMAL(15,2) NOT NULL CHECK (cost > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_line_items_retirement ON retirement_line_items(retirement_id);

-- ================================================
-- TABLE: retirement_attachments
-- ================================================
CREATE TABLE retirement_attachments (
    id BIGSERIAL PRIMARY KEY,
    retirement_id BIGINT NOT NULL REFERENCES expenditure_retirements(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(10) NOT NULL CHECK (file_type IN ('jpg', 'jpeg', 'png', 'pdf')),
    file_size BIGINT NOT NULL CHECK (file_size > 0 AND file_size <= 5242880),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_attachments_retirement ON retirement_attachments(retirement_id);

-- ================================================
-- TABLE: notifications
-- ================================================
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    related_id BIGINT NOT NULL,
    related_type VARCHAR(20) NOT NULL CHECK (related_type IN ('requisition', 'retirement')),
    read BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_read ON notifications(read) WHERE read = false;
CREATE INDEX idx_notifications_created ON notifications(created_at DESC);

-- ================================================
-- SAMPLE DATA (for testing)
-- ================================================

-- Insert sample users
INSERT INTO users (name, email, phone, role, department, password_hash) VALUES
('John Mwanza', 'john.mwanza@apeck.co.tz', '+255712345678', 'field_staff', 'Operations', '$2a$12$dummyhashforjohn'),
('Grace Mbwambo', 'grace.mbwambo@apeck.co.tz', '+255712345679', 'finance', 'Finance', '$2a$12$dummyhashforgrace'),
('Sarah Komba', 'sarah.komba@apeck.co.tz', '+255712345680', 'approver', 'Operations', '$2a$12$dummyhashforsarah'),
('Dr. Amani Masoud', 'amani.masoud@apeck.co.tz', '+255712345681', 'admin', 'Management', '$2a$12$dummyhashforamani');

-- End of schema
```

---

This visual schema diagram provides a complete reference for:

1. **Database structure** - all tables and columns
2. **Relationships** - how data connects
3. **Constraints** - business rules enforced at DB level
4. **Indexes** - for query performance
5. **Sample SQL** - ready to execute

I will use this as a reference while building, will share with management to show technical depth, and refer to it during development!
