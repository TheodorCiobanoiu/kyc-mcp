# Product Requirements Document: KYC-MCP Proof of Concept

**Author:** Theodor Ciobanoiu
**Date:** 2025-07-19
**Version:** 0.1

---

## Table of Contents

- [1. Introduction](#1-introduction)
    - [1.1. Overview](#11-overview)
    - [1.2. Problem Statement](#12-problem-statement)
    - [1.3. Target Audience](#13-target-audience)
- [2. Goals and Objectives](#2-goals-and-objectives)
    - [Key Objectives](#key-objectives)
- [3. Scope](#3-scope)
    - [3.1. In-Scope Features (PoC)](#31-in-scope-features-poc)
    - [3.2. Out-of-Scope Features](#32-out-of-scope-features)
- [4. Requirements](#4-requirements)
    - [4.1. Functional Requirements](#41-functional-requirements)
    - [4.2. Technical Requirements](#42-technical-requirements)
- [5. Assumptions](#5-assumptions)
- [6. Success Criteria](#6-success-criteria)

---

## 1. Introduction

### 1.1. Overview

This document outlines the requirements for a Proof of Concept (PoC) of the **Know Your Customer - Model Context
Protocol (KYC-MCP)** application. The project's goal is to build a backend system that serves as an **MCP server**,
exposing core KYC functionalities as standardized tools. This will allow an **MCP client**, such as an AI agent or Large
Language Model (LLM), to act as a compliance assistant, performing tasks like client data retrieval, creation, and basic
risk assessment by interacting with the provided tools.

### 1.2. Problem Statement

Compliance officers in the fintech industry spend significant time on manual, repetitive tasks for KYC procedures. An
AI-powered assistant could streamline these workflows, but it requires a robust, standardized interface to interact with
the underlying KYC data systems safely and efficiently. The Model Context Protocol (MCP) provides this standard,
enabling seamless integration between AI models and external tools.

### 1.3. Target Audience

- **Primary User (Client):** An **AI Agent** or any MCP-compatible client that consumes the tools exposed by our
  application.
- **MCP Server:** The KYC-MCP application itself, which implements the MCP tool server.
- **End-User:** A **Compliance Officer** who interacts with the AI agent to manage their KYC workload.

## 2. Goals and Objectives

The primary goal of this PoC is to **demonstrate the feasibility and value of implementing a KYC-focused MCP server to
expose compliance operations as tools for AI agents.**

### Key Objectives:

- **Validate Core Functionality:** Prove that essential KYC tasks (client search, creation, data retrieval) can be
  successfully executed by an MCP client via the exposed tools.
- **Establish a Foundation:** Create a stable, well-defined service layer and data model that can be extended in future
  phases.
- **Assess MCP Integration:** Showcase a clean, intuitive, and effective tool-based API that adheres to the Model
  Context Protocol specification.

## 3. Scope

### 3.1. In-Scope Features (PoC)

The scope of this PoC is strictly limited to the features defined in the MVP phase of the development plan.

| User Story                                                                 | Feature/MCP Tool                                                                                                                             |
|----------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| As an AI agent, I can search for clients by various criteria.              | **`search_clients`**: A tool to find clients by name, client type, or risk level.                                                            |
| As an AI agent, I can retrieve detailed client information.                | **`get_client_details`**: A tool to fetch a client's complete profile, including all associated persons.                                     |
| As an AI agent, I can add new clients and associate individuals with them. | **`create_client`**: A tool to add a new client to the system.<br>**`add_person_to_client`**: A tool to link a person to an existing client. |
| As an AI agent, I can identify high-risk clients for compliance review.    | **`get_high_risk_clients`**: A tool to retrieve a list of all clients currently classified as high-risk.                                     |

### 3.2. Out-of-Scope Features

All features planned for subsequent phases are explicitly out of scope for this PoC, including but not limited to:

- Advanced search and reporting (e.g., `generate_risk_summary`).
- Document management (uploading, listing, validation).
- Automated risk scoring engines.
- External data integrations (e.g., sanctions screening, PEP checks).
- Case management and workflow automation.

## 4. Requirements

### 4.1. Functional Requirements

The system shall act as an MCP server, providing the following tools with the described functionality:

- **`search_clients(name: string, type: ClientType, riskLevel: RiskLevel)`**:
    - **Input**: One or more optional search parameters (name, type, risk).
    - **Output**: A list of `ClientDTO` objects matching the criteria.
- **`get_client_details(clientId: UUID)`**:
    - **Input**: A unique client identifier.
    - **Output**: A single `ClientDTO` object containing the client's full details and a list of associated `PersonDTO`
      objects.
- **`create_client(request: CreateClientRequest)`**:
    - **Input**: A `CreateClientRequest` DTO containing the necessary information for a new client.
    - **Output**: The newly created `ClientDTO`.
- **`add_person_to_client(clientId: UUID, request: CreatePersonRequest)`**:
    - **Input**: The target client's ID and a `CreatePersonRequest` DTO for the new person.
    - **Output**: The updated `ClientDTO` with the new person included.
- **`get_high_risk_clients()`**:
    - **Input**: None.
    - **Output**: A list of `ClientDTO` objects where `riskLevel` is `HIGH`.

### 4.2. Technical Requirements

- The application will be built using Java/Spring Boot and will function as an MCP server.
- Core business logic will be encapsulated in `ClientService` and `PersonService`.
- Data will be persisted in a relational database.
- Basic data validation will be implemented for all tool inputs.
- The MCP tools will be implemented using a framework compatible with the Model Context Protocol, such as Spring AI.

## 5. Assumptions

- The AI agent (MCP client) is capable of making standardized tool calls according to the MCP specification.
- Basic client, person, and risk level data models are sufficient for the PoC.
- For the PoC, a simple, predefined set of `ClientType`, `RelationshipType` , and `RiskLevel` enums is acceptable.

## 6. Success Criteria

The Proof of Concept will be considered successful when the following criteria are met:

1. All five MCP tools (`search_clients`, `get_client_details`, `create_client`, `add_person_to_client`,
   `get_high_risk_clients`) are implemented and exposed correctly via the MCP server.
2. An MCP-compatible client (e.g., an AI agent) can successfully call each tool and receive a valid, structured response
   that conforms to the protocol.
3. A developer can demonstrate a simple end-to-end workflow (e.g., create a client, add a person, search for the client,
   retrieve its details) using an MCP client.
4. The tool API is clean, documented, and showcases the core value of the KYC domain.
5. All integration tests for the MCP tools pass successfully.

