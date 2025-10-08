-- Sample data for the real-time chat application

-- Insert sample users
INSERT INTO
    app_user (id, username, password)
VALUES (
        'user1',
        'somari_jack',
        'test'
    ),
    ('user2', 'jane_smith', 'test'),
    (
        'user3',
        'alice_johnson',
        'test'
    ),
    ('user4', 'bob_wilson', 'test')
ON CONFLICT (id) DO NOTHING;

-- Insert sample chat rooms
INSERT INTO
    chat_room (id, name)
VALUES ('room1', 'General Discussion'),
    ('room2', 'Random Chat'),
    ('room3', 'Tech Talk'),
    ('room4', 'Gaming Zone')
ON CONFLICT (id) DO NOTHING;

-- Insert sample chat messages
INSERT INTO
    chat_message (
        id,
        sender,
        receiver,
        content,
        timestamp,
        type,
        room_id
    )
VALUES (
        'msg1',
        'user1',
        NULL,
        'Hello everyone!',
        NOW() - INTERVAL '2 hours',
        'CHAT',
        'room1'
    ),
    (
        'msg2',
        'user2',
        NULL,
        'Hi there!',
        NOW() - INTERVAL '1 hour 30 minutes',
        'CHAT',
        'room1'
    ),
    (
        'msg3',
        'user1',
        NULL,
        'john_doe has joined the room',
        NOW() - INTERVAL '1 hour',
        'JOIN',
        'room2'
    ),
    (
        'msg4',
        'user3',
        NULL,
        'Welcome to Tech Talk!',
        NOW() - INTERVAL '45 minutes',
        'CHAT',
        'room3'
    ),
    (
        'msg5',
        'user2',
        'user1',
        'Hey John, how are you?',
        NOW() - INTERVAL '30 minutes',
        'PRIVATE',
        NULL
    ),
    (
        'msg6',
        'user4',
        NULL,
        'Anyone up for gaming?',
        NOW() - INTERVAL '15 minutes',
        'CHAT',
        'room4'
    ),
    (
        'msg7',
        'user1',
        'user2',
        'I am doing great, thanks!',
        NOW() - INTERVAL '10 minutes',
        'PRIVATE',
        NULL
    ),
    (
        'msg8',
        'user3',
        NULL,
        'alice_johnson has left the room',
        NOW() - INTERVAL '5 minutes',
        'LEAVE',
        'room3'
    )
ON CONFLICT (id) DO NOTHING;