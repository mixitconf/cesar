UPDATE account SET lastname=UCASE(lastname);
UPDATE member SET lastname=UCASE(lastname);

UPDATE account SET firstname =  CONCAT(UCASE(SUBSTRING(firstname,1,1)),'', LCASE(SUBSTRING(firstname,2,LENGTH(firstname))));
UPDATE member SET firstname =  CONCAT(UCASE(SUBSTRING(firstname,1,1)),'', LCASE(SUBSTRING(firstname,2,LENGTH(firstname))));