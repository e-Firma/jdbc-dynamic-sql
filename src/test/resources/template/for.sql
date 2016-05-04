--##Section0
--FOR horse IN horses
INSERT INTO horses VALUES ('${horse.name}', ${horse.age});
--END_FOR

--##Section1
--FOR horse IN horses
   INSERT INTO horses VALUES ('${horse.name}', ${horse.age});
   --FOR dog IN doggies
      INSERT INTO home VALUES ('${dog.name}', '${horse.name}');
   --END_FOR
--END_FOR