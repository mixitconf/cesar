INSERT INTO mixit.Event (ID,CURRENT,YEAR) VALUES (6, TRUE ,2016);
UPDATE mixit.Event SET CURRENT=FALSE WHERE ID=5;

INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(10, 6, 'GOLD');

INSERT INTO `Member` (`DTYPE`, `ID`, `COMPANY`, `EMAIL`, `FIRSTNAME`, `LASTNAME`, `LOGIN`, `LONGDESCRIPTION`, `NBCONSULTS`, `PUBLICPROFILE`, `REGISTEREDAT`, `SHORTDESCRIPTION`, `LOGOURL`, `SESSIONTYPE`)
VALUES ('Sponsor', 5032, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5033, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5034, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5035, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5036, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5037, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5038, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5039, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5040, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5041, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5042, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL),
 ('Sponsor', 5043, 'Free slot', 'contact@mix-it.fr', null, 'Mix-IT', 'mixit', NULL, 1, TRUE, NULL, 'Soutenez Mix-IT 2016', 'sponsors/undefined.svg', NULL);

INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(1661, 6, 'GOLD');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5033, 6, 'GOLD');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5034, 6, 'GOLD');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5035, 6, 'GOLD');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5036, 6, 'GOLD');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5037, 6, 'GOLD');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5038, 6, 'GOLD');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5039, 6, 'GOLD');

INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(2871, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(992, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5034, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5035, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5036, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5037, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5038, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5039, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5040, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5041, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5042, 6, 'SILVER');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5043, 6, 'SILVER');

INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5032, 6, 'LANYARD');

INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5032, 6, 'PARTY');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5033, 6, 'PARTY');

INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5032, 6, 'BREAKFAST');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5033, 6, 'BREAKFAST');

INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5032, 6, 'LUNCH');
INSERT INTO MemberEvent (MEMBER_ID, EVENT_ID, LEVEL) VALUES(5033, 6, 'LUNCH');