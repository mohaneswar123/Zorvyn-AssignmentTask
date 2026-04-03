# Finance Backend - Simple API Documentation

This is a basic guide for using the Finance Backend APIs.

## 1) Base URL

- Local URL: http://localhost:8089
- API prefix: /api
- Full base: http://localhost:8089/api

## 2) Authentication
For Role Based Access
Used JWT token for protected APIs.

Header format:

Authorization: Bearer <your_token>

Get token from:

- POST /api/auth/register
- POST /api/auth/login

## 3) Roles

- Viewer: dashboard only
- Analyst: dashboard + records + insights
- Admin: everything (records write + user management)

## 4) Auth APIs

### POST /api/auth/register

Request body:

{
  "username": "eswar",
  "email": "eswar@gmail.com",
  "password": "12345678",
  "phoneNumber": "99232394399"
}

Response (example):

{
  "token": "<jwt>",
  "email": "eswar@gmail.com",
  "name": "eswar",
  "role": "Viewer",
  "userId": "<userid>"
}

### POST /api/auth/login

Request body:

{
  "email": "eswar@gmail.com",
  "password": "12345678"
}

Response is same shape as register response.

## 5) Transaction APIs

### Read APIs

- GET /api/transactions/dashboard
  - Returns the recent transactions of income and expense with totals, monthly trends 
  -  transactionService.getTotalIncome(),
                transactionService.getTotalExpense(),
                transactionService.getNetBalance(),
                transactionService.getCategoryTotals(),
                transactionService.getMonthlyTrends(DEFAULT_MONTH_TREND_WINDOW),
                transactionService.getRecentTransactions(DEFAULT_DASHBOARD_LIMIT)); 
  - Roles: Viewer, Analyst, Admin

- GET /api/transactions/records
  - Returns All Transaction Records
  - transactionService.getAllTransactions();
  - Roles: Analyst, Admin


- GET /api/transactions/records/date/{date}
  - Returns All Transcations on a particular Date
  - transactionService.getTransactionsByDate(date)
  - Roles: Analyst, Admin


- GET /api/transactions/records/range?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
  - Returns All Transactions from startDate to EndDate
  - transactionService.getTransactionsByDateRange(startDate, endDate)
  - Roles: Analyst, Admin

- GET /api/transactions/records/type/{type}
  - Returns All Transactions of Specified Type (income/expense)
  - transactionService.getTransactionsByType(type)
  - Roles: Analyst, Admin

- GET /api/transactions/records/category/{category}
  - Returns All Transactions of Specified Category
  - Roles: Analyst, Admin

- GET /api/transactions/insights
  - Returns the totalIncome, TotalExpense, NetBalance, AverageIncome, AvergaeExpense.
  - transactionService.getTotalIncome(),
    transactionService.getTotalExpense(),
    transactionService.getNetBalance(),
    transactionService.getAverageIncome(),
    transactionService.getAverageExpense());
  - Roles: Analyst, Admin

### Write APIs (Admin only)

- POST /api/transactions/income
  - To Create a income record 
- POST /api/transactions/expense
  - To Create a Expense record
- PUT /api/transactions/{transactionId}
  - To update a particular Transaction
- DELETE /api/transactions/{transactionId}
  - To delete a particular Transaction

Create income/expense request body:

{
  "amount": 1000,
  "category": "Salary",
  "timestamp": "2026-04-02",
  "description": "April salary"
}

Notes:

- type is set automatically by endpoint (income or expense)
- timestamp can be omitted (current date is used)

## 6) Admin User APIs (Admin only)

- GET /api/admin/users
  - Returns all users 
- POST /api/admin/users
  - To create a user
- GET /api/admin/users/{userId}
  - Returns a particular user by userid
- GET /api/admin/users/by-email?email=user@gmail.com
  - Returns a particular user by email
- GET /api/admin/users/role/{role}
  - Returns Users of a specified Role
- GET /api/admin/users/status/{status}
  - Returns the All active users
- PUT /api/admin/users/{userId}
  - update a particular user
- PATCH /api/admin/users/{userId}/role/{role}
  - assign a role to a user
- PATCH /api/admin/users/{userId}/activate
  - Activate a particular user by userid
- PATCH /api/admin/users/{userId}/deactivate
  - deactivate a particular user by userid
- DELETE /api/admin/users/{userId}
  - delete a particular userd by userid

## 7) Quick Setup

1. Configure MongoDB URI and JWT settings in src/main/resources/application.properties
2. Run app:
   - Windows: ./mvnw spring-boot:run
   - Linux/macOS: ./mvnw spring-boot:run
3. Open APIs at http://localhost:8089/api

## 8) Basic Assumptions

- Single-tenant app
- Transactions are global (not user-scoped)
- New self-registered users start as Viewer
