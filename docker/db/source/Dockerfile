FROM mysql:8.0

# MySQL 설정 파일 복사
COPY my.cnf /etc/mysql/conf.d/source.cnf
RUN chmod 644 /etc/mysql/conf.d/source.cnf

# 초기 1회 실행 SQL (볼륨이 처음 생성될 때만 실행됨)
COPY init.sql /docker-entrypoint-initdb.d/init.sql
RUN chmod 644 /docker-entrypoint-initdb.d/init.sql