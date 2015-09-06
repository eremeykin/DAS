DROP TABLE IF EXISTS "parameters";
CREATE TABLE "parameters" ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "name" VARCHAR, "parent" INTEGER, "script_arg" VARCHAR UNIQUE , "value" VARCHAR, "comment" VARCHAR, "editor" VARCHAR, "table" VARCHAR, "column" VARCHAR);
INSERT INTO parameters (id, name, parent, script_arg, value, comment, editor, "table", "column") VALUES (0,  '������', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1,  '������', 0, NULL, NULL, NULL, NULL, NULL, NULL),
(2,  '���������� ������', 1, "--inner", '0.028', '���������� ������', 'text', NULL, NULL),
(3,  '������� ������', 1, "--outer", '0.030', '������� ������', 'text', NULL, NULL),
(4,  '�����', 1, "--length", '0.06', '�����', 'text', NULL, NULL),
(5,  '��������', 1, NULL, NULL, '��������', NULL, NULL, NULL),
(6,  '��������', 5, NULL, NULL, '��������', 'cbox', 'materials', 'name'),
(7,  '������ ���������', 5, "--workpiece_young", "210000000000", '������ ���������', 'auto', '$6', 'young'),
(8,  '����������� ��������', 5, "--workpiece_poisson", "0.29", '����������� ��������', 'auto', '$6', 'poisson'),
(9,  '�������', 0, NULL, NULL, '�������', NULL, NULL, NULL),
(10, '�������� ��������', 9, "--displ", "0.001", '�������� ��������', 'text', NULL, NULL),
(11, '�������', 9, NULL, NULL, '�������', NULL, NULL, NULL),
(12, '�����', 11, "--jaw_length", '0.015', '�����', 'text', NULL, NULL),
(13, '������', 11, "--jaw_height", '0.015', '������', 'text', NULL, NULL),
(14, '������', 11, "--jaw_width", '0.015', '������', 'text', NULL, NULL),
(15, '��������', 9, NULL, NULL, '��������', NULL, NULL, NULL),
(16, '��������', 15, NULL, NULL, '��������', 'cbox', 'materials', 'name'),
(17, '������ ���������', 15, "--jaw_young", "210000000000", '������ ���������', 'auto', '$16', 'young'),
(18, '����������� ��������', 15, "--jaw_poisson", "0.29", '����������� ��������', 'auto', '$16', 'poisson'),
(19, '���� �������', 0, NULL, NULL, '���� �������', NULL, NULL, NULL),
(20, '��������', 19, NULL, NULL, '��������', NULL, NULL, NULL),
(21, '�����������', 20, "--tanf", NULL, '�����������', 'text', NULL, NULL),
(22, '����������', 20, "--radf", NULL, '����������', 'text', NULL, NULL),
(23, '������', 20, "--axlf", NULL, '������', 'text', NULL, NULL),
(24, '������� ���������', 19, "--anglef", NULL, '������� ���������', 'text', NULL, NULL);
SELECT * FROM parameters;