/**
 This file is not executed by flyway. It is only needed for a migration
 You have to install the prod backup on a local mysql, create the new shema (mixit) with flyway and use this scripts
 */

INSERT INTO mixit.Event (ID,CURRENT,YEAR) VALUES (2, FALSE ,2012), (3, FALSE ,2013), (4, FALSE ,2014), (5, TRUE ,2015);

INSERT INTO mixit.Interest (id, name)
SELECT id, name
FROM ad_32609ed48478829.interest;

INSERT INTO mixit.Member (
    DTYPE,  ID,     COMPANY,    EMAIL,  FIRSTNAME,  LASTNAME,   LOGIN,  LONGDESCRIPTION,    NBCONSULTS,
    PUBLICPROFILE,  REGISTEREDAT,   SHORTDESCRIPTION,   TICKETINGREGISTERED,    LEVEL,  LOGOURL)
SELECT
    DTYPE,  id,     company,    email,  firstname,  lastname,   login,  longDescription,    nbConsults,
    publicProfile,  registeredAt,   shortDescription,   false,  level,  logoUrl
FROM ad_32609ed48478829.member;

INSERT INTO mixit.Member_Interest (MEMBER_ID, INTERESTS_ID)
SELECT Member_id,	interests_id
FROM ad_32609ed48478829.member_interest;

INSERT INTO mixit.Member_Event (MEMBER_ID, EVENTS_ID)
SELECT sponsor_id, CASE events WHEN 'mixit12' THEN 2 WHEN 'mixit13' THEN 3 WHEN 'mixit14' THEN 4 WHEN 'mixit15' THEN 5 ELSE 6 END AS EVENTS_ID
FROM ad_32609ed48478829.sponsor_events;


INSERT INTO mixit.Article (ID,CONTENT,HEADLINE,NBCONSULTS,POSTEDAT,TITLE,VALID,AUTHOR_ID)
SELECT id, content, headline, nbConsults, postedAt, title, valid, author_id
FROM ad_32609ed48478829.article;

INSERT INTO mixit.ArticleComment (ID, CONTENT, POSTEDAT, ARTICLE_ID, MEMBER_ID)
SELECT id, content, postedAt, article_id, author_id
FROM ad_32609ed48478829.comment
WHERE article_id is not null;


INSERT INTO mixit.Session (
    DTYPE,ID,ADDEDAT,DESCRIPTION,FEEDBACK,FORMAT,GUEST,IDEAFORNOW,LANG,LEVEL,MAXATTENDEES, MESSAGEFORSTAFF, NBCONSULTS,SESSIONACCEPTED,
    SUMMARY,TITLE,VALID,EVENT_ID)
SELECT
    DTYPE,id, addedAt, description, feedback, DTYPE, guest, ideaForNow, lang, level ,maxAttendees, null,nbConsults,valid,summary, title,
    valid, CASE event WHEN 'mixit12' THEN 2 WHEN 'mixit13' THEN 3 WHEN 'mixit14' THEN 4 WHEN 'mixit15' THEN 5 ELSE 6 END AS EVENTS_ID
FROM ad_32609ed48478829.session;

INSERT INTO mixit.SessionComment (ID, CONTENT, POSTEDAT, SESSION_ID, MEMBER_ID, PRIVATE)
SELECT id, content, postedAt, session_id, author_id, private
FROM ad_32609ed48478829.comment
WHERE session_id is not null;

INSERT INTO mixit.Session_Interest (SESSION_ID, INTERESTS_ID)
SELECT session_id,	interests_id
FROM ad_32609ed48478829.session_interest;


INSERT INTO mixit.Session_Member (SESSIONS_ID, SPEAKERS_ID)
SELECT sessions_id,	speakers_id
FROM ad_32609ed48478829.session_member;

INSERT INTO mixit.SharedLink (ID,URL,NAME,ORDERNUM,MEMBER_ID)
SELECT id,	URL, name, ordernum, member_id
FROM ad_32609ed48478829.sharedlink;


INSERT INTO mixit.Vote (ID,VALUE,MEMBER_ID,SESSION_ID)
SELECT id, value, member_id, session_id
FROM ad_32609ed48478829.vote;

DELETE FROM mixit.Session_Member where  speakers_id in (168);
INSERT INTO mixit.Member_Event (MEMBER_ID, EVENTS_ID)
SELECT DISTINCT speakers_id, 5
FROM mixit.Session_Member;

UPDATE mixit.Session SET lang='fr' WHERE lang is null or lang='';

UPDATE mixit.Member SET LOGOURL=REPLACE(LOGOURL,'/public/images/', 'sponsors/') where LOGOURL is not null;


INSERT INTO mixit.Authority (ID,NAME) VALUES (1, 'ADMIN');
INSERT INTO mixit.Authority (ID,NAME) VALUES (2, 'MEMBER');
INSERT INTO mixit.Authority (ID,NAME) VALUES (3, 'SPEAKER');
INSERT INTO mixit.Authority (ID,NAME) VALUES (4, 'SPONSOR');

/*
INSERT INTO mixit.Account (ID, oauthId, provider,  password, token, defaultLanguage, valid, MEMBER_ID)
SELECT id, googleId, CASE provider WHEN 'Google' THEN 'GOOGLE' WHEN 'LinkIt' THEN 'CESAR' WHEN 'Twitter' THEN 'TWITTER' END AS provider, password, token, CASE lang WHEN 'fr' THEN 'fr' WHEN 'en' THEN 'en' ELSE 'fr'  END AS lang, 1, member_id
FROM ad_32609ed48478829.authaccount
WHERE provider<>'LinkedIn';

UPDATE mixit.Account a set a.LOGIN=(SELECT b.screenName from ad_32609ed48478829.account b where a.member_id=b.member_id and b.screenName is not null);
UPDATE mixit.Account a set a.LOGIN=(SELECT b.googleId from ad_32609ed48478829.account b where a.member_id=b.member_id and b.googleId is not null) where a.login is null;
UPDATE mixit.Account a set a.LOGIN=a.oauthId where a.login is null;
UPDATE mixit.Account a set a.LOGIN=(SELECT b.login from ad_32609ed48478829.member b where a.member_id=b.id and b.login is not null) where a.login is null;

UPDATE mixit.Account a set a.oauthId=(SELECT b.googleId from ad_32609ed48478829.account b where a.member_id=b.member_id and b.googleId is not null)  where oauthId is null;
UPDATE mixit.Account a set a.oauthId=(SELECT b.screenName from ad_32609ed48478829.account b where a.member_id=b.member_id and b.screenName is not null) where oauthId is null;
UPDATE mixit.Account a set a.oauthId=a.login where oauthId is null;

UPDATE mixit.`Member` set REGISTEREDAT=CURRENT_DATE where REGISTEREDAT is null;
UPDATE mixit.Account a set a.registeredAt=(SELECT b.registeredAt from mixit.Member b where a.member_id=b.id);
UPDATE mixit.Account a set a.email=(SELECT b.email from mixit.Member b where a.member_id=b.id);
UPDATE mixit.Account a set a.firstname=(SELECT b.firstname from mixit.Member b where a.member_id=b.id);
UPDATE mixit.Account a set a.lastname=(SELECT b.lastname from mixit.Member b where a.member_id=b.id);
UPDATE mixit.Account a set a.name=CONCAT(a.firstname, ' ', a.lastname);

INSERT INTO mixit.Account_Authority (ACCOUNT_ID,AUTHORITIES_ID)
SELECT id, 2
FROM mixit.Account;

INSERT INTO mixit.Account_Authority (ACCOUNT_ID,AUTHORITIES_ID)
SELECT a.id, 1
FROM mixit.Account a inner join mixit.Member b on a.member_id=b.id where b.DTYPE='Staff';
*/