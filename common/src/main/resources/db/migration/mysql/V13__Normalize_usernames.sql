UPDATE Account SET lastname=UCASE(lastname);
UPDATE Member SET lastname=UCASE(lastname);

UPDATE Account SET firstname =  CONCAT(UCASE(SUBSTRING(firstname,1,1)),'', LCASE(SUBSTRING(firstname,2,LENGTH(firstname))));
UPDATE Member SET firstname =  CONCAT(UCASE(SUBSTRING(firstname,1,1)),'', LCASE(SUBSTRING(firstname,2,LENGTH(firstname))));