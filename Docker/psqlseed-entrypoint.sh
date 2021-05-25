#!/bin/sh

PGPASSWORD=example dropdb -p 5432 -h db -U postgres -e test_1
PGPASSWORD=example createdb -p 5432 -h db -U postgres -e test_1

PGPASSWORD=example psql -h db -d test_1 -U postgres -e -f /SQL/sql_stefan.sql
PGPASSWORD=example psql -h db -d test_1 -U postgres -e -f /SQL/sql_elias.sql