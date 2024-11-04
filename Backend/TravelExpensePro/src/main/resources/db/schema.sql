-- CREATE

CREATE TABLE AppUser (
    username            VARCHAR(16)		NOT NULL	PRIMARY KEY,
    passwordhash        VARCHAR(254)	NOT NULL,	  -- To be determined based on java hash algorithm
    salt                VARCHAR(50)		NOT NULL	  -- To be determined based on java hash algorithm
);

CREATE TABLE ExpenseClaim (
    expenseClaimId      BIGINT			NOT NULL	PRIMARY KEY		AUTO_INCREMENT,
    username            VARCHAR(16)		NOT NULL,
    title               VARCHAR(10)		NOT NULL,
    amount              DECIMAL(5,2)	NOT NULL,
    description         VARCHAR(50)		NOT NULL
);

-- ALTER

ALTER TABLE ExpenseClaim
    ADD CONSTRAINT FK_ExpenseClaim_AppUser
        FOREIGN KEY (username)
            REFERENCES AppUser (username)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

-- RULES

ALTER TABLE AppUser
    ADD CONSTRAINT CHK_Username_Length
        CHECK (LENGTH(username) BETWEEN 3 AND 16);

ALTER TABLE ExpenseClaim
    ADD CONSTRAINT CHK_Title_Length
        CHECK (LENGTH(title) BETWEEN 3 AND 10);

ALTER TABLE ExpenseClaim
    ADD CONSTRAINT CHK_Amount_Range
        CHECK (amount BETWEEN 0 AND 10000);

ALTER TABLE ExpenseClaim
    ADD CONSTRAINT CHK_Description_Length
        CHECK (LENGTH(description) BETWEEN 3 AND 50);