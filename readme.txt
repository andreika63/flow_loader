Utility for load data from text file (flow-stat) to the Oracle database.
Text files getting by script:
----------
#!/bin/sh
dst=dst
src=src
year=`date -v-1d '+%Y'`
month=`date -v-1d '+%m'`
day=`date -v-1d '+%d'`
pref=/var/db/flows/MSR/STAT
/usr/local/bin/flow-cat /var/db/flows/MSR/$year/$year-$month/$year-$month-$day | /usr/local/bin/flow-nfilter -F pit-dst-ip | /usr/local/bin/flow-stat -f8 | grep -v "#" > $pref/$year-$month-$day$dst.txt
/usr/local/bin/flow-cat /var/db/flows/MSR/$year/$year-$month/$year-$month-$day | /usr/local/bin/flow-nfilter -F pit-src-ip | /usr/local/bin/flow-stat -f9 | grep -v "#" > $pref/$year-$month-$day$src.txt
----------
command line arguments example:
"jdbc:oracle:thin:user/password@127.0.0.1:1521:XE" "/stat"

loaded files will be moved to subfolder "/stat/loaded" (subfolder will be created automatically)
if data exist in database, it will be updated.

script for make table in database:
----------
  CREATE TABLE "FLOWSTAT"
   ("FLOWTYPE" VARCHAR2(3 CHAR),
	"FLOWDATE" VARCHAR2(10 CHAR),
	"IPADDR" VARCHAR2(15 CHAR),
	"FLOWS" VARCHAR2(30 CHAR),
	"OCTETS" VARCHAR2(30 CHAR),
	"PACKETS" VARCHAR2(30 CHAR)
   );
----------

