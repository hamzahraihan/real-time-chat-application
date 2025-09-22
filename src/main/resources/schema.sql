-- Schema for real-time chat application

-- Create app_user table
CREATE TABLE IF NOT EXISTS app_user (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL
);

-- Create chat_room table
CREATE TABLE IF NOT EXISTS chat_room (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Create chat_message table based on ChatMessage model
CREATE TABLE IF NOT EXISTS chat_message (
    id VARCHAR(255) PRIMARY KEY,
    sender VARCHAR(255),
    receiver VARCHAR(255),
    content VARCHAR(2000),
    timestamp TIMESTAMP WITH TIME ZONE,
    type VARCHAR(20) CHECK (
        type IN (
            'CHAT',
            'JOIN',
            'LEAVE',
            'PRIVATE'
        )
    ),
    room_id VARCHAR(255),
    FOREIGN KEY (room_id) REFERENCES chat_room (id)
);