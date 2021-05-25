FROM postgres:13.3

COPY ./ /

RUN chmod +x ./psqlseed-entrypoint.sh
ENTRYPOINT ["./psqlseed-entrypoint.sh"]