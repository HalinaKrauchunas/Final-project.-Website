CREATE TABLE my_db.sections
(
    id   int NOT NULL AUTO_INCREMENT,
    name varchar(15),
    PRIMARY KEY (id)
);

CREATE TABLE my_db.terms
(
    id         int NOT NULL AUTO_INCREMENT,
    name       varchar(15),
    definition text,
    source     text,
    section_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (section_id) REFERENCES my_db.sections (id)
);