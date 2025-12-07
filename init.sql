USE master;
GO

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
GO

-- Создание таблицы users
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
    CREATE TABLE users (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        email NVARCHAR(255) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL,
        role NVARCHAR(20) NOT NULL,
        CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'))
    );
    PRINT 'Table users created successfully.';
END
ELSE
BEGIN
    PRINT 'Table users already exists.';
END
GO

-- Создание таблицы task_groups
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='task_groups' AND xtype='U')
BEGIN
    CREATE TABLE task_groups (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        user_id BIGINT NOT NULL,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
    PRINT 'Table task_groups created successfully.';
END
ELSE
BEGIN
    PRINT 'Table task_groups already exists.';
END
GO

-- Создание таблицы tasks
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tasks' AND xtype='U')
BEGIN
    CREATE TABLE tasks (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        title NVARCHAR(500) NOT NULL,
        description NVARCHAR(2000),
        status NVARCHAR(20) NOT NULL DEFAULT 'PLANNED',
        user_id BIGINT NOT NULL,
        group_id BIGINT,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME NOT NULL DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (group_id) REFERENCES task_groups(id) ON DELETE SET NULL,
        CONSTRAINT chk_status CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'COMPLETED'))
    );
    PRINT 'Table tasks created successfully.';
END
ELSE
BEGIN
    PRINT 'Table tasks already exists.';
END
GO

-- Создание индексов
CREATE INDEX idx_tasks_user_id ON tasks(user_id);
CREATE INDEX idx_tasks_group_id ON tasks(group_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_task_groups_user_id ON task_groups(user_id);
GO

PRINT 'Database initialization completed successfully.';
GO