IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'advert_db')
BEGIN
    CREATE DATABASE advert_db;
END;

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'user_db')
BEGIN
    CREATE DATABASE user_db;
END;


