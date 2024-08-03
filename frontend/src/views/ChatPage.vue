<template>
  <div id="app">
    <div class="chat-container">
      <div v-if="connected" class="status connected-status">
        <p>Connected to server</p>
      </div>
      <div v-else class="status disconnected-status">
        <p>Disconnected from server</p>
      </div>

      <div class="chat-box">
        <div class="messages">
          <div v-for="(item, idx) in messages" :key="idx" class="message">
            <div class="message-user">{{ item.memberId }}</div>
            <div class="message-content">{{ item.message }}</div>
            <div class="message-timestamp">{{ item.createdAt }}</div>
          </div>
        </div>
        <div class="message-input">
          <input 
            v-model="message" 
            type="text" 
            @keyup.enter="sendMessage" 
            placeholder="Enter a message"
            :disabled="!connected"
          >
          <button 
            @click="sendMessage" 
            :disabled="!connected || !message"
          >
            Send
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'ChatPage',
  data() {
    return {
      message: "",
      stompClient: null,
      connected: false,
      chatRoomId: this.$route.params.chatRoomId,
    };
  },
  computed: {
    ...mapGetters('chat', ['messages']),
  },
  created() {
    this.connect();
  },
  beforeDestroy() {
    this.disconnect();
  },
  methods: {
    ...mapActions('chat', ['addMessage']),
    sendMessage() {
      if (this.message) {
        const now = new Date().toISOString();
        const msg = { 
          chatRoomId: this.chatRoomId,
          memberId: this.memberId,
          message: this.message,
          createdAt: now
        };
        this.send(msg);
        this.message = '';
      }
    },
    send(msg) {
      if (this.stompClient && this.stompClient.connected) {
        const accessToken = localStorage.getItem('accessToken');
        const headers = { 'Authorization': `${accessToken}` };
        this.stompClient.send("/app/receive", JSON.stringify(msg), headers);
        console.log('Message sent:', msg);
      } else {
        console.error("STOMP client is not connected.");
      }
    },
    connect() {
      const serverURL = "/ws";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`Connecting to ${serverURL}...`);
      this.stompClient.connect(
        {},
        () => {
          this.connected = true;
          console.log('Connected to WebSocket server.');
          this.stompClient.subscribe(`/topic/${this.chatRoomId}`, res => {
            console.log('STOMP message received:', res.body);
            try {
              const parsedMessage = JSON.parse(res.body);
              console.log('Parsed message:', parsedMessage);
              this.addMessage(parsedMessage);
              console.log('Updated messages:', this.messages);
            } catch (error) {
              console.error('Error parsing message:', error, res.body);
            }
          });
        },
        error => {
          console.error('STOMP connection error:', error);
          this.connected = false;
        }
      );
    },
    disconnect() {
      if (this.stompClient) {
        this.stompClient.disconnect(() => {
          this.connected = false;
          console.log('Disconnected from WebSocket server.');
        });
      }
    }
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 800px;
  margin: 0 auto;
  background-color: #f0f0f0;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.status {
  padding: 10px;
  text-align: center;
  font-weight: bold;
}

.connected-status {
  color: green;
  background-color: #e6ffe6;
}

.disconnected-status {
  color: red;
  background-color: #ffe6e6;
}

.chat-box {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px);
  overflow: hidden;
}

.messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  background-color: #fff;
  height: 100%; /* Ensure it's taking up the full height */
}

.message {
  margin-bottom: 10px;
  padding: 8px;
  border-radius: 4px;
  background-color: #f9f9f9;
}

.message-user {
  font-weight: bold;
  margin-bottom: 4px;
}

.message-content {
  color: #333;
}

.message-input {
  display: flex;
  padding: 10px;
  background-color: #fff;
  border-top: 1px solid #ddd;
}

.message-input input[type="text"] {
  flex: 1;
  padding: 8px;
  margin-right: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.message-input button {
  padding: 8px 12px;
  background-color: #007bff;
  border: none;
  border-radius: 4px;
  color: #fff;
  cursor: pointer;
}

.message-input button:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}

.message-input input:disabled {
  background-color: #e9ecef;
}
</style>