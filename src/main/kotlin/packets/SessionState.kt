package packets

enum class SessionState {
    HANDSHAKE,
    STATUS,

    LOGIN_START,
    LOGIN_ACKNOWLEDGED,
}