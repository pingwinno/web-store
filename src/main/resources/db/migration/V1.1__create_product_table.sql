create table PRODUCT
(
    ID            SERIAL         PRIMARY KEY,
    NAME          VARCHAR(255)   NOT NULL,
    DESCRIPTION   VARCHAR(255)   NOT NULL,
    PRICE         DECIMAL(13, 2) NOT NULL,
    CREATION_DATE DATE           NOT NULL
);