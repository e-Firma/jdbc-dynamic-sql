--without name
SELECT 1

--##First SQL Section Name
SELECT 2

--##ZOO SQL
SELECT * FROM ${zoo.horse.name} WHERE age > ${zoo.horse.age}-5 and age < ${zoo.horse.age}+5

--##Build Insert for ZOO
--FOR animal IN zoo
INSERT INTO animal (name, age) VALUES (${animal.name}, ${animal.age})
--END_FOR