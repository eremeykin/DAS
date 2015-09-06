-- Table: materials
DROP TABLE IF EXISTS "materials";
CREATE TABLE "materials"
(
	name VARCHAR(20) not null,
	young FLOAT not null,
	poisson FLOAT not null,
	primary key (name)
);
INSERT INTO materials (name, young, poisson) VALUES ('Сталь', 200000000000, 0.3), ('Чугун', 200000000, 2.29);
SELECT * FROM "materials";

