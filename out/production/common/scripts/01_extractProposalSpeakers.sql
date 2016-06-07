-- This request extracts all the sessions with their speakers. The status is important to know the state
-- ACCEPTED, REJECTED, SUBMITTED (rescue sessions keep the status SUBMITTED)
SELECT p.title, p.status,m.firstname, m.lastname, m.email
FROM proposal p inner join proposal_member pm on pm.proposal_id=p.id inner join member m on m.id=pm.speakers_id
order by p.status, p.title;