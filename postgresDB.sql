SET
statement_timeout = 0;
SET
lock_timeout = 0;
SET
idle_in_transaction_session_timeout = 0;
SET
client_encoding = 'UTF8';
SET
standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET
check_function_bodies = false;
SET
xmloption = content;
SET
client_min_messages = warning;
SET
row_security = off;

--DROP DATABASE IF EXISTS assessment_db;
--
-- Name: assessment_db; Type: DATABASE; Schema: -; Owner: postgres
--
CREATE
DATABASE assessment_db ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';
ALTER
DATABASE assessment_db OWNER TO postgres;