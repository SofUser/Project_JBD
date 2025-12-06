-- Создание базы данных, если её нет
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'Project_JBD')
BEGIN
    CREATE DATABASE Project_JBD;
    PRINT 'Database Project_JBD created successfully.';
END
ELSE
BEGIN
    PRINT 'Database Project_JBD already exists.';
END
GO

-- Используем созданную базу данных
USE Project_JBD;
GOы