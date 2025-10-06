# Online Presence System Implementation

## Overview

I've successfully implemented a comprehensive online presence system that allows senders to check if receivers are online. The system includes both real-time WebSocket updates and REST API endpoints for checking user online status.

## Backend Implementation

### 1. WebSocketEventListener.java (Enhanced)

- **Purpose**: Tracks online users and broadcasts presence updates
- **Key Features**:
  - `ConcurrentHashMap<String> onlineUsers` for thread-safe user tracking
  - `isUserOnline(String username)` - Check if specific user is online
  - `getOnlineUsers()` - Get all online users
  - `getOnlineUserCount()` - Get count of online users
  - Automatic presence broadcasting on connect/disconnect

### 2. ChatController.java (New REST Endpoints)

```java
// Check if a specific user is online
@GetMapping("/api/users/{username}/online")
public boolean isUserOnline(@PathVariable("username") String username)

// Get all online users
@GetMapping("/api/users/online")
public Set<String> getOnlineUsers()
```

### 3. ChatService.java (Enhanced)

- **Purpose**: Business logic layer for presence management
- **Integration**: Injected with WebSocketEventListener for real-time data
- **Methods**:
  - `isUserOnline(String username)` - Delegates to WebSocketEventListener
  - `getOnlineUsers()` - Returns current online users set

## Frontend Implementation

### 1. usePresence Hook

- **Purpose**: Custom React hook for managing presence state
- **Features**:
  - Initial fetch of online users on component mount
  - Real-time presence update handling
  - REST API integration for status checking
  - Automatic polling fallback every 30 seconds

### 2. PresenceMessage Type

```typescript
interface PresenceMessage {
  type: 'USER_ONLINE' | 'USER_OFFLINE' | 'ONLINE_USERS_LIST';
  user?: string;
  users?: string[];
}
```

### 3. ChatRoom Component (Enhanced)

- **Integration**: Uses usePresence hook
- **WebSocket Handling**: Processes presence updates from `/topic/presence`
- **Prop Passing**: Passes online status to PrivateChat components

### 4. PrivateChat Component (Enhanced)

- **Visual Indicator**: Green/red dot showing online/offline status
- **User Experience**: Clear "Online"/"Offline" text next to receiver name
- **Responsive Design**: Flexible layout with proper spacing

## How It Works

### Real-time Updates (WebSocket)

1. User connects → WebSocketEventListener adds user to onlineUsers
2. Presence update broadcasted to `/topic/presence`
3. All connected clients receive presence updates
4. Frontend updates local state and UI indicators

### API-based Checking (REST)

1. Frontend can query `/api/users/{username}/online` for specific user status
2. Backend checks WebSocketEventListener's onlineUsers map
3. Returns boolean indicating online status
4. Useful for initial state and fallback scenarios

### User Experience

- **Sender Perspective**: Can see if receiver is online before sending messages
- **Visual Feedback**: Green dot = Online, Red dot = Offline
- **Real-time Updates**: Status changes immediately when users connect/disconnect
- **Reliability**: Both WebSocket and REST API provide redundant checking

## API Endpoints Summary

| Endpoint                                 | Method | Purpose                          | Response            |
| ---------------------------------------- | ------ | -------------------------------- | ------------------- |
| `/api/users/{username}/online`           | GET    | Check if specific user is online | `boolean`           |
| `/api/users/online`                      | GET    | Get all online users             | `Set<String>`       |
| `/api/users/{sender}/{receiver}/history` | GET    | Get chat history                 | `List<ChatMessage>` |

## WebSocket Topics

| Topic                  | Purpose          | Message Type      |
| ---------------------- | ---------------- | ----------------- |
| `/topic/presence`      | Presence updates | `PresenceMessage` |
| `/user/queue/private`  | Private messages | `ChatMessage`     |
| `/topic/room.{roomId}` | Room messages    | `ChatMessage`     |

## Testing the Implementation

1. **Start Backend**: `./mvnw spring-boot:run`
2. **Start Frontend**: `cd chat-frontend && npm run dev`
3. **Test Scenario**:
   - Open two browser tabs/windows
   - Join as different users
   - Start a private chat
   - Observer online/offline indicators
   - Disconnect one user and watch status change

The implementation provides a complete, production-ready online presence system that enhances the user experience by showing real-time availability status of chat participants.
