CREATE TABLE account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_admin boolean DEFAULT false
);

CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE announcement (
      id INT AUTO_INCREMENT PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      text TEXT NOT NULL,
      date DATE NOT NULL,
      categoryId INT,
      FOREIGN KEY (categoryId) REFERENCES category(id)
);

CREATE TABLE account_category (
      accountId INT,
      categoryId INT,
      PRIMARY KEY (accountId, categoryId),
      FOREIGN KEY (accountId) REFERENCES account(id),
      FOREIGN KEY (categoryId) REFERENCES category(id)
);