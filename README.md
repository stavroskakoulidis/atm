# ATM Simulator
Welcome to the ATM Simulator project! This Java program simulates an ATM where users can perform various banking operations. 
The program follows a client-server approach, where a multithreaded server handles client requests related to ATM utilities and responds accordingly. All user data is stored in a MySQL server for secure and efficient management.

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Contributing](#contributing)

## Features

- **Client Identification**: Allows users to identify themselves to access their accounts.
- **Account Creation**: Users can create new accounts.
- **Withdraw**: Provides the ability to withdraw funds from existing accounts.
- **Deposit**: Allows users to deposit funds into their accounts.
- **Balance Update**: Provides real-time balance updates.
- **Transaction Logs**: Keeps a record of all transactions.
- **Exit**: Allows users to exit the ATM simulation.

## Getting Started

1. Clone this repository to your local machine.
2. Set up your MySQL database and configure the connection details in the program.
   - In the JDBC approach the configuration is done in the 'Database' class.
   - In the Hibernate approach the configuration is done in the 'hibernate.cgh.xml' file.
3. Make sure that the MySQL server is running and accessible with the provided configuration for the program to work correctly.
   Additionally, ensure that any required libraries or dependencies are installed. More specific:
   - If you are using the JDBC (Java Database Connectivity) approach to connect to the MySQL database, you'll need to ensure that the MySQL JDBC driver dependencies are installed.
   - If you are using the Hibernate approach to interact with the MySQL database, you'll need to include Hibernate-related dependencies, as long as the JDBC driver.
  
## Usage

1. Start the server by following the setup instructions.
2. Run the client to interact with the ATM simulation.
3. Follow the on-terminal prompts to access various ATM functionalities.

## Contributing

Contributions to this project are welcome! Feel free to submit issues, suggest improvements, or contribute code to enhance the functionality of the ATM simulation.
