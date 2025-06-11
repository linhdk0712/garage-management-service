# Garage Management Service API Specification

## Overview
This document provides the complete API specification for the Garage Management Service, a Spring Boot application that manages garage operations including customer management, appointments, vehicle services, inventory, and staff management.

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
The API uses JWT (JSON Web Token) authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

## Common Response Format
All API responses follow this standard format:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {}
}
```

## Pagination
For endpoints that return paginated results, the response includes:
```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 100
}
```

## API Endpoints

### 1. Authentication (`/auth`)

#### 1.1 Login
- **URL**: `POST /auth/login`
- **Description**: Authenticate user and return JWT token
- **Request Body**:
```json
{
  "username": "string",
  "password": "string"
}
```
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "token": "jwt_token",
    "refreshToken": "refresh_token",
    "id": 1,
    "username": "string",
    "email": "string",
    "roles": ["CUSTOMER"],
    "firstName": "string",
    "lastName": "string"
  }
}
```

#### 1.2 Refresh Token
- **URL**: `POST /auth/refresh-token`
- **Description**: Refresh JWT token using refresh token
- **Request Body**:
```json
{
  "refreshToken": "string"
}
```
- **Response**:
```json
{
  "accessToken": "new_jwt_token",
  "refreshToken": "refresh_token"
}
```

#### 1.3 Logout
- **URL**: `POST /auth/logout`
- **Description**: Logout user and invalidate refresh token
- **Query Parameters**:
  - `userId` (required): User ID
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": "Log out successful!"
}
```

#### 1.4 Register
- **URL**: `POST /auth/register`
- **Description**: Register new customer account
- **Request Body**:
```json
{
  "username": "string",
  "email": "string",
  "phone": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "address": "string",
  "city": "string",
  "state": "string",
  "zipCode": "string",
  "preferredContactMethod": "string",
  "roles": ["CUSTOMER"]
}
```
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "id": 1,
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string",
    "address": "string",
    "city": "string",
    "state": "string",
    "zipCode": "string",
    "preferredContactMethod": "string"
  }
}
```

### 2. Customer Management (`/customers`)

#### 2.1 Get Customer Profile by Username
- **URL**: `GET /customers/profile/{userName}`
- **Description**: Get customer profile information (requires authentication)
- **Path Parameters**:
  - `userName` (required): Username
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "id": 1,
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string",
    "address": "string",
    "city": "string",
    "state": "string",
    "zipCode": "string",
    "preferredContactMethod": "string",
    "registrationDate": "2024-01-01T12:00:00Z"
  }
}
```

#### 2.2 Get All Customer Profiles (Manager Only)
- **URL**: `GET /customers/profile`
- **Description**: Get paginated list of all customer profiles
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
  - `search` (optional): Search term
  - `status` (optional): Filter by status
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "firstName": "string",
        "lastName": "string",
        "email": "string",
        "phone": "string",
        "address": "string",
        "city": "string",
        "state": "string",
        "zipCode": "string",
        "preferredContactMethod": "string",
        "registrationDate": "2024-01-01T12:00:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 100
  }
}
```

### 3. Customer Appointments (`/customers/appointments`)

#### 3.1 Get Customer Appointments
- **URL**: `GET /customers/appointments`
- **Description**: Get paginated list of customer's appointments
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
  - `status` (optional): Filter by appointment status
  - `from` (optional): Filter by start date
  - `to` (optional): Filter by end date
  - `date` (optional): Filter by specific date
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "appointmentDate": "2024-01-01T12:00:00Z",
        "estimatedCompletion": "2024-01-01T14:00:00Z",
        "status": "SCHEDULED",
        "serviceType": "OIL_CHANGE",
        "description": "string",
        "createdAt": "2024-01-01T10:00:00Z",
        "updatedAt": "2024-01-01T10:00:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 50
  }
}
```

#### 3.2 Get Appointment by ID
- **URL**: `GET /customers/appointments/{id}`
- **Description**: Get specific appointment details
- **Path Parameters**:
  - `id` (required): Appointment ID
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "id": 1,
    "appointmentDate": "2024-01-01T12:00:00Z",
    "estimatedCompletion": "2024-01-01T14:00:00Z",
    "status": "SCHEDULED",
    "serviceType": "OIL_CHANGE",
    "description": "string",
    "createdAt": "2024-01-01T10:00:00Z",
    "updatedAt": "2024-01-01T10:00:00Z"
  }
}
```

#### 3.3 Create Appointment
- **URL**: `POST /customers/appointments`
- **Description**: Create new appointment
- **Request Body**:
```json
{
  "appointmentDate": "2024-01-01T12:00:00Z",
  "estimatedCompletion": "2024-01-01T14:00:00Z",
  "serviceType": "OIL_CHANGE",
  "description": "string"
}
```
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "id": 1,
    "appointmentDate": "2024-01-01T12:00:00Z",
    "estimatedCompletion": "2024-01-01T14:00:00Z",
    "status": "SCHEDULED",
    "serviceType": "OIL_CHANGE",
    "description": "string",
    "createdAt": "2024-01-01T10:00:00Z",
    "updatedAt": "2024-01-01T10:00:00Z"
  }
}
```

### 4. Vehicle Management (`/vehicles`)

#### 4.1 Get All Vehicles
- **URL**: `GET /vehicles`
- **Description**: Get paginated list of vehicles (filtered by user role)
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
  - `search` (optional): Search term
  - `make` (optional): Filter by vehicle make
  - `model` (optional): Filter by vehicle model
  - `year` (optional): Filter by vehicle year
  - `customerId` (optional): Filter by customer ID
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "vehicleId": 1,
        "make": "Toyota",
        "model": "Camry",
        "year": 2020,
        "licensePlate": "ABC123",
        "vin": "1HGBH41JXMN109186",
        "color": "White",
        "mileage": 50000,
        "lastServiceDate": "2023-12-01",
        "registrationDate": "2024-01-01T12:00:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 25
  }
}
```

#### 4.2 Create Vehicle
- **URL**: `POST /vehicles`
- **Description**: Create new vehicle
- **Request Body**:
```json
{
  "make": "Toyota",
  "model": "Camry",
  "year": 2020,
  "licensePlate": "ABC123",
  "vin": "1HGBH41JXMN109186",
  "color": "White",
  "mileage": 50000,
  "lastServiceDate": "2023-12-01"
}
```
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "vehicleId": 1,
    "make": "Toyota",
    "model": "Camry",
    "year": 2020,
    "licensePlate": "ABC123",
    "vin": "1HGBH41JXMN109186",
    "color": "White",
    "mileage": 50000,
    "lastServiceDate": "2023-12-01",
    "registrationDate": "2024-01-01T12:00:00Z"
  }
}
```

#### 4.3 Get Vehicle by ID
- **URL**: `GET /vehicles/{id}`
- **Description**: Get specific vehicle details
- **Path Parameters**:
  - `id` (required): Vehicle ID
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "vehicleId": 1,
    "make": "Toyota",
    "model": "Camry",
    "year": 2020,
    "licensePlate": "ABC123",
    "vin": "1HGBH41JXMN109186",
    "color": "White",
    "mileage": 50000,
    "lastServiceDate": "2023-12-01",
    "registrationDate": "2024-01-01T12:00:00Z"
  }
}
```

#### 4.4 Update Vehicle
- **URL**: `PUT /vehicles/{id}`
- **Description**: Update vehicle information
- **Path Parameters**:
  - `id` (required): Vehicle ID
- **Request Body**:
```json
{
  "make": "Toyota",
  "model": "Camry",
  "year": 2020,
  "licensePlate": "ABC123",
  "vin": "1HGBH41JXMN109186",
  "color": "White",
  "mileage": 50000,
  "lastServiceDate": "2023-12-01"
}
```
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "vehicleId": 1,
    "make": "Toyota",
    "model": "Camry",
    "year": 2020,
    "licensePlate": "ABC123",
    "vin": "1HGBH41JXMN109186",
    "color": "White",
    "mileage": 50000,
    "lastServiceDate": "2023-12-01",
    "registrationDate": "2024-01-01T12:00:00Z"
  }
}
```

#### 4.5 Delete Vehicle
- **URL**: `DELETE /vehicles/{id}`
- **Description**: Delete vehicle
- **Path Parameters**:
  - `id` (required): Vehicle ID
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": "Vehicle deleted successfully"
}
```

### 5. Inventory Management (`/inventory`)

#### 5.1 Get All Spare Parts (Manager Only)
- **URL**: `GET /inventory/parts`
- **Description**: Get paginated list of spare parts
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
  - `category` (optional): Filter by category
  - `search` (optional): Search term
  - `stockStatus` (optional): Filter by stock status
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Oil Filter",
        "partNumber": "OF001",
        "category": "FILTERS",
        "quantity": 50,
        "minQuantity": 10,
        "price": 15.99,
        "supplier": "AutoParts Inc"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 100
  }
}
```

#### 5.2 Get Spare Part by ID (Manager Only)
- **URL**: `GET /inventory/parts/{partId}`
- **Description**: Get specific spare part details
- **Path Parameters**:
  - `partId` (required): Part ID
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "id": 1,
    "name": "Oil Filter",
    "partNumber": "OF001",
    "category": "FILTERS",
    "quantity": 50,
    "minQuantity": 10,
    "price": 15.99,
    "supplier": "AutoParts Inc"
  }
}
```

#### 5.3 Get Low Stock Items (Manager Only)
- **URL**: `GET /inventory/low-stock`
- **Description**: Get paginated list of low stock items
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Oil Filter",
        "partNumber": "OF001",
        "category": "FILTERS",
        "quantity": 5,
        "minQuantity": 10,
        "price": 15.99,
        "supplier": "AutoParts Inc"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 15
  }
}
```

### 6. Manager Operations (`/manager`)

#### 6.1 Get Manager Profile
- **URL**: `GET /manager/profile/{userName}`
- **Description**: Get manager profile information
- **Path Parameters**:
  - `userName` (required): Username
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "id": 1,
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string",
    "position": "MANAGER",
    "hireDate": "2023-01-01",
    "department": "string"
  }
}
```

#### 6.2 Get All Appointments (Manager Only)
- **URL**: `GET /manager/appointments`
- **Description**: Get paginated list of all appointments
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
  - `status` (optional): Filter by appointment status
  - `from` (optional): Filter by start date
  - `to` (optional): Filter by end date
  - `date` (optional): Filter by specific date
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "appointmentDate": "2024-01-01T12:00:00Z",
        "estimatedCompletion": "2024-01-01T14:00:00Z",
        "status": "SCHEDULED",
        "serviceType": "OIL_CHANGE",
        "description": "string",
        "createdAt": "2024-01-01T10:00:00Z",
        "updatedAt": "2024-01-01T10:00:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 100
  }
}
```

#### 6.3 Get All Staff (Manager Only)
- **URL**: `GET /manager/staff`
- **Description**: Get paginated list of all staff members
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "firstName": "string",
        "lastName": "string",
        "email": "string",
        "phone": "string",
        "position": "MECHANIC",
        "hireDate": "2023-01-01",
        "department": "string"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 25
  }
}
```

#### 6.4 Get All Customers (Manager Only)
- **URL**: `GET /manager/customers`
- **Description**: Get paginated list of all customers
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
  - `search` (optional): Search term
  - `status` (optional): Filter by status
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "firstName": "string",
        "lastName": "string",
        "email": "string",
        "phone": "string",
        "address": "string",
        "city": "string",
        "state": "string",
        "zipCode": "string",
        "preferredContactMethod": "string"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 100
  }
}
```

#### 6.5 Get All Vehicles (Manager Only)
- **URL**: `GET /manager/vehicles`
- **Description**: Get paginated list of all vehicles
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
  - `search` (optional): Search term
  - `make` (optional): Filter by vehicle make
  - `model` (optional): Filter by vehicle model
  - `year` (optional): Filter by vehicle year
  - `customerId` (optional): Filter by customer ID
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "vehicleId": 1,
        "make": "Toyota",
        "model": "Camry",
        "year": 2020,
        "licensePlate": "ABC123",
        "vin": "1HGBH41JXMN109186",
        "color": "White",
        "mileage": 50000,
        "lastServiceDate": "2023-12-01",
        "registrationDate": "2024-01-01T12:00:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 50
  }
}
```

#### 6.6 Get Dashboard Statistics (Manager Only)
- **URL**: `GET /manager/reports/dashboard`
- **Description**: Get dashboard statistics and analytics
- **Query Parameters**:
  - `period` (optional, default: "week"): Time period for statistics
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "totalAppointments": 150,
    "completedAppointments": 120,
    "pendingAppointments": 30,
    "totalCustomers": 500,
    "totalRevenue": 25000.00,
    "lowStockItems": 15
  }
}
```

#### 6.7 Get Low Stock Items (Manager Only)
- **URL**: `GET /manager/inventory/low-stock`
- **Description**: Get paginated list of low stock items
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `sortBy` (optional, default: "id"): Sort field
  - `sortDir` (optional, default: "asc"): Sort direction (asc/desc)
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Oil Filter",
        "partNumber": "OF001",
        "category": "FILTERS",
        "quantity": 5,
        "minQuantity": 10,
        "price": 15.99,
        "supplier": "AutoParts Inc"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 15
  }
}
```

#### 6.8 Get Customer Statistics (Manager Only)
- **URL**: `GET /manager/reports/customers`
- **Description**: Get customer analytics and statistics
- **Query Parameters**:
  - `period` (optional, default: "MONTH"): Time period for statistics
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "newCustomers": 25,
    "returningCustomers": 150,
    "customerRetentionRate": 85.5,
    "averageCustomerValue": 500.00
  }
}
```

#### 6.9 Get Performance Analytics (Manager Only)
- **URL**: `GET /manager/analytics/performance`
- **Description**: Get performance analytics data
- **Query Parameters**:
  - `period` (optional, default: "month"): Time period for analytics
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "appointmentCompletionRate": 95.5,
    "averageServiceTime": 2.5,
    "customerSatisfaction": 4.8,
    "revenueGrowth": 12.5
  }
}
```

### 7. Staff Operations (`/staff`)

#### 7.1 Create Staff (Manager Only)
- **URL**: `POST /staff`
- **Description**: Create new staff member
- **Request Body**:
```json
{
  "username": "string",
  "email": "string",
  "phone": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "position": "MECHANIC",
  "department": "string",
  "hireDate": "2023-01-01"
}
```
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "id": 1,
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string",
    "position": "MECHANIC",
    "hireDate": "2023-01-01",
    "department": "string"
  }
}
```

#### 7.2 Get Staff Work Orders (Staff Only)
- **URL**: `GET /staff/work-orders`
- **Description**: Get paginated list of work orders assigned to staff
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `status` (optional): Filter by work order status
  - `from` (optional): Filter by start date
  - `to` (optional): Filter by end date
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "appointmentId": 1,
        "staffId": 1,
        "status": "IN_PROGRESS",
        "description": "string",
        "estimatedHours": 2.5,
        "actualHours": 2.0,
        "createdAt": "2024-01-01T10:00:00Z",
        "updatedAt": "2024-01-01T12:00:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 25
  }
}
```

#### 7.3 Get Staff Appointments (Staff Only)
- **URL**: `GET /staff/appointments`
- **Description**: Get paginated list of appointments assigned to staff
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 10): Page size
  - `status` (optional): Filter by appointment status
  - `from` (optional): Filter by start date
  - `to` (optional): Filter by end date
- **Response**:
```json
{
  "errorCode": "SUCCESS",
  "errorMessage": "Success",
  "tranDate": "2024-01-01 12:00:00",
  "data": {
    "content": [
      {
        "id": 1,
        "appointmentDate": "2024-01-01T12:00:00Z",
        "estimatedCompletion": "2024-01-01T14:00:00Z",
        "status": "SCHEDULED",
        "serviceType": "OIL_CHANGE",
        "description": "string",
        "createdAt": "2024-01-01T10:00:00Z",
        "updatedAt": "2024-01-01T10:00:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 30
  }
}
```

## Error Codes

| Error Code | Description |
|------------|-------------|
| SUCCESS | Operation completed successfully |
| 99 | Unauthorized access or authentication error |
| 404 | Resource not found |
| 500 | Internal server error |

## HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Invalid request data |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

## Role-Based Access Control

The API implements role-based access control with the following roles:

- **CUSTOMER**: Can access customer-specific endpoints
- **STAFF**: Can access staff-specific endpoints
- **MANAGER**: Can access all endpoints including management and analytics

## Data Types

### Common Data Types
- **Instant**: ISO 8601 timestamp format (e.g., "2024-01-01T12:00:00Z")
- **LocalDate**: ISO date format (e.g., "2024-01-01")
- **Integer**: Numeric ID fields
- **String**: Text fields with specified maximum lengths
- **Double**: Decimal numbers for prices and measurements

### Enums
- **Appointment Status**: SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
- **Service Type**: OIL_CHANGE, BRAKE_SERVICE, TIRE_ROTATION, etc.
- **Position**: MECHANIC, MANAGER, RECEPTIONIST
- **Stock Status**: IN_STOCK, LOW_STOCK, OUT_OF_STOCK

## Rate Limiting
Currently, no rate limiting is implemented. Consider implementing rate limiting for production use.

## CORS Configuration
The API allows cross-origin requests from all origins (`*`) with a maximum age of 3600 seconds.

## Security Considerations
1. All endpoints require JWT authentication except for login and register
2. Role-based access control is enforced on all endpoints
3. Input validation is implemented using Bean Validation
4. Passwords are encrypted using BCrypt
5. JWT tokens have expiration times
6. Refresh tokens are used for token renewal

## Versioning
The API uses URL versioning with the current version being `v1`.

## Documentation
This API specification is generated from the Spring Boot controllers and can be used with tools like Swagger/OpenAPI for interactive documentation. 