-- Query 19 - Two Outers

-- In Oracle outer-join syntax:
-- SELECT DISTINCT AL1.IS_DWLGS_VAL, AL1.CU_ID,
--  AL1.CUOR_CUST_NM, AL1.CUIN_LST_NM, AL2.SRVPLN_ID
-- FROM PGE.CUST_SERV_ACCT AL1, PGE.SERV_PLAN AL2, PGE.REVN_DTL_RAND AL3
-- WHERE (AL3.SRVPLN_KEY=AL2.SRVPLN_KEY(+)
--  AND AL1.CUST_SERV_ACCT_KEY=AL3.CUST_SERV_ACCT_KEY(+))
--  AND (AL1.IS_DWLGS_VAL>2)
-- ORDER BY  5

SELECT DISTINCT AL1.IS_DWLGS_VAL, AL1.CU_ID,
 AL1.CUOR_CUST_NM, AL1.CUIN_LST_NM, AL2.SRVPLN_ID
FROM ((PGE.CUST_SERV_ACCT AL1
       LEFT OUTER JOIN PGE.REVN_DTL_RAND AL3
       ON AL1.CUST_SERV_ACCT_KEY=AL3.CUST_SERV_ACCT_KEY)
      LEFT OUTER JOIN PGE.SERV_PLAN AL2 on AL3.SRVPLN_KEY = AL2.SRVPLN_KEY)
WHERE AL1.IS_DWLGS_VAL > 2
ORDER BY 1,2,3,4,5
;